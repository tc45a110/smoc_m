package com.smoc.cloud.filters.service.message;

import com.google.gson.Gson;
import com.smoc.cloud.filters.filter.BaseGatewayFilter;
import com.smoc.cloud.filters.request.model.RequestFullParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
/**
 * 账号短信内容 高级扩展过滤 order 60
 */
@Slf4j
@Component
public class AccountMessageExtendGatewayFilter extends BaseGatewayFilter implements Ordered, GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取body内容
        String requestBody = exchange.getAttribute("cachedRequestBodyObject");;
        //校验数据请求的数据结构
        RequestFullParams model = new Gson().fromJson(requestBody, RequestFullParams.class);

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            //被执行后调用 post
        }));

    }

    @Override
    public int getOrder() {
        return 60;
    }
}
