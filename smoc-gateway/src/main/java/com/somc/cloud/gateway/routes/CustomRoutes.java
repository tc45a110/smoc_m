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
 * 路由
 */
@Slf4j
@Component
public class CustomRoutes {

    //身份认证验签过滤器
    @Resource
    private GatewayFilter identitySignatureGatewayFilter;

    //http短信服务验签过滤器
    @Resource
    private GatewayFilter shortMessageSignatureGatewayFilter;

    //梦网回执
    @Resource
    private GatewayFilter intellectSignatureGatewayFilter;

    @Bean
    public RouteLocator identityRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                //拦截所有GET请求
                .route(r -> r.method(HttpMethod.GET).filters(f -> f.stripPrefix(1)).uri("lb://smoc-identity"))

                /**
                 * 身份认证服务
                 */
                .route(r -> r.method(HttpMethod.POST).and().path("/smoc-gateway/identity/smoc-identity/**").and().header("signature-nonce", "\\d+").and().readBody(String.class, requestBody -> {
                            //相当于缓存了body信息，在filter 中可以这么获取 exchange.getAttribute("cachedRequestBodyObject");
                            return true;
                        }).filters(f -> f.stripPrefix(2).filter(identitySignatureGatewayFilter)).uri("lb://smoc-identity")

                )
                /**
                 * 智能短信回执
                 */
                .route(r -> r.method(HttpMethod.POST).and().path("/smoc-gateway/intellect/smoc-identity/AimService/**").and().readBody(String.class, requestBody -> {
                            //相当于缓存了body信息，在filter 中可以这么获取 exchange.getAttribute("cachedRequestBodyObject");
                            return true;
                        }).filters(f -> f.stripPrefix(2).filter(intellectSignatureGatewayFilter)).uri("lb://smoc-identity")

                )
                /**
                 * http协议发送短信服务
                 */
                .route(r -> r.method(HttpMethod.POST).and().path("/smoc-gateway/http-server/**").and().header("signature-nonce", "\\d+").and().readBody(String.class, requestBody -> {
                            //相当于缓存了body信息，在filter 中可以这么获取 exchange.getAttribute("cachedRequestBodyObject");
                            return true;
                        }).filters(f -> f.stripPrefix(1).filter(shortMessageSignatureGatewayFilter)).uri("lb://smoc-http-server")

                ).build();
    }

//配置限流代码 先注释掉。现在增加了根据业务动态限流
//     .requestRateLimiter().rateLimiter(RedisRateLimiter.class, c -> c.setBurstCapacity(20).setReplenishRate(10)) //burstCapacity令牌桶的上限  replenishRate 每秒平均速率
//            .configure(c -> c.setKeyResolver(hostAddrKeyResolver))

}
