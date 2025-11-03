
package com.server.iot.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 串口命令执行器
   通过单线程队列机制，将所有串口操作请求串行化执行，确保同一时刻只有一个操作在使用串口。
 * 多个传感器设备需要通过同一个串口进行数据采集时，必须通过此执行器提交任务，
 * 避免多个线程同时访问串口造成的数据混乱和通信失败。
 */
@Component
public class SerialCommandExecutor {
    
    /**
     * 命令任务队列，用于存储待执行的串口操作任务
     * 采用阻塞队列，当队列为空时，工作线程会阻塞等待新任务
     */
    private final BlockingQueue<Runnable> commandQueue = new LinkedBlockingQueue<>();
    
    /**
     * 工作线程，专门负责从队列中取出任务并串行执行
     */
    private final Thread workerThread;

    /**
     * 任务执行的最小间隔时间（毫秒）
     * 确保两次串口操作之间至少有500毫秒的间隔，避免串口操作过于频繁导致通信异常
     */
    private static final long MIN_INTERVAL_MS = 500;
    
    /**
     * 上次任务执行完成的时间戳（毫秒）
     * 用于计算下一个任务需要等待的时间，确保满足最小间隔要求
     */
    private long lastTaskTime = 0;

    /**
     * 构造函数
     * 初始化工作线程，该线程会持续运行，从队列中取出任务并执行
     * 工作线程设置为守护线程，当主程序退出时自动结束
     */
    public SerialCommandExecutor() {
        workerThread = new Thread(() -> {
            while (true) {
                try {
                    // 从队列中取出一个任务（队列为空时会阻塞等待）
                    Runnable task = commandQueue.take();
                    
                    // 计算距离上次任务执行完成的时间
                    long now = System.currentTimeMillis();
                    long wait = lastTaskTime + MIN_INTERVAL_MS - now;
                    
                    // 如果距离上次执行不足最小间隔时间，则等待剩余时间
                    if (wait > 0) {
                        Thread.sleep(wait);
                    }
                    
                    // 更新最后执行时间戳
                    lastTaskTime = System.currentTimeMillis();
                    
                    // 执行任务
                    try {
                        task.run();
                    } catch (Throwable t) {
                        // 捕获任务执行过程中的任何异常，避免影响后续任务
                        t.printStackTrace();
                        System.err.println("[SerialCommandExecutor] 任务执行异常: " + t.getMessage());
                    }
                } catch (InterruptedException ignored) {
                    // 线程被中断时，继续循环等待新任务
                }
            }
        }, "Serial-Command-Executor");
        
        // 设置为守护线程，主程序退出时自动结束
        workerThread.setDaemon(true);
        // 启动工作线程
        workerThread.start();
    }

    /**
     * 提交一个Runnable任务到执行队列
     * 任务会被添加到队列末尾，等待工作线程串行执行
     * 
     * @param task 待执行的串口操作任务
     */
    public void submit(Runnable task) {
        commandQueue.offer(task);
    }

    /**
     * 提交一个Callable任务到执行队列
     * 任务会被添加到队列末尾，等待工作线程串行执行
     * 返回Future对象，可用于获取任务执行结果或检查任务状态
     * 
     * @param task 待执行的串口操作任务，可以有返回值
     * @param <T> 任务返回值的类型
     * @return Future对象，可用于获取任务执行结果
     */
    public <T> Future<T> submit(Callable<T> task) {
        FutureTask<T> futureTask = new FutureTask<>(task);
        commandQueue.offer(futureTask);
        return futureTask;
    }
}