package com.godarmed.core.framework.gateway.config;

import com.godarmed.core.framework.gateway.properties.OpenPathProperties;
import com.godarmed.core.starters.global.entity.HeaderEntity;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * token校验全局过滤器
 */
@Log4j2
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    @Autowired
    private OpenPathProperties openPathProperties;

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // TODO Auto-generated method stub
        ServerHttpRequest request = exchange.getRequest();
        log.info("gateway reqeust=====> {}", request.getURI().getPath());
        // /AuthService/oauth请求，直接向下执行
        if (StringUtils.contains(openPathProperties.getPaths(), request.getURI().getPath())) {
            return chain.filter(exchange);
        }

        HttpHeaders headers = request.getHeaders();

        //判断请求是否有requestId
        String requestId = headers.getFirst(HeaderEntity.REQUEST_ID);
        if (StringUtils.isBlank(requestId)) {
            ServerHttpRequest addHeard = exchange.getRequest().mutate().header(HeaderEntity.REQUEST_ID, UUID.randomUUID().toString()).build();
            //将现在的request 变成 change对象
            ServerWebExchange addexchange = exchange.mutate().request(addHeard).build();
            return chain.filter(addexchange);
        }
        return chain.filter(exchange);
    }
}
