package com.yc.springcloud.kubernetes.example.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 网关服务
 *
 * @author yangchuan
 * @version 1.0
 * @createDate 2020/1/7
 */
@SpringBootApplication(exclude = CompositeDiscoveryClientAutoConfiguration.class)
@EnableFeignClients
@RestController
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/")
    public String hello() {
        return "this is gateway service";
    }

    @GetMapping("/services")
    public List<String> services() {
        return this.discoveryClient.getServices();
    }

}
