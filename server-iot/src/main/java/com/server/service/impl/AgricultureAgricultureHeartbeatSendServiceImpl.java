package com.server.service.impl;

import com.server.domain.AgricultureDeviceHeartbeat;
import com.server.service.AgricultureDeviceHeartbeatService;
import com.server.service.AgricultureHeartbeatSendService;
import com.server.service.SerialPortService;
import com.server.util.ModbusCommandParser;
import com.server.util.SerialCommandExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 心跳发送服务实现类
 * 负责发送心跳指令并校验设备回复
 * 仅在 iot.enabled=true 时加载
 * 
 * @author server
 * @date 2025-01-XX
 */
@Service
@ConditionalOnProperty(name = "iot.enabled", havingValue = "true")
public class AgricultureAgricultureHeartbeatSendServiceImpl implements AgricultureHeartbeatSendService {
    
    private static final Logger log = LoggerFactory.getLogger(AgricultureAgricultureHeartbeatSendServiceImpl.class);
    
    @Autowired
    private SerialPortService serialPortService;
    
    @Autowired
    private AgricultureDeviceHeartbeatService agricultureDeviceHeartbeatService;
    
    @Autowired
    private SerialCommandExecutor serialCommandExecutor;
    
    /**
     * 发送心跳指令并校验回复
     * 通过 SerialCommandExecutor 队列发送，确保与其他串口操作串行化执行
     * 
     * @param heartbeat 心跳记录对象
     * @return 是否成功（成功收到有效回复）
     */
    @Override
    public boolean sendHeartbeatAndValidate(AgricultureDeviceHeartbeat heartbeat) {
        if (heartbeat == null) {
            log.warn("心跳记录对象为空，跳过发送");
            return false;
        }
        
        String heartbeatCmdHex = heartbeat.getHeartbeatCmdHex();
        if (heartbeatCmdHex == null || heartbeatCmdHex.trim().isEmpty()) {
            log.warn("心跳指令为空，跳过发送。设备ID: {}", heartbeat.getDeviceId());
            return false;
        }
        
        // 检查串口是否连接
        if (!serialPortService.isConnected()) {
            log.warn("串口未连接，无法发送心跳指令。设备ID: {}", heartbeat.getDeviceId());
            return false;
        }
        
        try {
            // 通过 SerialCommandExecutor 队列提交心跳任务
            Future<Boolean> future = serialCommandExecutor.submit(() -> {
                synchronized (serialPortService.getSerialLock()) {
                    try {
                        // 发送心跳指令
                        log.info("发送心跳指令到设备 ID: {}, 指令: {}", heartbeat.getDeviceId(), heartbeatCmdHex);
                        byte[] commandBytes = serialPortService.hexStringToByteArray(heartbeatCmdHex);
                        int bytesSent = serialPortService.writeToSerial(commandBytes);
                        
                        if (bytesSent <= 0) {
                            log.error("心跳指令发送失败。设备ID: {}", heartbeat.getDeviceId());
                            agricultureDeviceHeartbeatService.updateOnlineStatusByDeviceId(heartbeat.getDeviceId(), 0L);
                            return false;
                        }
                        
                        // 更新发送时间
                        heartbeat.setLastSendTime(LocalDateTime.now());
                        agricultureDeviceHeartbeatService.updateById(heartbeat);
                        
                        // 等待设备回复（根据Modbus协议，通常需要200-500ms）
                        Thread.sleep(300);
                        
                        // 读取设备回复
                        byte[] responseBytes = serialPortService.readFromSerial(256);
                        
                        if (responseBytes == null || responseBytes.length == 0) {
                            log.warn("未收到设备回复。设备ID: {}", heartbeat.getDeviceId());
                            agricultureDeviceHeartbeatService.updateOnlineStatusByDeviceId(heartbeat.getDeviceId(), 0L);
                            return false;
                        }
                        
                        // 将回复转换为十六进制字符串
                        String responseHex = bytesToHexString(responseBytes);
                        log.info("收到设备回复。设备ID: {}, 回复: {}", heartbeat.getDeviceId(), responseHex);
                        
                        // 从指令中提取设备地址
                        String deviceAddr = ModbusCommandParser.extractDeviceAddr(heartbeatCmdHex);
                        if (deviceAddr == null) {
                            log.error("无法从指令中提取设备地址。设备ID: {}", heartbeat.getDeviceId());
                            agricultureDeviceHeartbeatService.updateOnlineStatusByDeviceId(heartbeat.getDeviceId(), 0L);
                            return false;
                        }
                        
                        // 校验回复指令
                        int functionCode = heartbeat.getCmdFunctionCode() != null ? heartbeat.getCmdFunctionCode().intValue() : 3;
                        Map<String, Object> validationResult = ModbusCommandParser.validateModbusResponse(
                                responseHex, deviceAddr, functionCode);
                        
                        boolean isValid = (Boolean) validationResult.get("isValid");
                        String message = (String) validationResult.get("message");
                        
                        if (isValid) {
                            log.info("心跳回复校验通过。设备ID: {}", heartbeat.getDeviceId());
                            agricultureDeviceHeartbeatService.updateOnlineStatusByDeviceId(heartbeat.getDeviceId(), 1L);
                            return true;
                        } else {
                            log.warn("心跳回复校验失败。设备ID: {}, 原因: {}", heartbeat.getDeviceId(), message);
                            agricultureDeviceHeartbeatService.updateOnlineStatusByDeviceId(heartbeat.getDeviceId(), 0L);
                            return false;
                        }
                        
                    } catch (InterruptedException e) {
                        log.error("发送心跳指令时线程被中断。设备ID: {}", heartbeat.getDeviceId(), e);
                        Thread.currentThread().interrupt();
                        agricultureDeviceHeartbeatService.updateOnlineStatusByDeviceId(heartbeat.getDeviceId(), 0L);
                        return false;
                    } catch (Exception e) {
                        log.error("发送心跳指令时发生异常。设备ID: {}", heartbeat.getDeviceId(), e);
                        agricultureDeviceHeartbeatService.updateOnlineStatusByDeviceId(heartbeat.getDeviceId(), 0L);
                        return false;
                    }
                }
            });
            
            // 等待任务完成并返回结果
            return future.get();
            
        } catch (Exception e) {
            log.error("提交心跳任务到队列时发生异常。设备ID: {}", heartbeat.getDeviceId(), e);
            agricultureDeviceHeartbeatService.updateOnlineStatusByDeviceId(heartbeat.getDeviceId(), 0L);
            return false;
        }
    }
    
    /**
     * 批量发送心跳指令
     * 
     * @param heartbeatList 心跳记录列表
     * @return 成功发送的数量
     */
    @Override
    public int sendBatchHeartbeat(List<AgricultureDeviceHeartbeat> heartbeatList) {
        if (heartbeatList == null || heartbeatList.isEmpty()) {
            return 0;
        }
        
        int successCount = 0;
        for (AgricultureDeviceHeartbeat heartbeat : heartbeatList) {
            if (sendHeartbeatAndValidate(heartbeat)) {
                successCount++;
            }
            
            // 使用每个心跳记录的发送间隔，如果没有设置则使用默认值5000毫秒
            Long sendInterval = heartbeat.getSendInterval();
            if (sendInterval == null || sendInterval <= 0) {
                sendInterval = 5000L; // 默认5秒
            }
            
            // 每次发送之间间隔一段时间，避免串口操作过于频繁
            try {
                Thread.sleep(sendInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("批量发送心跳时线程被中断");
                break;
            }
        }
        
        log.info("批量发送心跳完成，总数: {}, 成功: {}", heartbeatList.size(), successCount);
        return successCount;
    }
    
    /**
     * 发送单个设备的心跳指令
     * 
     * @param deviceId 设备ID
     * @return 是否成功
     */
    @Override
    public boolean sendDeviceHeartbeat(Long deviceId) {
        if (deviceId == null) {
            log.warn("设备ID为空，跳过发送");
            return false;
        }
        
        // 根据设备ID查询心跳记录
        AgricultureDeviceHeartbeat heartbeat = agricultureDeviceHeartbeatService
                .lambdaQuery()
                .eq(AgricultureDeviceHeartbeat::getDeviceId, deviceId)
                .one();
        
        if (heartbeat == null) {
            log.warn("未找到设备的心跳记录。设备ID: {}", deviceId);
            return false;
        }
        
        return sendHeartbeatAndValidate(heartbeat);
    }
    
    /**
     * 将字节数组转换为十六进制字符串（空格分隔）
     * 
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private String bytesToHexString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(String.format("%02X", bytes[i] & 0xFF));
        }
        
        return sb.toString();
    }
}

