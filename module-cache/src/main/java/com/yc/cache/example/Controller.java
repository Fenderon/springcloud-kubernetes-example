package com.yc.cache.example;

import com.yc.cache.*;

import java.util.concurrent.*;

/**
 * Description TODO
 *
 * @author yangchuan
 * @version 1.0 create at 2020/3/23
 */
public class Controller {

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private static CacheManager cacheManager = CacheManagerFactory.createDefaultCacheManager();

    private TestService testService = new TestService();

    public Object get() throws InterruptedException, ExecutionException, TimeoutException {
        return new ComputeCacheWrapper<>(
                new Computable() {
                    @Override
                    public Object compute(CacheKey cacheKey) throws ExecutionException, InterruptedException, TimeoutException {
                        return testService.get((CacheReqeust) cacheKey);
                    }
                },
                cacheManager,
                executorService
        ).compute(new CacheReqeust());
    }

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);
        Controller controller = new Controller();
        for (int i = 0; i < 10000; i++) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                        System.out.println(controller.get());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        latch.countDown();

        while (true){

        }
    }
}
