package com.server.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.server.domain.vo.AiMessageVO;
import com.server.properties.DeepSeekProperties;
import com.server.service.DeepSeekService;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DeepSeek API服务实现类
 */
@Service
public class DeepSeekServiceImpl implements DeepSeekService {

    private static final Logger logger = LoggerFactory.getLogger(DeepSeekServiceImpl.class);

    @Autowired
    private DeepSeekProperties deepSeekProperties;

    @Override
    public String chat(AiMessageVO aiMessageVO) {
        if (aiMessageVO == null || aiMessageVO.getPrompt() == null) {
            logger.error("AI消息对象或提示词为空");
            return null;
        }
        return chat(aiMessageVO.getPrompt());
    }

    @Override
    public String chat(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            logger.error("提示词为空");
            return null;
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = null;

        try {
            // 创建HTTP POST请求
            HttpPost httpPost = new HttpPost(deepSeekProperties.getApiUrl());

            // 设置请求头
            httpPost.setHeader("Authorization", "Bearer " + deepSeekProperties.getApiKey());
            httpPost.setHeader("Content-Type", "application/json");

            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", deepSeekProperties.getModel());
            
            // 构建消息列表
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.add(userMessage);
            
            requestBody.put("messages", messages);
            requestBody.put("temperature", deepSeekProperties.getTemperature());
            requestBody.put("stream", false);

            // 设置请求体
            String jsonBody = JSON.toJSONString(requestBody);
            StringEntity entity = new StringEntity(jsonBody, StandardCharsets.UTF_8);
            httpPost.setEntity(entity);

            // 设置超时配置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(deepSeekProperties.getTimeout())
                    .setSocketTimeout(deepSeekProperties.getTimeout())
                    .setConnectionRequestTimeout(deepSeekProperties.getTimeout())
                    .build();
            httpPost.setConfig(requestConfig);

            // 执行请求
            logger.info("调用DeepSeek API，提示词: {}", prompt);
            response = httpClient.execute(httpPost);

            // 解析响应
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String responseString = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                logger.debug("DeepSeek API响应: {}", responseString);

                // 解析JSON响应
                JSONObject jsonResponse = JSON.parseObject(responseString);
                
                // 检查是否有错误
                if (jsonResponse.containsKey("error")) {
                    JSONObject error = jsonResponse.getJSONObject("error");
                    String errorMessage = error.getString("message");
                    logger.error("DeepSeek API返回错误: {}", errorMessage);
                    throw new RuntimeException("DeepSeek API错误: " + errorMessage);
                }

                // 提取响应内容
                if (jsonResponse.containsKey("choices") && jsonResponse.getJSONArray("choices").size() > 0) {
                    JSONObject choice = jsonResponse.getJSONArray("choices").getJSONObject(0);
                    JSONObject message = choice.getJSONObject("message");
                    result = message.getString("content");
                    logger.info("DeepSeek API调用成功，响应长度: {}", result != null ? result.length() : 0);
                } else {
                    logger.error("DeepSeek API响应中没有choices字段");
                }
            }

        } catch (Exception e) {
            logger.error("调用DeepSeek API失败", e);
            throw new RuntimeException("调用DeepSeek API失败: " + e.getMessage(), e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (Exception e) {
                logger.error("关闭HTTP连接失败", e);
            }
        }

        return result;
    }
}

