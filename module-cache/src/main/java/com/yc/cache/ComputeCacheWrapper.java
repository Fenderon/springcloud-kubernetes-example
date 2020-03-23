package com.yc.cache;

import java.util.concurrent.*;

/**
 * 计算缓存装饰器
 *
 * @version 1.0 create at 2020/2/28
 * @auther yangchuan
 */
public class ComputeCacheWrapper<K extends CacheKey, V> implements Computable<K, V> {

    /**
     * 委托真正执行计算的对象
     */
    private Computable<K, V> delagate;

    /**
     * 缓存管理器
     */
    private CacheManager<Future<V>> cacheManager;

    /**
     * 运行线程池
     */
    private ExecutorService executorService;

    /**
     * 定时检测缓存过期执行器
     */
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

        //是否已有任务
        Future<V> f = cacheManager.get(arg);

        if (f == null) {

            //创建新的任务
            Callable<V> callable = new Callable<V>() {
                @Override
                public V call() throws Exception {
                    return delagate.compute(arg);
                }
            };

            FutureTask ft = new FutureTask<V>(callable);

            //如果已有，就返回已存在的对象，否则返回NULL
            f = cacheManager.putIfAbsent(arg, ft);

            if (f == null) {
                //如果等于NULL，就要执行当前这个Future
                f = ft;
                //提交执行计算
                executorService.submit(ft);
            }
        }

        //获取返回值
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
