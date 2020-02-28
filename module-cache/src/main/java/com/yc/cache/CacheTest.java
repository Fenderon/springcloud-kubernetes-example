package com.yc.cache;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Description TODO
 *
 * @version 1.0 create at 2020/2/28
 * @auther yangchuan
 */
public class CacheTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

        ExecutorService executorService = Executors.newCachedThreadPool();

        Computable computable = new Computable<CacheReqeust, String>() {
            @Override
            public String compute(CacheReqeust cacheKey) throws ExecutionException, InterruptedException {
                Thread.sleep(1000);
                return cacheKey.getKey() + ": " + new Random().nextInt(100);
            }
        };

        ComputeCacheWrapper computeWrapper = new ComputeCacheWrapper<CacheReqeust, String>(
                computable,
                CacheManagerFactory.createDefaultCacheManager(),
                executorService
        );

        //演示并发
        CountDownLatch countDownLatch = new CountDownLatch(1);

        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    computeWrapper.compute(new CacheReqeust(),100,TimeUnit.MICROSECONDS);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        long startTime = System.currentTimeMillis();

        countDownLatch.countDown();

        Thread.sleep(1000);

        executorService.shutdown();

        while (!executorService.isTerminated()) {

        }

        System.out.println("耗时：" + (System.currentTimeMillis() - startTime));

//        for (int i = 0; i < 100; i++) {
//            System.out.println(computable.compute(new CacheReqeust()));
//        }
    }
}
