package com.smoc.cloud.filters.filter.full_filter;

import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.filters.filter.BaseGatewayFilter;
import com.smoc.cloud.filters.request.model.RequestFullParams;
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
 * NUMBER_高级扩展参数过滤；
 * 这些过滤参数，可以根据参数规则进行配置，可以扩展；具体功能参照文档
 */
@Slf4j
@Component
public class NumberExtendFilterParamsGatewayFilter extends BaseGatewayFilter implements Ordered, GatewayFilter {


    @Autowired
    private FiltersService filtersService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取body内容
        String requestBody = exchange.getAttribute("cachedRequestBodyObject");

        //校验数据请求的数据结构
        RequestFullParams model = new Gson().fromJson(requestBody, RequestFullParams.class);
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPhone())) {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                //被执行后调用 post
            }));
        }

        Map<String, String> result = new HashMap<>();
        result.put("result", "false");

        /**
         * 查询业务账号配置的NUMBER_BLACK_级别配置参数
         */
        Object blackPatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER + "black:" + model.getAccount());
        if (null != blackPatten && !StringUtils.isEmpty(blackPatten.toString())) {
            Boolean validator = filtersService.validator(blackPatten.toString(), model.getPhone());
            if (validator) {
                result.put("result", "true");
                result.put("code", FilterResponseCode.NUMBER_BLACK_FILTER.getCode());
                result.put("message", FilterResponseCode.NUMBER_BLACK_FILTER.getMessage());
            }
            log.info("[号码_黑_过滤参数]{}:{}", model.getAccount(), blackPatten.toString());
        }

        /**
         * 查询业务账号配置的NUMBER_WHITE_级别配置参数 超级洗白
         */
        if ("true".equals(result.get("result"))) {
            Object whitePatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER + "white:" +  model.getAccount());
            if (null != whitePatten && !StringUtils.isEmpty(whitePatten.toString())) {
                Boolean validator = filtersService.validator(whitePatten.toString(), model.getPhone());
                if (validator) {
                    result.put("result", "false");
                }
            }

            log.info("[号码_白_过滤参数]{}:{}", model.getAccount(), new Gson().toJson(whitePatten));
        }

        /**
         * 查询业务账号配置的NUMBER_REGULAR_级别配置参数  满足则通过
         */
        if ("false".equals(result.get("result"))) {
            Object regularPatten = filtersService.get(RedisConstant.FILTERS_CONFIG_ACCOUNT_NUMBER + "regular:" +   model.getAccount());
            if (null != regularPatten && !StringUtils.isEmpty(regularPatten.toString())) {
                if (filtersService.validator(regularPatten.toString(), model.getPhone())) {
                    result.put("result", "false");
                } else {
                    result.put("result", "true");
                    result.put("code", FilterResponseCode.NUMBER_REGULAR_FILTER.getCode());
                    result.put("message", FilterResponseCode.NUMBER_REGULAR_FILTER.getMessage());
                }

                log.info("[正则_正则_过滤参数]{}:{}", model.getAccount(), new Gson().toJson(regularPatten));
            }
        }

        if ("true".equals(result.get("result"))) {
            return errorHandle(exchange, result.get("code"), result.get("message"));
        }

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            //被执行后调用 post
        }));
    }

    @Override
    public int getOrder() {
        return 40;
    }


}
