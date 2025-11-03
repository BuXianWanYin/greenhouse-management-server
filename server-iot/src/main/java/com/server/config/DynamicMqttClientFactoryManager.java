package com.server.config;

import com.server.domain.AgricultureDeviceMqttConfig;
import com.server.service.AgricultureDeviceMqttConfigService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态MQTT客户端工厂管理器
 * 根据设备配置表中的mqttBroker动态创建和管理MQTT连接工厂
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "iot.enabled", havingValue = "true")
public class DynamicMqttClientFactoryManager {

    @Autowired
    private AgricultureDeviceMqttConfigService deviceMqttConfigService;

    // 缓存客户端工厂，key为broker地址+用户名+密码的组合，避免重复创建
    // 格式：broker|username|password
    private final Map<String, MqttPahoClientFactory> factoryCache = new ConcurrentHashMap<>();

    // 缓存设备ID到配置的映射
    private final Map<Long, AgricultureDeviceMqttConfig> deviceConfigCache = new ConcurrentHashMap<>();

    /**
     * 根据设备ID获取对应的MQTT客户端工厂
     * 如果设备配置了mqttBroker，则使用配置的broker；否则返回null
     *
     * @param deviceId 设备ID
     * @return MQTT客户端工厂，如果设备未配置或配置无效则返回null
     */
    public MqttPahoClientFactory getClientFactoryByDeviceId(Long deviceId) {
        if (deviceId == null) {
            log.warn("设备ID为空，无法获取MQTT客户端工厂");
            return null;
        }

        try {
            // 从缓存获取配置
            AgricultureDeviceMqttConfig config = deviceConfigCache.get(deviceId);
            if (config == null) {
                // 从数据库查询设备配置
                config = deviceMqttConfigService.getByDeviceId(deviceId);
                if (config == null || config.getMqttBroker() == null || config.getMqttBroker().trim().isEmpty()) {
                    log.warn("设备ID={} 未配置MQTT Broker，无法创建动态连接", deviceId);
                    return null;
                }
                deviceConfigCache.put(deviceId, config);
            }

            // 将config赋值给final变量，以便在lambda中使用
            final AgricultureDeviceMqttConfig finalConfig = config;

            // 生成缓存key（broker+username+password的组合）
            String cacheKey = generateCacheKey(finalConfig);

            // 从缓存获取工厂，如果不存在则创建
            return factoryCache.computeIfAbsent(cacheKey, k -> createClientFactoryWithConfig(finalConfig));

        } catch (Exception e) {
            log.error("获取设备ID={} 的MQTT客户端工厂失败", deviceId, e);
            return null;
        }
    }

    /**
     * 根据设备配置创建MQTT客户端工厂
     * 
     * @param deviceId 设备ID
     * @param config 设备MQTT配置
     * @return MQTT客户端工厂
     */
    public MqttPahoClientFactory getClientFactoryByConfig(Long deviceId, AgricultureDeviceMqttConfig config) {
        if (config == null || config.getMqttBroker() == null || config.getMqttBroker().trim().isEmpty()) {
            log.warn("设备ID={} 的MQTT配置无效，无法创建客户端工厂", deviceId);
            return null;
        }

        // 更新设备配置缓存
        deviceConfigCache.put(deviceId, config);
        
        // 将config赋值给final变量，以便在lambda中使用
        final AgricultureDeviceMqttConfig finalConfig = config;
        
        // 生成缓存key
        String cacheKey = generateCacheKey(finalConfig);
        
        // 创建或获取工厂
        return factoryCache.computeIfAbsent(cacheKey, k -> createClientFactoryWithConfig(finalConfig));
    }

    /**
     * 生成缓存key（broker+username+password的组合）
     */
    private String generateCacheKey(AgricultureDeviceMqttConfig config) {
        String broker = config.getMqttBroker() != null ? config.getMqttBroker().trim() : "";
        String username = config.getMqttUsername() != null ? config.getMqttUsername().trim() : "";
        String password = config.getMqttPassword() != null ? config.getMqttPassword().trim() : "";
        return broker + "|" + username + "|" + password;
    }

    /**
     * 根据设备配置创建MQTT客户端工厂（使用设备配置的认证信息）
     */
    private MqttPahoClientFactory createClientFactoryWithConfig(AgricultureDeviceMqttConfig config) {
        String broker = config.getMqttBroker().trim();
        log.info("创建MQTT客户端工厂: broker={}, username={}", broker, 
                config.getMqttUsername() != null ? config.getMqttUsername() : "未配置");
        
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();

        options.setServerURIs(new String[]{broker});
        
        // 设置认证信息（如果配置了）
        if (config.getMqttUsername() != null && !config.getMqttUsername().trim().isEmpty()) {
            options.setUserName(config.getMqttUsername());
        }
        if (config.getMqttPassword() != null && !config.getMqttPassword().trim().isEmpty()) {
            options.setPassword(config.getMqttPassword().toCharArray());
        }

        options.setCleanSession(true);
        options.setConnectionTimeout(30);
        options.setKeepAliveInterval(60);
        options.setAutomaticReconnect(true);

        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * 清除设备缓存（当设备配置更新时调用）
     *
     * @param deviceId 设备ID
     */
    public void clearDeviceCache(Long deviceId) {
        AgricultureDeviceMqttConfig oldConfig = deviceConfigCache.remove(deviceId);
        if (oldConfig != null) {
            // 清除对应的工厂缓存（如果该配置不再被使用）
            String oldCacheKey = generateCacheKey(oldConfig);
            factoryCache.remove(oldCacheKey);
            log.info("已清除设备ID={} 的MQTT配置缓存和对应的工厂缓存", deviceId);
        } else {
            log.info("已清除设备ID={} 的MQTT配置缓存", deviceId);
        }
    }

    /**
     * 清除所有缓存
     */
    public void clearAllCache() {
        factoryCache.clear();
        deviceConfigCache.clear();
        log.info("已清除所有MQTT配置缓存");
    }
}
