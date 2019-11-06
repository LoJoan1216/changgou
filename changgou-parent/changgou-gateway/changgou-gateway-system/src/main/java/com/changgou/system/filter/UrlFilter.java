package com.changgou.system.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 客户端访问url
 *
 * @author Joan
 * @date 2019-11-05 14:57
 */
@Component
public class UrlFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("经过了第二个过滤器UrlFilter");
        ServerHttpRequest request = exchange.getRequest();
        RequestPath path = request.getPath();
        System.out.println("url:" + path);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
