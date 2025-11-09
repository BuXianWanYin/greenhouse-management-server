package com.server.consumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.server.domain.AgricultureJob;
import com.server.domain.vo.AiMessageVO;
import com.server.mapper.AgricultureJobMapper;
import com.server.service.DeepSeekService;
import com.server.ws.MqSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * AI消息消费者
 * 处理来自RabbitMQ的AI消息，调用DeepSeek API并保存结果
 */
@Slf4j
@Component
public class AiMessageConsumer {

    @Autowired
    private DeepSeekService deepSeekService;

    @Autowired
    private AgricultureJobMapper agricultureJobMapper;

    @Autowired
    private MqSocketServer mqSocketServer;

    /**
     * 消费AI作业消息
     *
     * @param aiMessageVO AI消息对象
     */
    @RabbitListener(queues = "ai_job_queue")
    public void handleAiJobMessage(AiMessageVO aiMessageVO) {
        log.info("收到AI作业消息: {}", aiMessageVO);
        
        try {
            // 调用DeepSeek API
            String response = deepSeekService.chat(aiMessageVO);
            
            if (response == null || response.trim().isEmpty()) {
                log.error("DeepSeek API返回空响应");
                sendErrorToClient(aiMessageVO.getId(), "AI服务返回空响应");
                return;
            }

            // 解析AI响应并保存到数据库
            List<AgricultureJob> jobList = parseAiResponse(aiMessageVO.getId(), response);
            
            if (jobList != null && !jobList.isEmpty()) {
                // 批量保存作业数据
                for (AgricultureJob job : jobList) {
                    agricultureJobMapper.insert(job);
                }
                log.info("成功保存{}条作业数据", jobList.size());
                
                // 通过WebSocket通知客户端
                sendSuccessToClient(aiMessageVO.getId(), jobList.size());
            } else {
                log.warn("解析AI响应后未得到有效数据");
                sendErrorToClient(aiMessageVO.getId(), "解析AI响应失败");
            }

        } catch (Exception e) {
            log.error("处理AI作业消息失败", e);
            sendErrorToClient(aiMessageVO.getId(), "处理失败: " + e.getMessage());
        }
    }

    /**
     * 解析AI响应为作业数据列表
     *
     * @param classId 种类ID
     * @param aiResponse AI响应内容
     * @return 作业数据列表
     */
    private List<AgricultureJob> parseAiResponse(Long classId, String aiResponse) {
        List<AgricultureJob> jobList = new ArrayList<>();
        
        try {
            // 尝试解析JSON格式的响应
            JSONArray jsonArray = JSON.parseArray(aiResponse);
            
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jobJson = jsonArray.getJSONObject(i);
                AgricultureJob job = AgricultureJob.builder()
                        .classId(classId)
                        .jobName(jobJson.getString("jobName"))
                        .cycleUnit(jobJson.getString("cycleUnit") != null ? jobJson.getString("cycleUnit") : "0")
                        .jobStart(jobJson.getLong("jobStart"))
                        .jobFinish(jobJson.getLong("jobFinish"))
                        .status("0")
                        .build();
                jobList.add(job);
            }
        } catch (Exception e) {
            log.warn("AI响应不是标准JSON格式，尝试其他解析方式: {}", e.getMessage());
            
            // 如果JSON解析失败，可以尝试其他解析方式
            // 这里可以根据实际AI返回的格式进行调整
            // 例如：如果AI返回的是文本格式，可以尝试正则表达式提取
        }
        
        return jobList;
    }

    /**
     * 发送成功消息到客户端
     */
    private void sendSuccessToClient(Long id, int count) {
        try {
            JSONObject message = new JSONObject();
            message.put("id", id);
            message.put("status", "success");
            message.put("message", "成功生成" + count + "条作业数据");
            mqSocketServer.sendToAllClient(message.toJSONString());
        } catch (Exception e) {
            log.error("发送WebSocket消息失败", e);
        }
    }

    /**
     * 发送错误消息到客户端
     */
    private void sendErrorToClient(Long id, String errorMessage) {
        try {
            JSONObject message = new JSONObject();
            message.put("id", id);
            message.put("status", "error");
            message.put("message", errorMessage);
            mqSocketServer.sendToAllClient(message.toJSONString());
        } catch (Exception e) {
            log.error("发送WebSocket错误消息失败", e);
        }
    }
}

