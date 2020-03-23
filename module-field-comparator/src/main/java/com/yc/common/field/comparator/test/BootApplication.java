package com.yc.common.field.comparator.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 *
 * @author yangchuan
 * @version 1.0 create at 2020/3/23
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.yc.common.field.comparator")
public class BootApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class,args);
    }
}
