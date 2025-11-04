package com.server.service.impl;

import com.server.config.DynamicMqttClientFactoryManager;
import com.server.domain.AgricultureDeviceMqttConfig;
import com.server.service.AgricultureDeviceMqttConfigService;
import com.server.service.DynamicMqttService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 动态MQTT服务
 * 根据设备配置动态创建MQTT连接并发送消息
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "iot.enabled", havingValue = "true")
public class DynamicMqttServiceImpl implements DynamicMqttService {

    @Autowired
    private DynamicMqttClientFactoryManager factoryManager;

    @Autowired
    private AgricultureDeviceMqttConfigService deviceMqttConfigService;

    // 缓存MessageHandler，key为broker地址，避免重复创建
    private final Map<String, MqttPahoMessageHandler> handlerCache = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    public boolean sendMessage(Long deviceId, String payload) {
        if (deviceId == null) {
            log.warn("设备ID为空，无法发送MQTT消息");
            return false;
        }

        try {
            // 获取设备配置
            AgricultureDeviceMqttConfig config = deviceMqttConfigService.getByDeviceId(deviceId);
            if (config == null) {
                log.warn("设备ID={} 未配置MQTT，无法发送消息", deviceId);
                return false;
            }

            // 获取topic
            String topic = config.getMqttTopic();
            if (topic == null || topic.trim().isEmpty()) {
                log.warn("设备ID={} 未配置MQTT Topic，无法发送消息", deviceId);
                return false;
            }

            // 获取QoS
            int qos = config.getMqttQos() != null ? config.getMqttQos().intValue() : 1;

            return sendMessage(deviceId, payload, topic, qos);
        } catch (Exception e) {
            log.error("发送MQTT消息失败: deviceId={}", deviceId, e);
            return false;
        }
    }

    @Override
    public boolean sendMessage(Long deviceId, String payload, String topic) {
        if (deviceId == null) {
            log.warn("设备ID为空，无法发送MQTT消息");
            return false;
        }

        try {
            // 获取设备配置
            AgricultureDeviceMqttConfig config = deviceMqttConfigService.getByDeviceId(deviceId);
            if (config == null) {
                log.warn("设备ID={} 未配置MQTT，无法发送消息", deviceId);
                return false;
            }

            // 获取QoS
            int qos = config.getMqttQos() != null ? config.getMqttQos().intValue() : 1;

            return sendMessage(deviceId, payload, topic != null ? topic : config.getMqttTopic(), qos);
        } catch (Exception e) {
            log.error("发送MQTT消息失败: deviceId={}, topic={}", deviceId, topic, e);
            return false;
        }
    }

    @Override
    public boolean sendMessage(Long deviceId, String payload, String topic, int qos) {
        if (deviceId == null || payload == null || topic == null || topic.trim().isEmpty()) {
            log.warn("参数无效: deviceId={}, topic={}", deviceId, topic);
            return false;
        }

        try {
            // 获取设备配置
            AgricultureDeviceMqttConfig config = deviceMqttConfigService.getByDeviceId(deviceId);
            if (config == null || config.getMqttBroker() == null || config.getMqttBroker().trim().isEmpty()) {
                log.warn("设备ID={} 未配置MQTT Broker，无法发送消息", deviceId);
                return false;
            }

            String broker = config.getMqttBroker().trim();

            // 获取或创建MessageHandler
            MqttPahoMessageHandler messageHandler = getOrCreateMessageHandler(deviceId, config, broker);

            if (messageHandler == null) {
                log.error("无法创建MQTT MessageHandler: deviceId={}, broker={}", deviceId, broker);
                return false;
            }

            // 准备消息头 - 使用Spring Integration MQTT的标准消息头键名
            // 使用MessageBuilder来构建消息，确保消息头正确设置
            org.springframework.messaging.Message<String> message = MessageBuilder
                    .withPayload(payload)
                    .setHeader("mqtt_topic", topic)
                    .setHeader("mqtt_qos", qos)
                    .build();

            // 发送消息
            messageHandler.handleMessage(message);

            log.debug("MQTT消息发送成功: deviceId={}, broker={}, topic={}, qos={}",
                    deviceId, broker, topic, qos);
            return true;

        } catch (Exception e) {
            log.error("发送MQTT消息失败: deviceId={}, topic={}, qos={}", deviceId, topic, qos, e);
            return false;
        }
    }

    /**
     * 获取或创建MessageHandler
     */
    private MqttPahoMessageHandler getOrCreateMessageHandler(Long deviceId,
                                                             AgricultureDeviceMqttConfig config,
                                                             String broker) {
        // 使用broker作为key，但实际每个broker可能共享同一个factory
        String cacheKey = broker;
        return handlerCache.computeIfAbsent(cacheKey, key -> {
            try {
                // 从factoryManager获取客户端工厂（会使用缓存）
                MqttPahoClientFactory factory = factoryManager.getClientFactoryByConfig(deviceId, config);
                if (factory == null) {
                    log.error("无法获取MQTT客户端工厂: deviceId={}, broker={}", deviceId, broker);
                    return null;
                }

                // 创建唯一的客户端ID
                String clientId = "dynamic_mqtt_" + deviceId + "_" + UUID.randomUUID().toString().substring(0, 8);

                MqttPahoMessageHandler handler = new MqttPahoMessageHandler(clientId, factory);
                handler.setAsync(false); // 同步发送
                handler.setCompletionTimeout(5000); // 5秒超时

                // 设置DefaultPahoMessageConverter，用于正确处理消息转换
                // DefaultPahoMessageConverter会将String或byte[]负载转换为MqttMessage
                handler.setConverter(new DefaultPahoMessageConverter());

                log.info("创建动态MQTT MessageHandler: deviceId={}, broker={}, clientId={}",
                        deviceId, broker, clientId);

                return handler;
            } catch (Exception e) {
                log.error("创建MQTT MessageHandler失败: deviceId={}, broker={}", deviceId, broker, e);
                return null;
            }
        });
    }
}
