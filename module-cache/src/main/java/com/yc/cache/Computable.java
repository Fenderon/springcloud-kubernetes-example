package com.yc.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 计算接口
 *
 * @version 1.0 create at 2020/2/28
 * @auther yangchuan
 */
public interface Computable<K extends CacheKey, V> {

    /**
     * 计算接口
     *
     * @param k 入参
     * @return 计算后结果
     */
    V compute(K k) throws ExecutionException, InterruptedException, TimeoutException;

}
