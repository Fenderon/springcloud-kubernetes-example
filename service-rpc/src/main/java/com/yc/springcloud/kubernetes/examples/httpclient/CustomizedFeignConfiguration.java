package com.yc.springcloud.kubernetes.examples.httpclient;

import feign.Client;
import okhttp3.ConnectionPool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * 装载自定义配置
 *
 * @author yangchuan
 * @version 1.0
 * @createDate 2020/1/7
 */
@Configuration
public class CustomizedFeignConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Profile("local")
    public LocalServerListLoader localServerListLoader(Environment environment) {
        return new LocalServerListLoader(environment);
    }

    @Bean
    @ConditionalOnMissingBean(Client.class)
    @Profile("local")
    public Client feignClient(LocalServerListLoader localServerListLoader
            , okhttp3.OkHttpClient okHttpClient) {
        return new CustomizedFeignClient(okHttpClient, localServerListLoader);
//        return new LoadBalancerFeignClient(delegate, cachingFactory, clientFactory);
    }

    @Configuration
    @ConditionalOnMissingBean(okhttp3.OkHttpClient.class)
    protected static class OkHttpFeignConfiguration {

        private okhttp3.OkHttpClient okHttpClient;

        @Bean
        @ConditionalOnMissingBean(ConnectionPool.class)
        public ConnectionPool httpClientConnectionPool(
                FeignHttpClientProperties httpClientProperties,
                OkHttpClientConnectionPoolFactory connectionPoolFactory) {
            Integer maxTotalConnections = httpClientProperties.getMaxConnections();
            Long timeToLive = httpClientProperties.getTimeToLive();
            TimeUnit ttlUnit = httpClientProperties.getTimeToLiveUnit();
            return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
        }

        @Bean
        public okhttp3.OkHttpClient client(OkHttpClientFactory httpClientFactory,
                                           ConnectionPool connectionPool,
                                           FeignHttpClientProperties httpClientProperties) {
            Boolean followRedirects = httpClientProperties.isFollowRedirects();
            Integer connectTimeout = httpClientProperties.getConnectionTimeout();
            this.okHttpClient = httpClientFactory
                    .createBuilder(httpClientProperties.isDisableSslValidation())
                    .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                    .followRedirects(followRedirects).connectionPool(connectionPool)
                    .build();
            return this.okHttpClient;
        }

        @PreDestroy
        public void destroy() {
            if (this.okHttpClient != null) {
                this.okHttpClient.dispatcher().executorService().shutdown();
                this.okHttpClient.connectionPool().evictAll();
            }
        }

    }

}
