package com.yc.springcloud.kubernetes.example.gateway.configuration.local;

import com.yc.springcloud.kubernetes.examples.httpclient.LocalServerListLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClient;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Arrays;

/**
 * 本地环境自定义装配
 *
 * @author yangchuan
 * @version 1.0
 * @createDate 2020/1/7
 */
@Configuration
@ConditionalOnExpression("'${spring.profiles.active}'.equals('local')")
public class CustomizedLocalDiscoveryClientConfiguration {

    @Autowired
    private LocalServerListLoader localServerListLoader;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Primary
    public CompositeDiscoveryClient LocaldiscoveryClient() {
        return new CompositeDiscoveryClient(Arrays.asList(new CustomizeDiscoveryClient(localServerListLoader)));
    }


    @Bean
    public GlobalFilter rewriteRouteFilter(){
        return new RewriteRouteFilter();
    }
}
