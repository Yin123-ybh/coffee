package com.ybh.coffee.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthHeaderFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
        if (userId == null || userId.isBlank()) {
            userId = "1";
        }
        ServerWebExchange mutated = exchange.mutate()
                .request(builder -> builder.header("X-User-Id", userId).header(HttpHeaders.CACHE_CONTROL, "no-store"))
                .build();
        return chain.filter(mutated);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
