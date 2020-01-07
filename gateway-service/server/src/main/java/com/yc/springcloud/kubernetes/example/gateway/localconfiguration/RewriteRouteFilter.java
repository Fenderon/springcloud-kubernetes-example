package com.yc.springcloud.kubernetes.example.gateway.localconfiguration;

import com.netflix.loadbalancer.Server;
import com.yc.springcloud.kubernetes.examples.httpclient.LocalServerListLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;


/**
 * 本地环境重写路由过滤器
 *
 * @author yangchuan
 * @version 1.0
 * @createDate 2020/1/7
 */
@Slf4j
public class RewriteRouteFilter implements GlobalFilter, Ordered {

    @Autowired
    private LocalServerListLoader localServerListLoader;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        if (route == null) {
            return chain.filter(exchange);
        }
        URI uri = exchange.getRequest().getURI();
        boolean encoded = containsEncodedParts(uri);
        URI routeUri = route.getUri();

        Server server = localServerListLoader.getLocalServer(routeUri.getHost());

        if (server != null) {

            URI mergedUrl = UriComponentsBuilder.fromUri(uri)
                    // .uri(routeUri)
                    .scheme(routeUri.getScheme()).host(server.getHost())
                    .port(server.getPort()).build(encoded).toUri();

            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, mergedUrl);

            log.info("Rewrite route {} to {}", uri, mergedUrl);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
