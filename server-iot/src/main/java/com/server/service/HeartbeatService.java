package com.server.service;

import com.server.domain.AgricultureDeviceHeartbeat;

import java.util.List;

/**
 * 心跳服务接口
 * 负责发送心跳指令并校验设备回复
 * 
 * @author server
 * @date 2025-01-XX
 */
public interface HeartbeatService {
    
    /**
     * 发送心跳指令并校验回复
     * 
     * @param heartbeat 心跳记录对象
     * @return 是否成功（成功收到有效回复）
     */
    boolean sendHeartbeatAndValidate(AgricultureDeviceHeartbeat heartbeat);
    
    /**
     * 批量发送心跳指令
     * 
     * @param heartbeatList 心跳记录列表
     * @return 成功发送的数量
     */
    int sendBatchHeartbeat(List<AgricultureDeviceHeartbeat> heartbeatList);
    
    /**
     * 发送单个设备的心跳指令
     * 
     * @param deviceId 设备ID
     * @return 是否成功
     */
    boolean sendDeviceHeartbeat(Long deviceId);
}

