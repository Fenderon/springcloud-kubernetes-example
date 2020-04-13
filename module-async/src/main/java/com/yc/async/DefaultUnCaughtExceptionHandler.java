package com.yc.async;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认异常处理
 *
 * @author yangchuan
 * @version 1.0 create at 2020/4/13
 */
@Slf4j
public class DefaultUnCaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.warn("线程异常：" + t.getName(), e);
    }
}
