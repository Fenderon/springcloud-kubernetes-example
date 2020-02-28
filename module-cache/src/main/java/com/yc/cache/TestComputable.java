package com.yc.cache;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 测试计算类
 *
 * @version 1.0 create at 2020/2/28
 * @auther yangchuan
 */
public class TestComputable<K extends CacheKey, V> implements Computable {

    @Override
    public V compute(CacheKey cacheKey) throws ExecutionException, InterruptedException, TimeoutException {
        Thread.sleep(1000);
        return (V) (cacheKey.getKey() + ": " + new Random().nextInt(100));
    }
}
