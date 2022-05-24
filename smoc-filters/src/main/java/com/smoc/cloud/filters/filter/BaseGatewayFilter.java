package com.smoc.cloud.filters.filter;

import com.google.gson.Gson;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
public class BaseGatewayFilter {

    /**
     * 成功响应
     * @param exchange
     * @return
     */
    public Mono<Void> success(ServerWebExchange exchange) {

        Long start = System.currentTimeMillis();
        log.info("[filter-  end]:{}",start);
        //响应信息
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set("Content-Type", "application/json;charset=utf-8");
        ResponseData responseData = ResponseDataUtil.buildSuccess();
        //log.error("[响应数据]数据:{}", new Gson().toJson(responseData));
        byte[] bytes = new Gson().toJson(responseData).getBytes(StandardCharsets.UTF_8);
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(bodyDataBuffer));
    }

    /**
     * 错误响应
     * @param exchange
     * @param errorCode
     * @param errorMessage
     * @return
     */
    public Mono<Void> errorHandle(ServerWebExchange exchange, String errorCode, String errorMessage) {

        Long start = System.currentTimeMillis();
        log.info("[filter-  end]:{}",start);
        //响应信息
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set("Content-Type", "application/json;charset=utf-8");
        ResponseData responseData = ResponseDataUtil.buildError(errorCode, errorMessage);
        //log.error("[响应数据]数据:{}", new Gson().toJson(responseData));
        byte[] bytes = new Gson().toJson(responseData).getBytes(StandardCharsets.UTF_8);
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(bodyDataBuffer));
    }
}
