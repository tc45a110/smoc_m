package com.somc.cloud.gateway.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;

/**
 * 路由配置
 */
@Slf4j
@Component
public class CustomRoutes {

    //验签过滤器
    @Resource
    private GatewayFilter verifySignatureGatewayFilter;

    @Bean
    public RouteLocator vmRouteLocator(RouteLocatorBuilder builder) {


        return builder.routes()
                //拦截所有GET请求
                .route(r -> r.method(HttpMethod.GET)
                        .filters(f ->
                                f.stripPrefix(1))
                        .uri("lb://smoc-identity")
                )
                //拦截所有HEAD请求
                .route(r -> r.method(HttpMethod.HEAD)
                        .filters(f ->
                                f.stripPrefix(1))
                        .uri("lb://smoc-identity")
                )
                //拦截所有PUT请求
                .route(r -> r.method(HttpMethod.PUT)
                        .filters(f ->
                                f.stripPrefix(1))
                        .uri("lb://smoc-identity")
                )
                //拦截所有PATCH请求
                .route(r -> r.method(HttpMethod.PATCH)
                        .filters(f ->
                                f.stripPrefix(1))
                        .uri("lb://smoc-identity")
                )
                //拦截所有DELETE请求
                .route(r -> r.method(HttpMethod.DELETE)
                        .filters(f ->
                                f.stripPrefix(1))
                        .uri("lb://smoc-identity")
                )
                //拦截所有OPTIONS请求
                .route(r -> r.method(HttpMethod.OPTIONS)
                        .filters(f ->
                                f.stripPrefix(1))
                        .uri("lb://smoc-identity")
                )
                //拦截所有TRACE请求
                .route(r -> r.method(HttpMethod.TRACE)
                        .filters(f ->
                                f.stripPrefix(1))
                        .uri("lb://smoc-identity")
                )
                //拦截符合规则的 POST 请求
                .route(r -> r.method(HttpMethod.POST)
                        .and().readBody(String.class, requestBody -> {
                            //相当于缓存了body信息，在filter 中可以这么获取 exchange.getAttribute("cachedRequestBodyObject");
                            return true;
                        })
                        .filters(f ->
                                f.filter(verifySignatureGatewayFilter).stripPrefix(1))
                        .uri("lb://smoc-identity")

                )
                .build();
    }


}
