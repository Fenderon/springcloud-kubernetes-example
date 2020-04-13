package com.yc.async;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程自动化配置
 *
 * @author yangchuan
 * @version 1.0 create at 2020/4/13
 */
@EnableAsync
@EnableConfigurationProperties(AsyncThreadPoolProperties.class)
public class AsyncThreadPoolAutoConfiguration implements DisposableBean {

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired(required = false)
    private List<RunnableWrapper> runnableWrappers;

    @Bean
    @ConditionalOnProperty(value = "thread-pool.async.auto-logged", matchIfMissing = true)
    public RunnableWrapper autoLoggedErrorRunnableWrapper() {
        return new AutoLoggedRunnableWrapper();
    }

    @Bean
    @ConditionalOnMissingBean
    private RequestContextAsyncService requestContextAsyncService() {
        return new DefaultRequestContextAsyncProxy();
    }

    @Bean
    private RunnableWrapperService runnableWrapperService() {
        return new DefaultRunnableWrapperService(runnableWrappers);
    }

    @Bean
    @ConditionalOnMissingBean
    private AsyncExecutorService asyncExecutorService(RequestContextAsyncService requestContextAsyncService,
                                                      RunnableWrapperService runnableWrapperService) {
        return new DefaultExecutorService(requestContextAsyncService, runnableWrapperService);
    }

    @Bean
    @Primary
    public ThreadPoolTaskExecutor defaultTaskExecutor(AsyncThreadPoolProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.setKeepAliveSeconds(properties.getKeepAliveTime());
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());

        // 线程池对拒绝任务的处理策略
        RejectedExecutionHandler rejectedExecutionHandler;
        RejectedExecutionHandlerType rejectedExecutionHandlerType = properties
                .getRejectedExecutionHandlerType();
        if (Objects.isNull(rejectedExecutionHandlerType)) {
            rejectedExecutionHandlerType = RejectedExecutionHandlerType.CALLER_RUNS_POLICY;
        }

        switch (rejectedExecutionHandlerType) {
            case CALLER_RUNS_POLICY:
                rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
                break;
            case ABORT_POLICY:
                rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
                break;
            case DISCARD_OLDEST_POLICY:
                rejectedExecutionHandler = new ThreadPoolExecutor.DiscardOldestPolicy();
                break;
            case DISCARD_POLICY:
                rejectedExecutionHandler = new ThreadPoolExecutor.DiscardPolicy();
                break;
            default:
                throw new UnsupportedOperationException(rejectedExecutionHandlerType.name());
        }

        executor.setRejectedExecutionHandler(rejectedExecutionHandler);

        // 初始化
        executor.initialize();

        //设置异常处理
        Thread.setDefaultUncaughtExceptionHandler(new DefaultUnCaughtExceptionHandler());
        return this.threadPoolTaskExecutor = executor;
    }

    @Override
    public void destroy() throws Exception {
        this.threadPoolTaskExecutor.destroy();
    }
}
