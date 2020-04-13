package com.yc.async;

import lombok.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * request context 上下文异步处理，注入上下文属性
 *
 * @author yangchuan
 * @version 1.0 create at 2020/4/13
 */
@Async("defaultTaskExecutor")
public class DefaultRequestContextAsyncProxy implements RequestContextAsyncService {

    @Override
    public void execute(@NonNull Runnable task, ServletRequestAttributes attributes) {
        setServletRequestAttributes(attributes);
        task.run();
        resetRequestAttributes();
    }

    @Override
    public void execute(@NonNull Runnable task, CountDownLatch countDownLatch, ServletRequestAttributes attributes) {
        setServletRequestAttributes(attributes);
        task.run();
        countDownLatch.countDown();
        resetRequestAttributes();
    }

    @Override
    public <T> Future<T> execute(@NonNull Callable<T> task, ServletRequestAttributes attributes) throws Exception {
        setServletRequestAttributes(attributes);
        return new AsyncResult<>(task.call());
    }

    @Override
    public <T> void execute(T param, @NonNull Consumer<T> consumer, ServletRequestAttributes attributes) {
        setServletRequestAttributes(attributes);
        consumer.accept(param);
        resetRequestAttributes();
    }

    @Override
    public <T> void execute(T param, @NonNull Consumer<T> consumer, CountDownLatch latch, ServletRequestAttributes attributes) {
        setServletRequestAttributes(attributes);
        consumer.accept(param);
        latch.countDown();
        resetRequestAttributes();
    }

    @Override
    public <T> void execute(Collection<T> params, @NonNull Consumer<T> consumer, CountDownLatch latch, ServletRequestAttributes attributes) {
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        setServletRequestAttributes(attributes);
        params.stream().forEach(consumer);
        latch.countDown();
        resetRequestAttributes();
    }

    @Override
    public <T, R> Future<R> execute(T param, @NonNull Function<T, R> function, ServletRequestAttributes attributes) {
        setServletRequestAttributes(attributes);
        return new AsyncResult<>(function.apply(param));
    }

    @Override
    public <T, C> void execute(T param, C content, @NonNull BiConsumer<T, C> consumer, ServletRequestAttributes attributes) {
        setServletRequestAttributes(attributes);
        consumer.accept(param, content);
        resetRequestAttributes();
    }

    private static void setServletRequestAttributes(ServletRequestAttributes attributes) {
        if (Objects.nonNull(attributes)) {
            RequestContextHolder.setRequestAttributes(attributes);
        }
    }

    private static void resetRequestAttributes() {
        RequestContextHolder.resetRequestAttributes();
    }
}
