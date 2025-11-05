package com.server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.server.domain.AgricultureDevice;
import com.server.util.SerialCommandExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 传感器通信服务
 * 核心服务，负责通过串口与传感器进行周期性的指令收发和数据解析。
 * 1. 在应用启动时，从数据库加载所有传感器设备。
 * 2. 为每个传感器创建一个独立的线程任务，进行周期性轮询。
 * 3. 发送指令给传感器，读取并解析返回的数据。
 * 4. 成功收到数据后，更新设备在Redis中的在线状态。
 * 5. 将解析后的数据交给DataProcessingService进行后续处理和存储。
 */
@Service
@ConditionalOnProperty(name = "iot.enabled", havingValue = "true")
public class SensorCommunicationService {

    private static final Logger log = LoggerFactory.getLogger(SensorCommunicationService.class);

    @Autowired
    private SerialPortService serialPortService; // 串口服务

    @Autowired
    private AgricultureDataProcessingService agricultureDataProcessingService; // 数据处理服务，用于存储解析后的数据

    @Autowired
    private AgricultureDeviceService deviceService; // 设备信息服务，用于查询设备基础信息

    @Autowired
    private SerialCommandExecutor serialCommandExecutor; // 注入 SerialCommandExecutor

    // 存储每个设备的采集任务线程状态
    private final ConcurrentHashMap<Long, Thread> deviceCollectThreads = new ConcurrentHashMap<>();

    /**
     * 确保此方法在SensorCommunicationService的Bean初始化后立即执行。
     * 初始化线程池。
     */
    @PostConstruct
    public void init() {
    }

    /**
     * 监听应用就绪事件（ApplicationReadyEvent）。
     * spring boot应用完全启动并准备好接收请求时，调用此方法。
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        startSensorCommunication();
    }

    /**
     * Spring容器销毁SensorCommunicationService的Bean之前调用。
     * 执行资源清理工作，优雅地关闭所有采集任务线程。
     */
    @PreDestroy
    public void destroy() {
        log.info("正在关闭传感器通信服务...");
        // 停止所有运行中的采集任务
        for (Long deviceId : deviceCollectThreads.keySet()) {
            stopSensorCollectLoop(deviceId);
        }
        deviceCollectThreads.clear();
        log.info("传感器通信服务已关闭");
    }

    /**
     * 启动总的传感器通信流程。
     * 查询所有需要轮询的传感器，并逐一启动任务。
     */
    private void startSensorCommunication() {
        try {
            List<AgricultureDevice> sensors = getSensorDevices();
            log.info("启动传感器通信流程，查询到 {} 个传感器设备", sensors.size());
            
            if (sensors.isEmpty()) {
                log.warn("未查询到任何传感器设备，请检查数据库中的设备配置");
                return;
            }
            
            int startedCount = 0;
            for (AgricultureDevice sensor : sensors) {
                // 跳过未配置指令的设备
                String command = sensor.getSensorCommand();
                if (command == null || "null".equals(command) || command.trim().isEmpty()) {
                    continue;
                }
                
                startSensorCollectLoop(sensor);
                startedCount++;
            }
            log.info("传感器通信流程启动完成，成功启动 {} 个采集任务", startedCount);
        } catch (Exception e) {
            log.error("启动传感器通信流程失败", e);
        }
    }

    /**
     * 从数据库获取所有需要轮询的传感器设备列表。
     * @return 传感器设备列表
     */
    private List<AgricultureDevice> getSensorDevices() {
        QueryWrapper<AgricultureDevice> queryWrapper = new QueryWrapper<>();
        // '1' 空气传感器, '2' 土壤传感器
        queryWrapper.in("device_type_id", Arrays.asList("1", "2"));
        return deviceService.list(queryWrapper);
    }

    /**
     * 启动单个传感器的采集循环（通过全局队列串行化）。
     */
    private void startSensorCollectLoop(AgricultureDevice sensor) {
        Long sensorId = sensor.getId();
        
        // 如果该设备已经有运行中的采集任务，先停止它
        stopSensorCollectLoop(sensorId);
        
        Thread collectThread = new Thread(() -> {
            String sensorName = sensor.getDeviceName();
            String commandHexStr = sensor.getSensorCommand();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    serialCommandExecutor.submit(() -> {
                        synchronized (serialPortService.getSerialLock()) {
                            try {
                                // 实时从数据库获取最新的设备信息，检查用户控制开关
                                AgricultureDevice currentDevice = deviceService.getById(sensorId);
                                if (currentDevice == null) {
                                    log.warn("设备 {} (ID: {}) 不存在，跳过数据处理", sensorName, sensorId);
                                    return;
                                }
                                
                                // 检查用户控制开关，如果为 '0'（关闭），则跳过本次数据采集
                                String userControlSwitch = currentDevice.getUserControlSwitch();
                                if (userControlSwitch != null && "0".equals(userControlSwitch)) {
                                    return;
                                }
                                
                                byte[] commandBytes = hexStringToByteArray(commandHexStr);
                                serialPortService.writeToSerial(commandBytes);
                                Thread.sleep(200);
                                byte[] response = serialPortService.readFromSerial(256);
                                if (response != null && response.length > 0) {
                                    // 再次确认设备信息（因为可能已经更新）
                                    AgricultureDevice latestDevice = deviceService.getById(sensorId);
                                    if (latestDevice == null) {
                                        log.warn("设备 {} (ID: {}) 不存在，跳过数据处理", sensorName, sensorId);
                                        return;
                                    }
                                    
                                    // 再次检查用户控制开关（防止在采集过程中被关闭）
                                    String latestUserControlSwitch = latestDevice.getUserControlSwitch();
                                    if (latestUserControlSwitch != null && "0".equals(latestUserControlSwitch)) {
                                        return;
                                    }
                                    
                                    String deviceType = latestDevice.getDeviceTypeId();
                                    Map<String, Object> parsedData = parseSensorData(response, deviceType);
                                    // 检查解析结果是否为空
                                    if (parsedData == null || parsedData.isEmpty()) {
                                        log.warn("设备 {} (ID: {}) 解析后的数据为空，可能数据格式不正确", 
                                            sensorName, sensorId);
                                    } else {
                                        parsedData.put("deviceId", sensorId);
                                        parsedData.put("deviceName", latestDevice.getDeviceName());
                                        parsedData.put("type", getDataTypeByDeviceType(deviceType));
                                        // 传递温室ID（如果设备有绑定温室）
                                        if (latestDevice.getPastureId() != null && !latestDevice.getPastureId().isEmpty()) {
                                            try {
                                                parsedData.put("pastureId", Long.parseLong(latestDevice.getPastureId()));
                                            } catch (NumberFormatException e) {
                                                log.warn("设备 {} (ID: {}) 的pasture_id格式不正确: {}", 
                                                    latestDevice.getDeviceName(), sensorId, latestDevice.getPastureId());
                                            }
                                        }
                                        agricultureDataProcessingService.processAndStore(parsedData);
                                    }
                                }
                            } catch (Exception e) {
                                log.error("采集任务异常: {}", e.getMessage(), e);
                            }
                        }
                    });
                    // 从数据库获取最新的采集间隔配置
                    AgricultureDevice latestDevice = deviceService.getById(sensorId);
                    long collectIntervalMs = 5000; // 默认5秒
                    if (latestDevice != null && latestDevice.getCollectInterval() != null && latestDevice.getCollectInterval() > 0) {
                        // collectInterval 单位是秒，转换为毫秒
                        collectIntervalMs = latestDevice.getCollectInterval() * 1000;
                    }
                    Thread.sleep(collectIntervalMs); // 使用设备配置的采集间隔
                } catch (InterruptedException e) {
                    break;
                }
            }
            deviceCollectThreads.remove(sensorId);
        }, "Sensor-Collect-" + sensor.getId());
        
        collectThread.setDaemon(true);
        collectThread.start();
        deviceCollectThreads.put(sensorId, collectThread);
    }

    /**
     * 停止指定设备的采集任务
     * @param deviceId 设备ID
     */
    public void stopSensorCollectLoop(Long deviceId) {
        Thread thread = deviceCollectThreads.get(deviceId);
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            deviceCollectThreads.remove(deviceId);
        }
    }

    /**
     * 启动指定设备的采集任务（动态启动）
     * @param deviceId 设备ID
     */
    public void startSensorCollectLoopByDeviceId(Long deviceId) {
        try {
            AgricultureDevice device = deviceService.getById(deviceId);
            if (device == null) {
                log.warn("设备 ID: {} 不存在，无法启动采集任务", deviceId);
                return;
            }
            
            // 检查是否为传感器设备
            String deviceTypeId = device.getDeviceTypeId();
            if (!"1".equals(deviceTypeId) && !"2".equals(deviceTypeId)) {
                log.warn("设备 ID: {} 不是传感器设备（类型ID: {}），无法启动采集任务", deviceId, deviceTypeId);
                return;
            }
            
            // 检查用户控制开关
            String userControlSwitch = device.getUserControlSwitch();
            if (userControlSwitch != null && "0".equals(userControlSwitch)) {
                log.warn("设备 ID: {} 用户控制开关为关闭状态，无法启动采集任务", deviceId);
                return;
            }
            
            // 检查是否已配置指令
            String command = device.getSensorCommand();
            if (command == null || "null".equals(command) || command.trim().isEmpty()) {
                log.warn("设备 ID: {} 没有配置指令，无法启动采集任务", deviceId);
                return;
            }
            
            // 检查是否已经有运行中的任务
            if (deviceCollectThreads.containsKey(deviceId)) {
                Thread existingThread = deviceCollectThreads.get(deviceId);
                if (existingThread != null && existingThread.isAlive()) {
                    return;
                }
            }
            
            startSensorCollectLoop(device);
        } catch (Exception e) {
            log.error("启动设备 ID: {} 的采集任务失败", deviceId, e);
        }
    }

    /**
     * 数据解析的主分发方法。
     * 根据设备类型，调用相应的具体解析方法。
     * @param data 从串口读取的原始字节数组
     * @param deviceType 设备的类型ID ("3" 空气传感器, "4" 土壤传感器)
     * @return 包含解析后键值对的Map
     */
    private Map<String, Object> parseSensorData(byte[] data, String deviceType) {
        Map<String, Object> result = new HashMap<>();
        try {
            switch (deviceType) {
                case "1": // 空气传感器
                    result.putAll(parseAirSensorData(data));
                    break;
                case "2": // 土壤传感器
                    result.putAll(parseSoilSensorData(data));
                    break;
                default:
                    log.warn("未知设备类型: {}, 数据长度: {}", deviceType, data.length);
                    result.put("raw_data", bytesToHexString(data));
            }
        } catch (Exception e) {
            log.error("解析传感器数据出错: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 解析空气传感器数据（温湿度、光照）
     */
    private Map<String, Object> parseAirSensorData(byte[] data) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (data.length != 11 || data[0] != 0x01 || data[1] != 0x03) {
                log.error("空气传感器数据格式错误: 长度={}, 首字节=0x{}, 次字节=0x{}", 
                    data.length, 
                    data.length > 0 ? String.format("%02X", data[0] & 0xFF) : "00",
                    data.length > 1 ? String.format("%02X", data[1] & 0xFF) : "00");
                return result;
            }

            // 温度解析（16位有符号整数，精度0.1℃）
            int tempRaw = ((data[3] & 0xFF) << 8) | (data[4] & 0xFF);
            double temperature;
            if ((tempRaw & 0x8000) != 0) {
                temperature = -((0xFFFF - tempRaw) + 1) / 10.0;
            } else {
                temperature = tempRaw / 10.0;
            }

            // 湿度解析（16位无符号整数，精度0.1%）
            double humidity = (((data[5] & 0xFF) << 8) | (data[6] & 0xFF)) / 10.0;

            // 光照解析（16位无符号整数，单位Lux）
            int illuminanceRaw = ((data[7] & 0xFF) << 8) | (data[8] & 0xFF);
            int illuminance = illuminanceRaw * 10; // 乘以10转换为实际Lux值
            
            result.put("temperature", Math.round(temperature * 10.0) / 10.0);
            result.put("humidity", Math.round(humidity * 10.0) / 10.0);
            result.put("illuminance", (double) illuminance);
        } catch (Exception e) {
            log.error("解析空气传感器数据出错: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 解析土壤传感器数据
     * 数据格式：设备地址(1) + 功能码(1) + 数据长度(1) + 数据(64) + CRC(2) = 69字节
     */
    private Map<String, Object> parseSoilSensorData(byte[] data) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 土壤传感器数据格式：02 03 40 [64字节数据] [2字节CRC] = 69字节
            if (data.length != 69 || data[0] != 0x02 || data[1] != 0x03) {
                log.error("土壤传感器数据格式错误: 长度={}, 期望=69, 首字节=0x{}, 次字节=0x{}", 
                    data.length,
                    data.length > 0 ? String.format("%02X", data[0] & 0xFF) : "00",
                    data.length > 1 ? String.format("%02X", data[1] & 0xFF) : "00");
                return result;
            }

            // 数据从索引3开始（索引0:设备地址, 1:功能码, 2:数据长度）
            int idx = 3; // 数据起始位置

            // 土壤温度（精度0.1℃）
            int tempRaw = ((data[idx] & 0xFF) << 8) | (data[idx + 1] & 0xFF);
            idx += 2;
            double soilTemp;
            if ((tempRaw & 0x8000) != 0) {
                soilTemp = -((0xFFFF - tempRaw) + 1) / 10.0;
            } else {
                soilTemp = tempRaw / 10.0;
            }

            // 土壤湿度（精度0.1 m³/m³）
            double soilHumidity = (((data[idx] & 0xFF) << 8) | (data[idx + 1] & 0xFF)) / 10.0;
            idx += 2;

            // 电导率（无小数，0~20000μS/cm）
            int ec = ((data[idx] & 0xFF) << 8) | (data[idx + 1] & 0xFF);
            idx += 2;

            // 盐分（mg/L）
            int salinity = ((data[idx] & 0xFF) << 8) | (data[idx + 1] & 0xFF);
            idx += 2;

            // 氮（mg/kg）
            int nitrogen = ((data[idx] & 0xFF) << 8) | (data[idx + 1] & 0xFF);
            idx += 2;

            // 磷（mg/kg）
            int phosphorus = ((data[idx] & 0xFF) << 8) | (data[idx + 1] & 0xFF);
            idx += 2;

            // 钾（mg/kg）
            int potassium = ((data[idx] & 0xFF) << 8) | (data[idx + 1] & 0xFF);
            idx += 2;

            // pH值（精度0.01）
            double ph = (((data[idx] & 0xFF) << 8) | (data[idx + 1] & 0xFF)) / 100.0;

            result.put("soil_temperature", Math.round(soilTemp * 10.0) / 10.0);
            result.put("soil_humidity", Math.round(soilHumidity * 10.0) / 10.0);
            result.put("conductivity", (double) ec);
            result.put("salinity", (double) salinity);
            result.put("nitrogen", (double) nitrogen);
            result.put("phosphorus", (double) phosphorus);
            result.put("potassium", (double) potassium);
            result.put("ph_value", Math.round(ph * 100.0) / 100.0);
        } catch (Exception e) {
            log.error("解析土壤传感器数据出错: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 将16进制表示的字符串（例如 "01 0A"）转换为字节数组（例如 {0x01, 0x0A}）。
     * 会自动忽略空格。
     * @param hex 16进制字符串
     * @return 对应的字节数组
     */
    byte[] hexStringToByteArray(String hex) {
        hex = hex.replaceAll("\\s", "");
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 将字节数组转换为16进制表示的字符串，每个字节后附带一个空格，便于阅读。
     * @param bytes 字节数组
     * @return 格式化的16进制字符串
     */
    String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }

    /**
     * 根据设备类型ID，返回一个更通用的数据分类。
     * 这个分类用于数据处理服务和MQTT主题。
     * @param deviceType 设备类型ID
     * @return 数据分类字符串
     */
    String getDataTypeByDeviceType(String deviceType) {
        switch (deviceType) {
            case "1":
                return "air";
            case "2":
                return "soil";
            default:
                return "unknown";
        }
    }

    /**
     * 公开方法，用于外部触发重新加载传感器配置。
     * 它会先停止所有当前任务，然后重新从数据库加载并启动任务。
     */
    public void reloadSensorConfig() {
        startSensorCommunication();
    }

    /**
     * 获取当前所有传感器任务的运行状态。
     * @return 一个Map，Key是设备ID，Value是任务状态（"RUNNING" 或 "STOPPED"）。
     */
    public Map<String, String> getSensorTaskStatus() {
        return new HashMap<>();
    }
} 