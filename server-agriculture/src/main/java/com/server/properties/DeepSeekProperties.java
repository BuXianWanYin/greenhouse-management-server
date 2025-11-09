package com.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * DeepSeek API配置类
 */
@Component
@ConfigurationProperties(prefix = "deepseek")
@Data
public class DeepSeekProperties {
    /**
     * DeepSeek API基础URL
     * 必须从配置文件 deepseek.apiUrl 读取
     */
    private String apiUrl;
    
    /**
     * DeepSeek API密钥
     */
    private String apiKey;
    
    /**
     * 使用的模型名称
     */
    private String model = "deepseek-chat";
    
    /**
     * 请求超时时间（毫秒）
     */
    private Integer timeout = 60000;
    
    /**
     * 温度参数（控制随机性，0-2之间）
     */
    private Double temperature = 0.7;
}

