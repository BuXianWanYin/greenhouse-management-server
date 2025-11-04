package com.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 空气传感器数据MQTT传输DTO
 * 包含格式化后的时间
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirDataMqttDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long deviceId;
    private Long pastureId;
    private Double temperature;
    private Double humidity;
    private Double illuminance;
    private String collectTime; // 格式化后的时间：yyyy-MM-dd HH:mm:ss
}
