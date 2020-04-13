package com.yc.async;

/**
 * 线程池对拒绝任务的处理策略
 *
 * @author yangchuan
 * @version 1.0 create at 2020/4/13
 */
public enum RejectedExecutionHandlerType {

    /**
     * 主线程直接执行该任务，执行完之后尝试添加下一个任务到线程池中，可以有效降低向线程池内添加任务的速度
     */
    CALLER_RUNS_POLICY,

    /**
     * 直接抛出java.util.concurrent.RejectedExecutionException异常
     */
    ABORT_POLICY,

    /**
     * 抛弃当前任务、暂不支持；会导致被丢弃的任务无法再次被执行
     */
    DISCARD_POLICY,

    /**
     * 抛弃旧的任务、暂不支持；会导致被丢弃的任务无法再次被执行
     */
    DISCARD_OLDEST_POLICY;
}
