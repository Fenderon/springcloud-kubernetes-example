package com.yc.async.example;

import com.yc.async.AsyncExecutorService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.function.Consumer;

/**
 * 测试类
 *
 * @author yangchuan
 * @version 1.0 create at 2020/4/13
 */
@SpringBootApplication
@ComponentScan("com.yc.async")
public class BootApplication implements InitializingBean {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class);
    }

    @Autowired
    private AsyncExecutorService executorService;

    @Override
    public void afterPropertiesSet() throws Exception {
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("test-runnable");
                }
            });
            executorService.execute(1L, new Consumer<Object>() {
                @Override
                public void accept(Object o) {
                    System.out.println(o);
                }
            });
        }
    }
}
