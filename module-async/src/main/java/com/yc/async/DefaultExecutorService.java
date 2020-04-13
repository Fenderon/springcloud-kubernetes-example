package com.yc.async;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 默认异步处理服务
 *
 * @author yangchuan
 * @version 1.0 create at 2020/4/13
 */
@AllArgsConstructor
public class DefaultExecutorService implements AsyncExecutorService {

    private final RequestContextAsyncService requestContextAsyncService;
    private final RunnableWrapperService runnableWrapperService;

    @Override
    public void execute(Runnable task) {
        task = runnableWrapperService.wrapper(task);
        requestContextAsyncService.execute(task, getServletRequestAttributes());
    }

    @Override
    public void execute(Runnable task, CountDownLatch countDownLatch) {
        task = runnableWrapperService.wrapper(task);
        requestContextAsyncService.execute(task, countDownLatch, getServletRequestAttributes());
    }

    @Override
    public <T> Future<T> execute(Callable<T> task) throws Exception {
        return requestContextAsyncService.execute(task, getServletRequestAttributes());
    }

    @Override
    public <T> void execute(T param, Consumer<T> consumer) {
        requestContextAsyncService.execute(param, consumer, getServletRequestAttributes());
    }

    @Override
    public <T> void execute(T param, Consumer<T> consumer, CountDownLatch latch) {
        requestContextAsyncService.execute(param, consumer, latch, getServletRequestAttributes());
    }

    @Override
    public <T> void execute(Collection<T> params, Consumer<T> consumer, CountDownLatch latch) {
        requestContextAsyncService.execute(params, consumer, latch, getServletRequestAttributes());
    }

    @Override
    public <T, R> Future<R> execute(T param, Function<T, R> function) {
        return new AsyncResult<>(function.apply(param));
    }

    @Override
    public <T, C> void execute(T param, C content, BiConsumer<T, C> consumer) {
        consumer.accept(param, content);
    }

    private static ServletRequestAttributes getServletRequestAttributes() {
        return ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes());
    }
}
