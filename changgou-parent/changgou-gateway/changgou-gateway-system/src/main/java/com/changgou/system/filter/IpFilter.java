package com.changgou.system.filter;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

/**
 * 获取客户端的访问ip
 * @author Joan
 * @date 2019-11-04 21:05
 */
@Component
public class IpFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("经过第一个过滤器IpFilter");
        ServerHttpRequest request = exchange.getRequest();
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        System.out.println("ip:"+remoteAddress.getHostName());
        return chain.filter(exchange);
    }

    /**
     * 过滤优先级，值越小优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 1;
    }
}
