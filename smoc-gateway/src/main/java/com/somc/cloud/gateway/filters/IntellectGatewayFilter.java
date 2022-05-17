package com.somc.cloud.gateway.filters;

import com.google.gson.Gson;
import com.smoc.cloud.common.response.ResponseCode;
import com.somc.cloud.gateway.configuration.IntelligenceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 梦网回执 验证签名
 */
@Slf4j
@Component
public class IntellectGatewayFilter {

    @Autowired
    private IntelligenceProperties intelligenceProperties;

    /**
     * 验签过滤器
     *
     * @return
     */
    @Bean
    public GatewayFilter intellectSignatureGatewayFilter() {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

                ServerHttpRequest request = exchange.getRequest();
                URI uri = request.getURI();
                log.info("[梦网回执请求]URI:{}", uri.toString());
                //回执是否验证签名
                if (null != intelligenceProperties.getCallbackIsSign() && intelligenceProperties.getCallbackIsSign()) {
                    //HttpHeaders
                    HttpHeaders headers = exchange.getRequest().getHeaders();
                    //String account = headers.getFirst("account");
                    String pwd = headers.getFirst("pwd");
                    String timestamp = headers.getFirst("timestamp");
                    String sign = DigestUtils.md5Hex(intelligenceProperties.getAccount().toUpperCase() + "00000000" + intelligenceProperties.getPassword() + timestamp);
                    //验签错误
                    if (!sign.equals(pwd)) {
                        return errorHandle(exchange, ResponseCode.SIGN_ERROR.getCode(), ResponseCode.SIGN_ERROR.getMessage());
                    }
                }

                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    //被执行后调用 post
                }));

            }
        };
    }

    public Mono<Void> errorHandle(ServerWebExchange exchange, String errorCode, String errorMessage) {
        //响应信息
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set("Content-Type", "application/json;charset=utf-8");
        Map<String, Object> result = new HashMap<>();
        result.put("subCode", 100);
        result.put("message", errorMessage);
        log.error("[响应数据]数据:{}", new Gson().toJson(result));
        byte[] bytes = new Gson().toJson(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(bodyDataBuffer));
    }
}
