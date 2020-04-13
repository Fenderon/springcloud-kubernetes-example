package com.yc.async;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 自动日志装饰器
 *
 * @author yangchuan
 * @version 1.0 create at 2020/4/13
 */
@Slf4j
public class AutoLoggedErrorRunnableWrapper implements RunnableWrapper {

    @Override
    public Runnable wrapper(Runnable runnable) {
        return new AutoLoggedRunnable(runnable);
    }

    @AllArgsConstructor
    private class AutoLoggedRunnable implements Runnable {

        @NonNull
        private final Runnable delegate;

        @Override
        public void run() {
            log.info("异步线程开始执行：" + Thread.currentThread().getName());
            try {
                delegate.run();
            } catch (Exception e) {
                log.error("异步线程发生异常：{}", Thread.currentThread().getName(), e);
            }
            log.info("异步线程执行结束：" + Thread.currentThread().getName());
        }

    }
}
