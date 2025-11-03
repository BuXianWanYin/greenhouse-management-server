package com.server.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * MQTT设备数据DTO
 */
@Data
public class MqttDeviceDataDTO {
    
    private Long id;
    private Long deviceId;
    private String pastureId;
    private String batchId;
    private LocalDateTime collectTime;
    private String remark;
} 