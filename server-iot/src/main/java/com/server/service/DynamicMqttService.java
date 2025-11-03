package com.server.service;

/**
 * 动态MQTT服务接口
 * 根据设备配置动态发送MQTT消息
 */
public interface DynamicMqttService {
    
    /**
     * 根据设备ID发送MQTT消息
     * 使用设备配置表中的mqttBroker、mqttTopic等配置
     *
     * @param deviceId 设备ID
     * @param payload 消息内容
     * @return 是否发送成功
     */
    boolean sendMessage(Long deviceId, String payload);
    
    /**
     * 根据设备ID和指定的topic发送MQTT消息
     *
     * @param deviceId 设备ID
     * @param payload 消息内容
     * @param topic MQTT主题（如果为null，则使用设备配置中的topic）
     * @return 是否发送成功
     */
    boolean sendMessage(Long deviceId, String payload, String topic);
    
    /**
     * 根据设备ID发送MQTT消息，指定QoS
     *
     * @param deviceId 设备ID
     * @param payload 消息内容
     * @param topic MQTT主题
     * @param qos QoS等级
     * @return 是否发送成功
     */
    boolean sendMessage(Long deviceId, String payload, String topic, int qos);
}
