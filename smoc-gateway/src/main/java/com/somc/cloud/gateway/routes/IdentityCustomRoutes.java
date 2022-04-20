package com.somc.cloud.gateway.routes;

import com.somc.cloud.gateway.limiter.HostAddrKeyResolver;
import com.somc.cloud.gateway.limiter.UserKeyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;

/**
 * 身份证验证路由
 */
@Slf4j
@Component
public class IdentityCustomRoutes {

    //验签过滤器
    @Resource
    private GatewayFilter verifySignatureGatewayFilter;

    //验签过滤器
    @Resource
    private GatewayFilter httpServerSignatureGatewayFilter;

    @Autowired
    @Qualifier("userKeyResolver")
    private UserKeyResolver userKeyResolver;

    @Autowired
    @Qualifier("hostAddrKeyResolver")
    private HostAddrKeyResolver hostAddrKeyResolver;


    @Bean
    public RouteLocator identityRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()
                //拦截所有GET请求
                .route(r -> r.method(HttpMethod.GET).filters(f -> f.stripPrefix(1)).uri("lb://smoc-identity"))

                /**
                 * 身份认证服务  线路服务中burstCapacity令牌桶的上限  replenishRate 每秒平均速率
                 */.route(r -> r.method(HttpMethod.POST).and().path("/smoc-gateway/smoc-identity/**").and().header("signature-nonce", "\\d+").and().readBody(String.class, requestBody -> {
                            //相当于缓存了body信息，在filter 中可以这么获取 exchange.getAttribute("cachedRequestBodyObject");
                            return true;
                        }).filters(f -> f.stripPrefix(1).requestRateLimiter().rateLimiter(RedisRateLimiter.class, c -> c.setBurstCapacity(10).setReplenishRate(4)) //burstCapacity令牌桶的上限  replenishRate 每秒平均速率
                                .configure(c -> c.setKeyResolver(hostAddrKeyResolver)).filter(verifySignatureGatewayFilter)).uri("lb://smoc-identity")

                )
                /**
                 * http协议发送短信服务  线路服务中burstCapacity令牌桶的上限  replenishRate 每秒平均速率
                 */.route(r -> r.method(HttpMethod.POST).and().path("/smoc-gateway/http-server/**").and().header("signature-nonce", "\\d+").and().readBody(String.class, requestBody -> {
                            //相当于缓存了body信息，在filter 中可以这么获取 exchange.getAttribute("cachedRequestBodyObject");
                            return true;
                        }).filters(f -> f.stripPrefix(1).requestRateLimiter().rateLimiter(RedisRateLimiter.class, c -> c.setBurstCapacity(10).setReplenishRate(4)) //burstCapacity令牌桶的上限  replenishRate 每秒平均速率
                                .configure(c -> c.setKeyResolver(userKeyResolver)).filter(httpServerSignatureGatewayFilter)).uri("lb://smoc-http-server")

                ).build();
    }


}
