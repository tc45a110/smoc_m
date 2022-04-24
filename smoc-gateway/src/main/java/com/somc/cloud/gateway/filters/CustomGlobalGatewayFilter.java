package com.somc.cloud.gateway.filters;

import com.google.gson.Gson;
import com.smoc.cloud.common.gateway.request.RequestStardardHeaders;
import com.smoc.cloud.common.gateway.utils.ValidatorUtil;
import com.smoc.cloud.common.http.server.utils.RedisModel;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.somc.cloud.gateway.redis.service.DataService;
import com.somc.cloud.gateway.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 全局过滤器
 * 拦截非post请求，只允许post请求；
 * 对于post请求进行请求HttpHeaders初步验证
 */
@Slf4j
@Component
public class CustomGlobalGatewayFilter implements GlobalFilter, Ordered {

    @Autowired
    private DataService dataService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        /**
         *  拦截所有非POST请求
         */
        if (!request.getMethod().equals(HttpMethod.POST)) {
            URI uri = request.getURI();
            log.warn("[非法请求][数据]URI:{}", uri.toString());
            return errorHandle(exchange, ResponseCode.REQUEST_LEGAL_ERROR.getCode(), ResponseCode.REQUEST_LEGAL_ERROR.getMessage());
        }

        //HttpHeaders 自定义的headers 组织签名信息
        HttpHeaders headers = request.getHeaders();
        RequestStardardHeaders requestStardardHeaders = new RequestStardardHeaders();
        requestStardardHeaders.setSignatureNonce(headers.getFirst("signature-nonce"));
        requestStardardHeaders.setSignature(headers.getFirst("signature"));
        requestStardardHeaders.setAccount(headers.getFirst("account"));
        //log.info("[HttpHeader][数据]{}",new Gson().toJson(requestStardardHeaders));

        //处理signatureNonce 重放攻击
        boolean replayAttacks = dataService.nonce(requestStardardHeaders.getSignatureNonce());
        if (replayAttacks) {
            return errorHandle(exchange, ResponseCode.NONCE_ERROR.getCode(), ResponseCode.NONCE_ERROR.getMessage());
        }

        //取密钥数据
        RedisModel redisModel = dataService.getHttpServerKey(headers.getFirst("account"));
        /**
         * IP鉴权
         */
        if (!StringUtils.isEmpty(redisModel.getIps())) {
            String ip = IpUtil.getIpAddr(request);
            Pattern ipPattern = Pattern.compile(ip);
            Matcher matcher = ipPattern.matcher(redisModel.getIps());
            if (!matcher.find()) {
                log.info("[IP鉴权]被限制IP：{}", ip);
                return errorHandle(exchange, ResponseCode.REQUEST_IP_ERROR.getCode(), ResponseCode.REQUEST_IP_ERROR.getMessage());
            }
        }

        //提交速率限流
        boolean limiter = dataService.limiter(headers.getFirst("account"));
        if (!limiter) {
            return errorHandle(exchange, ResponseCode.PARAM_SUBMIT_LIMITER_ERROR.getCode(), ResponseCode.PARAM_SUBMIT_LIMITER_ERROR.getMessage());
        }

        //参数规则验证
        if (!ValidatorUtil.validate(requestStardardHeaders)) {
            URI uri = request.getURI();
            log.warn("[非法POST请求][数据]URI:{}", uri.toString());
            return errorHandle(exchange, ResponseCode.REQUEST_LEGAL_ERROR.getCode(), ResponseCode.REQUEST_LEGAL_ERROR.getMessage());
        }

        return chain.filter(exchange);
    }

    public Mono<Void> errorHandle(ServerWebExchange exchange, String errorCode, String errorMessage) {
        //响应信息
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set("Content-Type", "application/json;charset=utf-8");
        ResponseData responseData = ResponseDataUtil.buildError(errorCode, errorMessage);
        log.error("[响应数据]数据:{}", new Gson().toJson(responseData));
        byte[] bytes = new Gson().toJson(responseData).getBytes(StandardCharsets.UTF_8);
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(bodyDataBuffer));
    }

    @Override
    public int getOrder() {
        return -200;
    }

}
