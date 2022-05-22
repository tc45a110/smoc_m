package com.smoc.cloud.filters.filter.full_filter;

import com.google.gson.Gson;
import com.smoc.cloud.filters.filter.BaseGatewayFilter;
import com.smoc.cloud.filters.request.model.RequestFullParams;
import com.smoc.cloud.filters.utils.FilterResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全量过滤开始过滤类，进行关键参数检查，这样在后续过滤中就不进行关键参数校验了
 */
@Slf4j
@Component
public class StartFullFilterGatewayFilter extends BaseGatewayFilter implements Ordered, GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取body内容
        String requestBody = "";
        if (HttpMethod.POST.equals(exchange.getRequest().getMethod())) {
            requestBody = exchange.getAttribute("cachedRequestBodyObject");
        }
        //校验数据请求的数据结构
        RequestFullParams model;
        try {
            model = new Gson().fromJson(requestBody, RequestFullParams.class);
        } catch (Exception e) {
            return errorHandle(exchange, FilterResponseCode.PARAM_FORMAT_ERROR.getCode(), FilterResponseCode.PARAM_FORMAT_ERROR.getMessage());
        }

        /**
         * 关键参数校验
         */
        if (StringUtils.isEmpty(model.getAccount()) || StringUtils.isEmpty(model.getPhone()) || StringUtils.isEmpty(model.getMessage())) {
            return errorHandle(exchange, FilterResponseCode.PARAM_FORMAT_ERROR.getCode(), FilterResponseCode.PARAM_FORMAT_ERROR.getMessage());
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
