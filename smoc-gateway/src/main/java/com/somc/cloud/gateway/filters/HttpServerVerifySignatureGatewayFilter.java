package com.somc.cloud.gateway.filters;

import com.google.gson.Gson;
import com.smoc.cloud.common.gateway.request.HttpServerSignModel;
import com.smoc.cloud.common.gateway.request.RequestStardardHeaders;
import com.smoc.cloud.common.gateway.utils.HMACUtil;
import com.smoc.cloud.common.gateway.utils.ValidatorUtil;
import com.smoc.cloud.common.http.server.utils.Constant;
import com.smoc.cloud.common.redis.RedisModel;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.utils.DES;
import com.somc.cloud.gateway.configuration.GatewayConfigurationProperties;
import com.somc.cloud.gateway.redis.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Bean;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * http短信发送服务，验证签名
 */
@Slf4j
@Component
public class HttpServerVerifySignatureGatewayFilter {


    @Autowired
    private DataService dataService;

    @Autowired
    private GatewayConfigurationProperties gatewayConfigurationProperties;

    /**
     * 验签过滤器
     *
     * @return
     */
    @Bean
    public GatewayFilter httpServerSignatureGatewayFilter() {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

                ServerHttpRequest request = exchange.getRequest();

                //HttpHeaders 自定义的headers 组织签名信息;headers 数据已经经过了非空验证
                HttpHeaders headers = exchange.getRequest().getHeaders();
                String signatureNonce = headers.getFirst("signature-nonce");
                String signature = headers.getFirst("signature");
                String account = headers.getFirst("account");
                RequestStardardHeaders requestHeaderData = new RequestStardardHeaders();
                requestHeaderData.setSignatureNonce(signatureNonce);
                requestHeaderData.setSignature(signature);
                requestHeaderData.setAccount(account);
                //log.info("[接口请求][账户:{}]header数据:{}", account, new Gson().toJson(requestHeaderData));

                //获取body内容
                String requestBody = "";
                if (HttpMethod.POST.equals(exchange.getRequest().getMethod())) {
                    requestBody = exchange.getAttribute("cachedRequestBodyObject");
                    //log.info("[接口请求]请求数据:{}", requestBody);
                }

                //requestBody 为空
                if (StringUtils.isEmpty(requestBody)) {
                    return errorHandle(exchange, ResponseCode.PARAM_ERROR.getCode(), ResponseCode.PARAM_ERROR.getMessage());
                }

                //校验数据请求的数据结构
                HttpServerSignModel model;
                try {
                    model = new Gson().fromJson(requestBody, HttpServerSignModel.class);
                } catch (Exception e) {
                    return errorHandle(exchange, ResponseCode.PARAM_FORMAT_ERROR.getCode(), ResponseCode.PARAM_FORMAT_ERROR.getMessage());
                }

                //header account 与 model account 保持一致
                if (!account.equals(model.getAccount())) {
                    return errorHandle(exchange, ResponseCode.PARAM_FORMAT_ERROR.getCode(), "account数据不一致");
                }

                //身份证规则验证  验证身证号 及姓名
                if (!ValidatorUtil.validate(model)) {
                    String errorMessage = ValidatorUtil.validateMessage(model);
                    return errorHandle(exchange, ResponseCode.PARAM_FORMAT_ERROR.getCode(), errorMessage);
                }

                //取密钥数据
                RedisModel redisModel = dataService.getHttpServerKey(model.getAccount());
                if (null == redisModel || StringUtils.isEmpty(redisModel.getMd5HmacKey())) {
                    return errorHandle(exchange, ResponseCode.USER_NOT_EXIST.getCode(), ResponseCode.USER_NOT_EXIST.getMessage());
                }

                /**
                 * 账号 业务对应关系
                 */
                Map<String, String> businessAccountMap = Constant.BUSINESS_ACCOUNT_MAP;
                URI uri = request.getURI();
                for (Map.Entry<String, String> map : businessAccountMap.entrySet()) {
                    Pattern UrlPattern = Pattern.compile(map.getKey());
                    Matcher matcher = UrlPattern.matcher(uri.toString());
                    if (matcher.find()) {
                        //log.info("[账号、业务类型的映射关系]需要业务类型:{}", map.getValue());
                        if (!map.getValue().equals(redisModel.getBusinessType())) {
                            return errorHandle(exchange, ResponseCode.ACCOUNT_BUSINESS_ERROR.getCode(), ResponseCode.ACCOUNT_BUSINESS_ERROR.getMessage());
                        }
                    }
                }

                //组织签名数据
                String md5HmacKey = DES.decrypt(redisModel.getMd5HmacKey());
                StringBuffer signData = new StringBuffer();
                signData.append(requestHeaderData.getSignatureNonce().trim());
                signData.append(model.getOrderNo().trim());
                signData.append(model.getAccount());
                signData.append(model.getTimestamp().trim());

                //校验签名
                boolean verifySign = HMACUtil.verifySign(signData.toString(), requestHeaderData.getSignature(), md5HmacKey, gatewayConfigurationProperties.getSignStyle());
                if (!verifySign) {
                    return errorHandle(exchange, ResponseCode.SIGN_ERROR.getCode(), ResponseCode.SIGN_ERROR.getMessage());
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
        ResponseData responseData = ResponseDataUtil.buildError(errorCode, errorMessage);
        log.error("[响应数据]数据:{}", new Gson().toJson(responseData));
        byte[] bytes = new Gson().toJson(responseData).getBytes(StandardCharsets.UTF_8);
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(bodyDataBuffer));
    }
}
