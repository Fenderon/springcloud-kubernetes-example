package com.yc.springcloud.kubernetes.example.gateway.localconfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 非本地环境自定义装配
 *
 * @author yangchuan
 * @version 1.0
 * @createDate 2020/1/7
 */
@Configuration
@ConditionalOnExpression("!'${spring.profiles.active}'.equals('local')")
public class CustomizedOriginDiscoveryClientConfiguration {

    @Bean
    @Primary
    public CompositeDiscoveryClient compositeDiscoveryClient(
            List<DiscoveryClient> discoveryClients) {
        return new CompositeDiscoveryClient(discoveryClients);
    }
}
