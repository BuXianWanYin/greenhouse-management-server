package com.server.task;

import com.server.domain.AgricultureDeviceHeartbeat;
import com.server.service.AgricultureDeviceHeartbeatService;
import com.server.service.HeartbeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 心跳指令定时发送任务
 * 使用定时任务 + 基于时间的判断，统一管理所有设备的心跳发送
 * 
 * 优点：
 * 1. 统一管理，资源消耗小（只需要一个定时任务线程）
 * 2. 灵活配置，每个设备可以有不同的发送间隔（sendInterval）
 * 3. 精确控制，基于lastSendTime判断，精确控制发送间隔
 * 4. 避免冲突，通过队列串行化，确保不会并发冲突
 * 
 * @author server
 * @date 2025-01-XX
 */
@Component
@ConditionalOnProperty(name = "iot.enabled", havingValue = "true")
public class HeartbeatScheduledTask {
    
    private static final Logger log = LoggerFactory.getLogger(HeartbeatScheduledTask.class);
    
    @Autowired
    private AgricultureDeviceHeartbeatService agricultureDeviceHeartbeatService;
    
    @Autowired
    private HeartbeatService heartbeatService;
    
    /**
     * 定时检查并发送心跳指令
     * 每10秒执行一次，检查所有设备是否需要发送心跳
     * 
     * 执行逻辑：
     * 1. 查询所有需要发送心跳的设备
     * 2. 检查每个设备的 lastSendTime + sendInterval 是否已到
     * 3. 如果已到，则提交到队列发送
     * 
     * 注意：定时任务的执行周期应该小于最小发送间隔（建议为最小间隔的1/5到1/10）
     * 例如：如果最小发送间隔是5秒，则定时任务建议每1-2秒执行一次
     */
    @Scheduled(fixedDelay = 10000) // 每10秒检查一次，可以根据实际情况调整
    public void checkAndSendHeartbeat() {
        try {
            // 查询所有需要发送心跳的设备
            List<AgricultureDeviceHeartbeat> heartbeats = agricultureDeviceHeartbeatService.list();
            
            if (heartbeats == null || heartbeats.isEmpty()) {
                return;
            }
            
            LocalDateTime now = LocalDateTime.now();
            int needSendCount = 0;
            int sentCount = 0;
            
            for (AgricultureDeviceHeartbeat heartbeat : heartbeats) {
                // 检查心跳指令是否配置
                if (heartbeat.getHeartbeatCmdHex() == null || heartbeat.getHeartbeatCmdHex().trim().isEmpty()) {
                    continue;
                }
                
                // 获取发送间隔（毫秒），如果没有设置则使用默认值5000毫秒（5秒）
                Long sendInterval = heartbeat.getSendInterval();
                if (sendInterval == null || sendInterval <= 0) {
                    sendInterval = 5000L; // 默认5秒
                }
                
                // 计算下次发送时间
                LocalDateTime nextSendTime;
                if (heartbeat.getLastSendTime() != null) {
                    // 上次发送时间 + 发送间隔 = 下次发送时间
                    nextSendTime = heartbeat.getLastSendTime().plusSeconds(sendInterval / 1000);
                } else {
                    // 如果从未发送过，立即发送
                    nextSendTime = now;
                }
                
                // 检查是否需要发送（当前时间 >= 下次发送时间）
                if (now.isAfter(nextSendTime) || now.isEqual(nextSendTime)) {
                    needSendCount++;
                    // 异步提交到队列发送（不阻塞定时任务）
                    try {
                        heartbeatService.sendHeartbeatAndValidate(heartbeat);
                        sentCount++;
                        log.debug("心跳指令已提交到队列: 设备ID={}, 发送间隔={}ms", 
                                heartbeat.getDeviceId(), sendInterval);
                    } catch (Exception e) {
                        log.error("提交心跳指令到队列失败: 设备ID={}", heartbeat.getDeviceId(), e);
                    }
                }
            }
            
            if (needSendCount > 0) {
                log.debug("心跳检查完成: 总数={}, 需要发送={}, 已提交={}", 
                        heartbeats.size(), needSendCount, sentCount);
            }
            
        } catch (Exception e) {
            log.error("心跳定时检查任务执行异常", e);
        }
    }
}

