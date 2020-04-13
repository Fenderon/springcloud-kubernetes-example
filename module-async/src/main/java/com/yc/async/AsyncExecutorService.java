package com.yc.async;

import lombok.NonNull;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 异步执行服务
 *
 * @author yangchuan
 * @version 1.0 create at 2020/4/13
 */
public interface AsyncExecutorService {

    /**
     * 执行一个runnable
     *
     * @param task the runnable task
     */
    void execute(Runnable task);

    /**
     * 执行一个runnable,并且countDownLatch.countDown(1)
     *
     * @param task           the runnable task
     * @param countDownLatch the latch
     */
    void execute(Runnable task, CountDownLatch countDownLatch);

    /**
     * 执行一个runnable，return the future
     *
     * @param task the runnable task
     * @return future
     */
    <T> Future<T> execute(Callable<T> task) throws Exception;

    /**
     * 执行一个消费函数
     *
     * @param param    参数
     * @param consumer 消费喊出
     */
    <T> void execute(@NonNull T param, Consumer<T> consumer);

    /**
     * 执行一个消费函数，并且latch.countDown(1)
     *
     * @param param    参数
     * @param consumer 消费函数
     * @param latch    the latch
     */
    <T> void execute(@NonNull T param, Consumer<T> consumer, CountDownLatch latch);

    /**
     * 执行一个消费函数，并且latch.countDown(1)
     *
     * @param params   参数集合
     * @param consumer 消费函数
     * @param latch    the latch
     */
    <T> void execute(@NonNull Collection<T> params, Consumer<T> consumer, CountDownLatch latch);

    /**
     * 执行一个function
     *
     * @param param    参数
     * @param function 执行函数
     * @return 返回值
     */
    <T, R> Future<R> execute(@NonNull T param, Function<T, R> function);

    /**
     * 执行一个消费函数
     *
     * @param param    参数1
     * @param content  参数2
     * @param consumer 消费函数
     */
    <T, C> void execute(@NonNull T param, @NonNull C content, BiConsumer<T, C> consumer);
}
