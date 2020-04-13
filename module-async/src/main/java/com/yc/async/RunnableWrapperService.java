package com.yc.async;

/**
 * 任务装饰服务
 *
 * @author yangchuan
 * @version 1.0 create at 2020/4/13
 */
public interface RunnableWrapperService {

    /**
     * 装饰runnable
     *
     * @param runnable
     * @return
     */
    Runnable wrapper(Runnable runnable);
}
