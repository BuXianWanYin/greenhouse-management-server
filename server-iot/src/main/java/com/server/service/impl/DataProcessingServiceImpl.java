package com.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.domain.AgricultureAirData;
import com.server.domain.AgricultureDeviceMqttConfig;
import com.server.domain.AgricultureSoilData;
import com.server.domain.dto.AirDataMqttDTO;
import com.server.domain.dto.SoilDataMqttDTO;
import com.server.service.AgricultureAirDataService;
import com.server.service.AgricultureDeviceMqttConfigService;
import com.server.service.AgricultureSoilDataService;
import com.server.service.DataProcessingService;
import com.server.service.DynamicMqttService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/**
 * 数据处理服务实现
 * 负责对从传感器接收并解析后的数据进行后续处理。
 */
@Service
@ConditionalOnProperty(name = "iot.enabled", havingValue = "true")
public class DataProcessingServiceImpl implements DataProcessingService {

    private static final Logger log = LoggerFactory.getLogger(DataProcessingServiceImpl.class);

    @Autowired
    private AgricultureAirDataService airDataService; // 空气传感器数据

    @Autowired
    private AgricultureSoilDataService soilDataService; // 土壤传感器数据

    @Autowired
    private AgricultureDeviceMqttConfigService deviceMqttConfigService; //Mqtt配置

    @Autowired
    private DynamicMqttService dynamicMqttService; // 动态MQTT服务

    @Autowired
    private ObjectMapper objectMapper; // Spring Boot自动配置的JSON处理工具

    // 时间格式化器：yyyy-MM-dd HH:mm:ss
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void processAndStore(Map<String, Object> parsedData) {
        try {
            // 构建上下文
            DataProcessingContext context = buildContext(parsedData);
            if (context.deviceId() == null) {
                log.warn("解析数据中缺少设备ID，跳过处理");
                return;
            }

            // 根据数据类型处理
            String type = (String) parsedData.get("type");
            switch (type) {
                case "air" -> processAirData(context);
                case "soil" -> processSoilData(context);
                default -> sendRawDataToMqtt(context, parsedData, type);
            }
        } catch (Exception e) {
            log.error("处理并存储已解析的数据失败", e);
        }
    }

    /**
     * 构建数据处理上下文
     */
    private DataProcessingContext buildContext(Map<String, Object> parsedData) {
        Long deviceId = parsedData.get("deviceId") != null 
            ? Long.valueOf(parsedData.get("deviceId").toString()) 
            : null;

        AgricultureDeviceMqttConfig config = null;
        String topic = null;
        if (deviceId != null) {
            config = deviceMqttConfigService.getByDeviceId(deviceId);
            if (config != null && config.getMqttTopic() != null && !config.getMqttTopic().isEmpty()) {
                topic = config.getMqttTopic();
            }
        }

        return new DataProcessingContext(parsedData, deviceId, config, topic);
    }

    /**
     * 处理空气传感器数据
     */
    private void processAirData(DataProcessingContext context) {
        AgricultureAirData airData = createAirData(context.parsedData());
        saveAirData(airData, context.deviceId());
        sendAirDataToMqtt(airData, context);
    }

    /**
     * 处理土壤传感器数据
     */
    private void processSoilData(DataProcessingContext context) {
        AgricultureSoilData soilData = createSoilData(context.parsedData());
        saveSoilData(soilData, context.deviceId());
        sendSoilDataToMqtt(soilData, context);
    }

    /**
     * 保存空气传感器数据
     */
    private void saveAirData(AgricultureAirData airData, Long deviceId) {
        try {
            airDataService.save(airData);
            log.info("空气传感器数据保存成功: 设备ID={}, 温度={}, 湿度={}, 光照={}", 
                airData.getDeviceId(), airData.getTemperature(), airData.getHumidity(), airData.getIlluminance());
        } catch (Exception e) {
            log.error("保存空气传感器数据失败: 设备ID={}", deviceId, e);
            throw e;
        }
    }

    /**
     * 保存土壤传感器数据
     */
    private void saveSoilData(AgricultureSoilData soilData, Long deviceId) {
        try {
            soilDataService.save(soilData);
            log.info("土壤传感器数据保存成功: 设备ID={}, 温度={}, 湿度={}, pH={}", 
                soilData.getDeviceId(), soilData.getSoilTemperature(), soilData.getSoilHumidity(), soilData.getPhValue());
        } catch (Exception e) {
            log.error("保存土壤传感器数据失败: 设备ID={}", deviceId, e);
            throw e;
        }
    }

    /**
     * 发送空气传感器数据到MQTT
     */
    private void sendAirDataToMqtt(AgricultureAirData airData, DataProcessingContext context) {
        try {
            if (isMqttConfigValid(context)) {
                AirDataMqttDTO mqttDTO = convertToAirDataMqttDTO(airData);
                sendMqttMessage(context, objectMapper.writeValueAsString(mqttDTO), "空气传感器数据");
            } else {
                log.warn("设备ID={} 未配置MQTT Broker，无法发送MQTT消息", context.deviceId());
            }
        } catch (Exception e) {
            log.error("MQTT消息发送失败: 设备ID={}, topic={}", context.deviceId(), context.topic(), e);
        }
    }

    /**
     * 发送土壤传感器数据到MQTT
     */
    private void sendSoilDataToMqtt(AgricultureSoilData soilData, DataProcessingContext context) {
        try {
            if (isMqttConfigValid(context)) {
                SoilDataMqttDTO mqttDTO = convertToSoilDataMqttDTO(soilData);
                sendMqttMessage(context, objectMapper.writeValueAsString(mqttDTO), "土壤传感器数据");
            } else {
                log.warn("设备ID={} 未配置MQTT Broker，无法发送MQTT消息", context.deviceId());
            }
        } catch (Exception e) {
            log.error("MQTT消息发送失败: 设备ID={}, topic={}", context.deviceId(), context.topic(), e);
        }
    }

    /**
     * 发送原始数据到MQTT（未知类型）
     */
    private void sendRawDataToMqtt(DataProcessingContext context, Map<String, Object> parsedData, String type) {
        try {
            if (isMqttConfigValid(context)) {
                sendMqttMessage(context, objectMapper.writeValueAsString(parsedData), "未知数据类型: " + type);
                log.warn("收到未知数据类型: {}，原始数据已推送到MQTT主题 {}", type, context.topic());
            } else {
                log.warn("收到未知数据类型: {}，设备ID={} 未配置MQTT Broker，无法发送MQTT消息", type, context.deviceId());
            }
        } catch (Exception e) {
            log.error("MQTT消息发送失败: 设备ID={}, 主题={}, 数据类型={}", context.deviceId(), context.topic(), type, e);
        }
    }

    /**
     * 检查MQTT配置是否有效
     */
    private boolean isMqttConfigValid(DataProcessingContext context) {
        return context.config() != null 
            && context.config().getMqttBroker() != null 
            && !context.config().getMqttBroker().trim().isEmpty();
    }

    /**
     * 发送MQTT消息（公共方法）
     */
    private void sendMqttMessage(DataProcessingContext context, String payload, String dataType) {
        try {
            boolean sent = dynamicMqttService.sendMessage(context.deviceId(), payload);
            if (sent) {
                log.debug("MQTT消息发送成功: {} - 设备ID={}, broker={}, topic={}", 
                    dataType, context.deviceId(), context.config().getMqttBroker(), context.topic());
            } else {
                log.warn("MQTT消息发送失败: {} - 设备ID={}, broker={}, topic={}", 
                    dataType, context.deviceId(), context.config().getMqttBroker(), context.topic());
            }
        } catch (Exception e) {
            log.error("发送MQTT消息异常: {} - 设备ID={}, topic={}", dataType, context.deviceId(), context.topic(), e);
        }
    }

    /**
     * 数据处理上下文（使用 Record，JDK 14+）
     */
    private record DataProcessingContext(
        Map<String, Object> parsedData,
        Long deviceId,
        AgricultureDeviceMqttConfig config,
        String topic
    ) {}

    /**
     * 根据传入的Map数据，创建一个空气传感器数据实体对象。
     * @param data 包含传感器数据的Map
     * @return 构造好的AgricultureAirData实体
     */
    private AgricultureAirData createAirData(Map<String, Object> data) {
        AgricultureAirData airData = new AgricultureAirData();
        airData.setDeviceId(Long.valueOf(Objects.toString(data.get("deviceId"))));
        // 设置温室ID
        if (data.get("pastureId") != null) {
            airData.setPastureId(getLongValue(data.get("pastureId"), null));
        }
        airData.setTemperature(getDoubleValue(data.get("temperature"), null));
        airData.setHumidity(getDoubleValue(data.get("humidity"), null));
        airData.setIlluminance(getDoubleValue(data.get("illuminance"), null));
        airData.setCollectTime(LocalDateTime.now());
        return airData;
    }

    /**
     * 根据传入的Map数据，创建一个土壤传感器数据实体对象。
     * @param data 包含传感器数据的Map
     * @return 构造好的AgricultureSoilData实体
     */
    private AgricultureSoilData createSoilData(Map<String, Object> data) {
        AgricultureSoilData soilData = new AgricultureSoilData();
        soilData.setDeviceId(Long.valueOf(Objects.toString(data.get("deviceId"))));
        // 设置温室ID
        if (data.get("pastureId") != null) {
            soilData.setPastureId(getLongValue(data.get("pastureId"), null));
        }
        soilData.setSoilTemperature(getDoubleValue(data.get("soil_temperature"), null));
        soilData.setSoilHumidity(getDoubleValue(data.get("soil_humidity"), null));
        soilData.setConductivity(getDoubleValue(data.get("conductivity"), null));
        soilData.setSalinity(getDoubleValue(data.get("salinity"), null));
        soilData.setNitrogen(getDoubleValue(data.get("nitrogen"), null));
        soilData.setPhosphorus(getDoubleValue(data.get("phosphorus"), null));
        soilData.setPotassium(getDoubleValue(data.get("potassium"), null));
        soilData.setPhValue(getDoubleValue(data.get("ph_value"), null));
        soilData.setCollectTime(LocalDateTime.now());
        return soilData;
    }

    /**
     * 从Object对象获取Double值。
     * 处理 Number、String 或 null 的情况
     */
    private Double getDoubleValue(Object value, Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            log.warn("无法转换值 '{}' 为Double类型，使用默认值 '{}'", value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 从Object对象获取Long值。
     * 处理 Number、String 或 null 的情况
     */
    private Long getLongValue(Object value, Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            log.warn("无法转换值 '{}' 为Long类型，使用默认值 '{}'", value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 将空气传感器数据转换为MQTT DTO
     * 包含格式化后的时间
     */
    private AirDataMqttDTO convertToAirDataMqttDTO(AgricultureAirData airData) {
        AirDataMqttDTO dto = new AirDataMqttDTO();
        dto.setId(airData.getId());
        dto.setDeviceId(airData.getDeviceId());
        dto.setPastureId(airData.getPastureId());
        dto.setTemperature(airData.getTemperature());
        dto.setHumidity(airData.getHumidity());
        dto.setIlluminance(airData.getIlluminance());
        
        // 格式化时间
        if (airData.getCollectTime() != null) {
            dto.setCollectTime(airData.getCollectTime().format(TIME_FORMATTER));
        }
        
        return dto;
    }

    /**
     * 将土壤传感器数据转换为MQTT DTO
     * 包含格式化后的时间
     */
    private SoilDataMqttDTO convertToSoilDataMqttDTO(AgricultureSoilData soilData) {
        SoilDataMqttDTO dto = new SoilDataMqttDTO();
        dto.setId(soilData.getId());
        dto.setDeviceId(soilData.getDeviceId());
        dto.setPastureId(soilData.getPastureId());
        dto.setSoilTemperature(soilData.getSoilTemperature());
        dto.setSoilHumidity(soilData.getSoilHumidity());
        dto.setConductivity(soilData.getConductivity());
        dto.setSalinity(soilData.getSalinity());
        dto.setNitrogen(soilData.getNitrogen());
        dto.setPhosphorus(soilData.getPhosphorus());
        dto.setPotassium(soilData.getPotassium());
        dto.setPhValue(soilData.getPhValue());
        
        // 格式化时间
        if (soilData.getCollectTime() != null) {
            dto.setCollectTime(soilData.getCollectTime().format(TIME_FORMATTER));
        }
        
        return dto;
    }
}