package com.yc.cache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

/**
 * 高并发缓存测试
 *
 * @version 1.0 create at 2020/2/28
 * @auther yangchuan
 */
public class CacheTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

        ExecutorService executorService = Executors.newCachedThreadPool();

        Computable computable = new TestComputable();

        ComputeCacheWrapper computeWrapper = new ComputeCacheWrapper<CacheReqeust, String>(
                computable,
                CacheManagerFactory.createDefaultCacheManager(),
                executorService
        );

        //演示并发
        CountDownLatch countDownLatch = new CountDownLatch(1);

        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "开始等待");
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat simpleDateFormat = ThreadSafeFormatter.dateFormatTheadLocal.get();
                String time = simpleDateFormat.format(new Date());

                try {
                    System.out.println(Thread.currentThread().getName() + ": " + time + " 被放行");

                    computeWrapper.compute(new CacheReqeust());
//                    computeWrapper.compute(new CacheReqeust(),100,TimeUnit.MICROSECONDS);
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

        //多等一，，压测
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
