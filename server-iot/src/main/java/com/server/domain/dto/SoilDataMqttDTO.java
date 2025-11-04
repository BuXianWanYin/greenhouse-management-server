package com.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 土壤传感器数据MQTT传输DTO
 * 包含格式化后的时间
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoilDataMqttDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long deviceId;
    private Long pastureId;
    private Double soilTemperature;
    private Double soilHumidity;
    private Double conductivity;
    private Double salinity;
    private Double nitrogen;
    private Double phosphorus;
    private Double potassium;
    private Double phValue;
    private String collectTime; // 格式化后的时间：yyyy-MM-dd HH:mm:ss
    private Long onlineStatus; // 在线状态（1=在线，0=离线）
    private String lastOnlineTime; // 最后在线时间：yyyy-MM-dd HH:mm:ss
}
