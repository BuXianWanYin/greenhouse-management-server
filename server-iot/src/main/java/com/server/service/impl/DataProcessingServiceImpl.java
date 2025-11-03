package com.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.domain.AgricultureAirData;
import com.server.domain.AgricultureDeviceMqttConfig;
import com.server.domain.AgricultureSoilData;
import com.server.gateway.MqttGateway;
import com.server.service.AgricultureAirDataService;
import com.server.service.AgricultureDeviceMqttConfigService;
import com.server.service.AgricultureSoilDataService;
import com.server.service.DataProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private MqttGateway mqttGateway; // MQTT消息推送网关

    @Autowired
    private ObjectMapper objectMapper; // Spring Boot自动配置的JSON处理工具

    @Override
    public void processAndStore(Map<String, Object> parsedData) {
        try {
            // 1. 获取设备ID
            Long deviceId = null;
            if (parsedData.get("deviceId") != null) {
                deviceId = Long.valueOf(parsedData.get("deviceId").toString());
            }

            // 2. 查找该设备的MQTT配置，获取专属topic
            String topic = null;
            if (deviceId != null) {
                AgricultureDeviceMqttConfig config = deviceMqttConfigService.getByDeviceId(deviceId);
                if (config != null && config.getMqttTopic() != null && !config.getMqttTopic().isEmpty()) {
                    topic = config.getMqttTopic();
                }
            }
            // 兜底：如果没有查到topic，推送到一个默认主题
            if (topic == null) {
                topic = "/fish-dish/unknown";
                log.warn("设备ID {} 未配置MQTT主题，推送到默认主题 {}", deviceId, topic);
            }

            // 3. 数据类型（决定存储到哪张表，但推送MQTT只看设备ID）
            String type = (String) parsedData.get("type");

            if ("air".equals(type)) {
                // 空气传感器数据，存表
                AgricultureAirData airData = createAirData(parsedData);
                try {
                    airDataService.save(airData);
                    log.info("空气传感器数据保存成功: 设备ID={}, 温度={}, 湿度={}, 光照={}", 
                        airData.getDeviceId(), airData.getTemperature(), airData.getHumidity(), airData.getIlluminance());
                } catch (Exception e) {
                    log.error("保存空气传感器数据失败: 设备ID={}", deviceId, e);
                    throw e; // 数据保存失败应该抛出异常
                }

                // 推送到设备专属topic（失败不影响数据保存）
                try {
                    mqttGateway.sendToMqtt(objectMapper.writeValueAsString(airData), topic);
                    log.debug("MQTT消息发送成功: 设备ID={}, 主题={}", deviceId, topic);
                } catch (Exception e) {
                    log.error("MQTT消息发送失败: 设备ID={}, 主题={}", deviceId, topic, e);
                    // MQTT发送失败不影响数据保存，只记录错误
                }

            } else if ("soil".equals(type)) {
                // 土壤传感器数据，存表
                AgricultureSoilData soilData = createSoilData(parsedData);
                try {
                    soilDataService.save(soilData);
                    log.info("土壤传感器数据保存成功: 设备ID={}, 温度={}, 湿度={}, pH={}", 
                        soilData.getDeviceId(), soilData.getSoilTemperature(), soilData.getSoilHumidity(), soilData.getPhValue());
                } catch (Exception e) {
                    log.error("保存土壤传感器数据失败: 设备ID={}", deviceId, e);
                    throw e; // 数据保存失败应该抛出异常
                }

                // 推送到设备专属topic（失败不影响数据保存）
                try {
                    mqttGateway.sendToMqtt(objectMapper.writeValueAsString(soilData), topic);
                    log.debug("MQTT消息发送成功: 设备ID={}, 主题={}", deviceId, topic);
                } catch (Exception e) {
                    log.error("MQTT消息发送失败: 设备ID={}, 主题={}", deviceId, topic, e);
                    // MQTT发送失败不影响数据保存，只记录错误
                }
            } else {
                // 其它类型，直接推送原始数据
                try {
                    mqttGateway.sendToMqtt(objectMapper.writeValueAsString(parsedData), topic);
                    log.warn("收到未知数据类型: {}，原始数据已推送到MQTT主题 {}", type, topic);
                } catch (Exception e) {
                    log.error("MQTT消息发送失败: 设备ID={}, 主题={}, 数据类型={}", deviceId, topic, type, e);
                }
            }
        } catch (Exception e) {
            log.error("处理并存储已解析的数据失败", e);
        }
    }

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
     * @param value 待转换的对象
     * @param defaultValue 如果转换失败或对象为null时返回的默认值
     * @return 转换后的Double值或默认值
     */
    private Double getDoubleValue(Object value, Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            } else {
                String strValue = Objects.toString(value);
                if ("null".equals(strValue) || strValue.trim().isEmpty()) {
                    return defaultValue;
                }
                return new java.math.BigDecimal(strValue).doubleValue();
            }
        } catch (Exception e) {
            log.warn("无法转换值 '{}' 为Double类型，使用默认值 '{}'", value, defaultValue);
            return defaultValue;
        }
    }

    /**
     * 从Object对象获取Long值。
     * @param value 待转换的对象
     * @param defaultValue 如果转换失败或对象为null时返回的默认值
     * @return 转换后的Long值或默认值
     */
    private Long getLongValue(Object value, Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            } else {
                String strValue = Objects.toString(value);
                if ("null".equals(strValue) || strValue.trim().isEmpty()) {
                    return defaultValue;
                }
                return Long.parseLong(strValue);
            }
        } catch (Exception e) {
            log.warn("无法转换值 '{}' 为Long类型，使用默认值 '{}'", value, defaultValue);
            return defaultValue;
        }
    }
}