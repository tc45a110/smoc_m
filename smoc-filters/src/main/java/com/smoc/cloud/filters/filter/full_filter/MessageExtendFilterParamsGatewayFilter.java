package com.smoc.cloud.filters.filter.full_filter;

import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.filters.filter.BaseGatewayFilter;
import com.smoc.cloud.filters.request.model.RequestFullParams;
import com.smoc.cloud.filters.service.FiltersRedisDataService;
import com.smoc.cloud.filters.service.FiltersService;
import com.smoc.cloud.filters.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


/**
 * MESSAGE_高级扩展参数过滤；
 * 这些过滤参数，可以根据参数规则进行配置，可以扩展；具体功能参照文档
 */
@Slf4j
@Component
public class MessageExtendFilterParamsGatewayFilter extends BaseGatewayFilter implements Ordered, GatewayFilter {


    @Autowired
    private FiltersService filtersService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取body内容
        String requestBody = exchange.getAttribute("cachedRequestBodyObject");
        //校验数据请求的数据结构
        RequestFullParams model = new Gson().fromJson(requestBody, RequestFullParams.class);
        //Long start = System.currentTimeMillis();
        Map<String, String> result = new HashMap<>();
        result.put("result", "false");
        /**
         * 查询业务账号配置的MESSAGE_BLACK_级别配置参数
         */
        Object blackPatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE + "black:" + model.getAccount());
        if (!StringUtils.isEmpty(blackPatten)) {
            //log.info("[内容_敏感词_扩展参数]{}:{}", model.getAccount(), new Gson().toJson(blackPatten));
            Boolean validator = filtersService.validator(blackPatten.toString(), model.getMessage());
            if (validator) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.MESSAGE_SENSITIVE_EXTEND_FILTER.getCode());
                result.put("message", FilterResponseCode.MESSAGE_SENSITIVE_EXTEND_FILTER.getMessage());
            }
        }

        /**
         * 查询业务账号配置的MESSAGE_WHITE_级别配置参数
         */
        if ("true".equals(result.get("result"))) {
            Object whitePatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE + "white:" + model.getAccount());
            if (!StringUtils.isEmpty(whitePatten)) {
                //log.info("[内容_白词_扩展参数]{}:{}", model.getAccount(), new Gson().toJson(whitePatten));
                if (null != whitePatten && !StringUtils.isEmpty(whitePatten.toString())) {
                    Boolean validator = filtersService.validator(whitePatten.toString(), model.getMessage());
                    if (validator) {
                        result.put("result", "false");
                    }
                }
            }
        }

        /**
         * 查询业务账号配置的MESSAGE_REGULAR_级别配置参数
         */
        if ("false".equals(result.get("result"))) {
            Object regularPatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_MESSAGE + "regular:" + model.getAccount());
            if (!StringUtils.isEmpty(regularPatten)) {
                //log.info("[内容_正则_扩展参数]{}:{}", model.getAccount(), new Gson().toJson(regularPatten));
                if (filtersService.validator(regularPatten.toString(), model.getMessage())) {
                    result.put("result", "false");
                } else {
                    result.put("result", "true");
                    result.put("code", FilterResponseCode.MESSAGE_REGULAR_FILTER.getCode());
                    result.put("message", FilterResponseCode.MESSAGE_REGULAR_FILTER.getMessage());
                }
            }
        }
        //Long end = System.currentTimeMillis();
        //log.info("[内容_扩展参数]耗时:{}毫秒", end -start);
        if ("true".equals(result.get("result"))) {
            return errorHandle(exchange, result.get("code"), result.get("message"));
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 40;
    }




}
