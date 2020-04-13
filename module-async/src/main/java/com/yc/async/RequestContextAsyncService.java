package com.yc.async;

import org.springframework.web.context.request.ServletRequestAttributes;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * request context上下文异步处理服务
 *
 * @author yangchuan
 * @version 1.0 create at 2020/4/13
 */
public interface RequestContextAsyncService {

    /**
     * 执行一个runnable
     *
     * @param task       the runnable task
     * @param attributes request context attributes
     */
    void execute(Runnable task, ServletRequestAttributes attributes);

    /**
     * 执行一个runnable,并且countDownLatch.countDown(1)
     *
     * @param task           the runnable task
     * @param countDownLatch the latch
     * @param attributes     request context attributes
     */
    void execute(Runnable task, CountDownLatch countDownLatch, ServletRequestAttributes attributes);

    /**
     * 执行一个runnable，return the future
     *
     * @param task       the runnable task
     * @param attributes request context attributes
     * @return future
     */
    <T> Future<T> execute(Callable<T> task, ServletRequestAttributes attributes) throws Exception;

    /**
     * 执行一个消费函数
     *
     * @param param      参数
     * @param consumer   消费函数
     * @param attributes request context attributes
     */
    <T> void execute(@NotNull T param, Consumer<T> consumer, ServletRequestAttributes attributes);

    /**
     * 执行一个消费函数，并且latch.countDown(1)
     *
     * @param param      参数
     * @param consumer   消费函数
     * @param latch      the latch
     * @param attributes request context attributes
     */
    <T> void execute(@NotNull T param, Consumer<T> consumer, CountDownLatch latch, ServletRequestAttributes attributes);

    /**
     * 执行一个消费函数，并且latch.countDown(1)
     *
     * @param params     参数集合
     * @param consumer   消费函数
     * @param latch      the latch
     * @param attributes request context attributes
     */
    <T> void execute(@NotEmpty Collection<T> params, Consumer<T> consumer, CountDownLatch latch, ServletRequestAttributes attributes);

    /**
     * 执行一个function
     *
     * @param param      参数
     * @param function   执行函数
     * @param attributes request context attributes
     * @return 返回值
     */
    <T, R> Future<R> execute(@NotNull T param, Function<T, R> function, ServletRequestAttributes attributes);

    /**
     * 执行一个消费函数
     *
     * @param param      参数1
     * @param content    参数2
     * @param consumer   消费函数
     * @param attributes request context attributes
     */
    <T, C> void execute(@NotNull T param, @NotNull C content, BiConsumer<T, C> consumer, ServletRequestAttributes attributes);
}
