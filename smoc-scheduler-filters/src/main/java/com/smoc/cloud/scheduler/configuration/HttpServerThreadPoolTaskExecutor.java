package com.smoc.cloud.scheduler.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

//@EnableAsync
@Configuration
public class HttpServerThreadPoolTaskExecutor implements AsyncConfigurer {

    /**
     *  cpu核心数量
     */
    public static final int cpuNum = Runtime.getRuntime().availableProcessors();

    private Integer core_pool_size = cpuNum +4;

    private Integer max_pool_size = cpuNum * 2;

    private Integer queue_capacity = 10;

    private Integer keep_alive_seconds = 60;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //配置核心线程池数量
        taskExecutor.setCorePoolSize(core_pool_size);
        //配置最大线程池数量
        taskExecutor.setMaxPoolSize(max_pool_size);
        ///线程池所使用的缓冲队列
        taskExecutor.setQueueCapacity(queue_capacity);
        //空闲线程存活时间
        taskExecutor.setKeepAliveSeconds(keep_alive_seconds);
        //等待时间（默认为0，此时立即停止），并没等待xx秒后强制停止
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        //线程池名称前缀
        taskExecutor.setThreadNamePrefix("task-executor-");
        //线程池拒绝策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //等待任务在关机时完成--表明等待所有线程执行完
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.initialize();
        return taskExecutor;
    }

}
