package com.yc.cache;

import java.util.concurrent.*;

/**
 * 计算缓存装饰器
 *
 * @version 1.0 create at 2020/2/28
 * @auther yangchuan
 */
public class ComputeCacheWrapper<K extends CacheKey, V> implements Computable<K, V> {

    private Computable<K, V> delagate;

    private CacheManager<Future<V>> cacheManager;

    private ExecutorService executorService;

    private ExecutorService singleExecutor = Executors.newSingleThreadExecutor();

    private final static ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(5);

    private ComputeCacheWrapper() {

    }

    public ComputeCacheWrapper(Computable delagate,
                               CacheManager cacheManager,
                               ExecutorService executorService) {
        this.delagate = delagate;
        this.cacheManager = cacheManager;
        this.executorService = executorService;
    }


    @Override
    public V compute(final K arg) throws ExecutionException, InterruptedException, TimeoutException {

        Future<V> f = cacheManager.get(arg);

        if (f == null) {

            Callable<V> callable = new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return delagate.compute(arg);
                }
            };

            FutureTask ft = new FutureTask<V>(callable);

            f = cacheManager.putIfAbsent(arg, ft);

            if (f == null) {
                f = ft;
                //提交执行计算
                executorService.submit(ft);
            }
        }

        V v = f.get();

        return v;
    }

    public V compute(final K arg, long expire, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        if (expire > 0) {
            scheduledExecutor.schedule(new Runnable() {
                @Override
                public void run() {
                    expire(arg);
                }
            }, expire, timeUnit);
        }
        return compute(arg);
    }

    private synchronized void expire(K arg) {
        Future<V> f = cacheManager.get(arg);
        if (f != null) {
            if (!f.isDone()) {
                System.out.println("任务被取消");
                f.cancel(true);
            }
            System.out.println("过期时间到，缓存被清除");
            cacheManager.remove(arg);
        }
    }
}
