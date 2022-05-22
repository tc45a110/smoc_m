package com.smoc.cloud.filters.filter.message_filter;

import com.google.gson.Gson;
import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.filters.filter.BaseGatewayFilter;
import com.smoc.cloud.filters.request.model.RequestFullParams;
import com.smoc.cloud.filters.service.FiltersService;
import com.smoc.cloud.filters.service.message.FilterInitialize;
import com.smoc.cloud.filters.service.message.SensitiveWordsFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Set;


/**
 * 短信内容过滤，只过滤系统敏感词
 */
@Slf4j
@Component
public class MessageGatewayFilter extends BaseGatewayFilter implements Ordered, GatewayFilter {


    @Autowired
    private FiltersService filtersService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取body内容
        String requestBody = exchange.getAttribute("cachedRequestBodyObject");

        //校验数据请求的数据结构
        RequestFullParams model = new Gson().fromJson(requestBody, RequestFullParams.class);
        if (StringUtils.isEmpty(model.getMessage())) {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                //被执行后调用 post
            }));
        }

        SensitiveWordsFilter sensitiveWordsFilter = FilterInitialize.sensitiveWordsFilter;
        Set<String> sensitiveWords = sensitiveWordsFilter.getSensitiveWords(model.getMessage(), 1);
        //log.info("[过滤出来的敏感词]：{}", sensitiveWords);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 40;
    }


}
