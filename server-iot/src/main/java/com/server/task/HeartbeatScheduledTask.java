package com.server.task;

import com.server.domain.AgricultureDeviceHeartbeat;
import com.server.service.AgricultureDeviceHeartbeatService;
import com.server.service.AgricultureHeartbeatSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 心跳指令定时发送任务
 * 系统启动时检查一次所有设备，然后为每个设备创建独立的线程，按照各自的间隔独立发送心跳
 * 
 * 优点：
 * 1. 每个设备独立线程，互不干扰
 * 2. 每个设备可以有不同的发送间隔（sendInterval）
 * 3. 精确控制，每个设备按照自己的间隔独立运行
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
    private AgricultureHeartbeatSendService agricultureHeartbeatSendService;
    
    // 存储每个设备的心跳线程状态
    private final ConcurrentHashMap<Long, AtomicBoolean> deviceThreadFlags = new ConcurrentHashMap<>();
    
    /**
     * 初始化方法
     */
    @PostConstruct
    public void init() {
        log.info("正在初始化心跳任务服务...");
    }
    
    /**
     * 监听应用就绪事件（ApplicationReadyEvent）
     * Spring Boot应用完全启动并准备好接收请求时，调用此方法
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        startHeartbeatTasks();
    }
    
    /**
     * Spring容器销毁HeartbeatScheduledTask的Bean之前调用
     * 执行资源清理工作，优雅地关闭所有线程
     */
    @PreDestroy
    public void destroy() {
        log.info("正在关闭心跳任务服务...");
        // 停止所有设备的心跳线程
        for (Long deviceId : deviceThreadFlags.keySet()) {
            AtomicBoolean flag = deviceThreadFlags.get(deviceId);
            if (flag != null) {
                flag.set(false);
            }
        }
        deviceThreadFlags.clear();
    }
    
    /**
     * 启动所有设备的心跳任务
     * 系统启动时检查一次，然后为每个设备创建独立的线程
     */
    private void startHeartbeatTasks() {
        try {
            // 系统启动时检查一次：查询所有需要发送心跳的设备
            List<AgricultureDeviceHeartbeat> heartbeats = agricultureDeviceHeartbeatService.list();
            
            if (heartbeats == null || heartbeats.isEmpty()) {
                log.info("未找到需要发送心跳的设备");
                return;
            }
            
            log.info("系统启动时检查到 {} 个设备需要发送心跳", heartbeats.size());
            
            // 为每个设备创建独立的线程，按照各自的间隔独立发送心跳
            for (AgricultureDeviceHeartbeat heartbeat : heartbeats) {
                // 检查心跳指令是否配置
                if (heartbeat.getHeartbeatCmdHex() == null || heartbeat.getHeartbeatCmdHex().trim().isEmpty()) {
                    log.warn("设备ID={} 未配置心跳指令，跳过心跳任务启动", heartbeat.getDeviceId());
                    continue;
                }
                
                // 启动该设备的独立心跳任务
                startDeviceHeartbeatTask(heartbeat);
            }
            
            log.info("心跳任务启动完成，共启动 {} 个设备的心跳任务", deviceThreadFlags.size());
            
        } catch (Exception e) {
            log.error("启动心跳任务失败", e);
        }
    }
    
    /**
     * 为单个设备启动独立的心跳任务线程
     * 每个设备按照自己的间隔独立运行
     * 
     * @param heartbeat 心跳记录对象
     */
    private void startDeviceHeartbeatTask(AgricultureDeviceHeartbeat heartbeat) {
        final Long deviceId = heartbeat.getDeviceId();
        if (deviceId == null) {
            log.warn("设备ID为空，跳过心跳任务启动");
            return;
        }
        
        // 获取发送间隔（毫秒），如果没有设置则使用默认值5000毫秒（5秒）
        Long initialSendInterval = heartbeat.getSendInterval();
        if (initialSendInterval == null || initialSendInterval <= 0) {
            initialSendInterval = 5000L; // 默认5秒
        }
        
        // 使用 AtomicLong 存储发送间隔，支持在 lambda 中修改
        final AtomicLong sendInterval = new AtomicLong(initialSendInterval);
        
        // 创建线程标志
        final AtomicBoolean running = new AtomicBoolean(true);
        deviceThreadFlags.put(deviceId, running);
        
        // 创建线程名称（final）
        final String threadName = "Heartbeat-Device-" + deviceId;
        
        // 为该设备创建独立的线程
        new Thread(() -> {
            Thread.currentThread().setName(threadName);
            
            log.info("启动设备 {} 的心跳任务线程，发送间隔={}ms", deviceId, sendInterval.get());
            
            // 首次延迟，等待设备初始化完成
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            
            while (running.get() && !Thread.currentThread().isInterrupted()) {
                try {
                    // 实时从数据库获取最新的心跳记录
                    AgricultureDeviceHeartbeat latestHeartbeat = agricultureDeviceHeartbeatService
                            .lambdaQuery()
                            .eq(AgricultureDeviceHeartbeat::getDeviceId, deviceId)
                            .one();
                    
                    if (latestHeartbeat == null) {
                        log.warn("设备ID={} 的心跳记录不存在，停止心跳任务", deviceId);
                        break;
                    }
                    
                    // 检查心跳指令是否还存在
                    if (latestHeartbeat.getHeartbeatCmdHex() == null || latestHeartbeat.getHeartbeatCmdHex().trim().isEmpty()) {
                        log.warn("设备ID={} 的心跳指令已删除，停止心跳任务", deviceId);
                        break;
                    }
                    
                    // 获取最新的发送间隔并更新
                    Long latestSendInterval = latestHeartbeat.getSendInterval();
                    if (latestSendInterval != null && latestSendInterval > 0) {
                        sendInterval.set(latestSendInterval);
                    }
                    
                    // 发送心跳指令
                    agricultureHeartbeatSendService.sendHeartbeatAndValidate(latestHeartbeat);
                    
                    // 按照设备的间隔休眠
                    Thread.sleep(sendInterval.get());
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.info("设备ID={} 的心跳任务线程被中断", deviceId);
                    break;
                } catch (Exception e) {
                    log.error("设备ID={} 的心跳任务执行异常", deviceId, e);
                    // 发生异常时，等待一段时间后继续
                    try {
                        Thread.sleep(sendInterval.get());
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
            
            log.info("设备ID={} 的心跳任务线程已停止", deviceId);
            deviceThreadFlags.remove(deviceId);
            
        }, threadName).start();
    }
}

