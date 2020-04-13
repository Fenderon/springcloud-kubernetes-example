package com.yc.async;

/**
 * Runnable装饰器
 *
 * @author yangchuan
 * @version 1.0 create at 2020/4/13
 */
public interface RunnableWrapper {

    /**
     * 装饰器
     *
     * @param runnable 任务
     * @return 任务
     */
    Runnable wrapper(Runnable runnable);
}
