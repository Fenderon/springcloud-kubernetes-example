package com.yc.async.example;

import com.yc.async.AsyncExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 测试类
 *
 * @author yangchuan
 * @version 1.0 create at 2020/4/13
 */
@SpringBootApplication
@ComponentScan("com.yc.async")
@Slf4j
public class BootApplication implements InitializingBean {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class);
    }

    @Autowired
    private AsyncExecutorService executorService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void afterPropertiesSet() throws Exception {

        executorService.execute(()->{
            while (true){
                System.out.println("\n--------");
                System.out.println("active count："+ threadPoolTaskExecutor.getActiveCount());
                System.out.println("core pool size："+ threadPoolTaskExecutor.getCorePoolSize());
                System.out.println("pool size："+ threadPoolTaskExecutor.getPoolSize());
                System.out.println("max pool size："+ threadPoolTaskExecutor.getMaxPoolSize());
                Thread.sleep(500);
            }
        });

        final CountDownLatch latch = new CountDownLatch(1);

        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 500; i++) {
            service.execute(()->{
                try {
                    latch.await();

                    System.out.println("---- test runnable ------");

                    //test runnable
                    testRunnable();

                    System.out.println("---- test consumer ------");

                    //test consumer
                    testConsumer();

                    System.out.println("---- test future ------");

                    //test future 多个查询，并发执行
                    testFuture();

                    System.out.println("---- test countDownLatch ------");

                    //test countDownLatch
                    testLatch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        latch.countDown();
    }

    private void testRunnable(){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("test-runnable");
            }
        });
    }

    private void testConsumer(){
        executorService.execute(1, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                log.info("counsumer: "+integer);
            }
        });
    }

    private void testFuture() throws Exception {
        Future<Integer> f1 = executorService.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 1;
            }
        });
        Future<Integer> f2 = executorService.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 1;
            }
        });
        Future<Integer> f3 = executorService.execute(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 1;
            }
        });
        log.info("result：" + (f1.get() + f2.get() + f3.get()));
    }

    private void testLatch() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        final AtomicInteger integer = new AtomicInteger();
        executorService.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            integer.incrementAndGet();
            log.info("runnable 1 finish");
        }, latch);
        executorService.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            integer.incrementAndGet();
            log.info("runnable 2 finish");
        }, latch);
        executorService.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            integer.incrementAndGet();
            log.info("runnable 3 finish");
        }, latch);
        latch.await();
        log.info("执行完成：result：" + integer.get());
    }
}
