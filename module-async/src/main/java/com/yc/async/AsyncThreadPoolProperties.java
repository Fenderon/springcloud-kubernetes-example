package com.yc.async;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 线程池参数配置
 *
 * @author yangchuan
 * @version 1.0 create at 2020/4/13
 */
@Data
@ConfigurationProperties(prefix = "thread-pool.async")
public class AsyncThreadPoolProperties {

    /**
     * 核心线程数量 默认: 50
     */
    private int corePoolSize = 50;

    /**
     * 最大线程数量 默认: 200
     */
    private int maxPoolSize = 200;

    /**
     * 队列长度 默认: 1024
     */
    private int queueCapacity = 1024;

    /**
     * 线程最大空闲时间 单位: 秒    默认: 120
     */
    private int keepAliveTime = 120;

    /**
     * 线程池对拒绝任务的处理策略 默认{@link RejectedExecutionHandlerType#CALLER_RUNS_POLICY}
     */
    private RejectedExecutionHandlerType rejectedExecutionHandlerType = RejectedExecutionHandlerType.CALLER_RUNS_POLICY;

    /**
     * 线程前缀 默认: default-async-pool
     */
    private String threadNamePrefix = "default-async-pool-";
}
