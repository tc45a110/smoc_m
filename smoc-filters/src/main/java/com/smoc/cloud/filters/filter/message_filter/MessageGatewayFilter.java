package com.smoc.cloud.filters.filter.message_filter;

import com.google.gson.Gson;
import com.smoc.cloud.filters.filter.BaseGatewayFilter;
import com.smoc.cloud.filters.request.model.RequestFullParams;
import com.smoc.cloud.filters.service.FiltersService;
import com.smoc.cloud.filters.utils.DFA.WordsCheckFilter;
import com.smoc.cloud.filters.utils.DFA.FilterInitialize;
import com.smoc.cloud.filters.utils.DFA.WordsSensitiveFilter;
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

        /**
         * 先过滤超级白词,超级白词比较少
         */
        long startSuper = System.currentTimeMillis();
        Boolean isExistSuperWhiteWords = FilterInitialize.superWhiteWordsFilter.isContain(model.getMessage(), 1);
        long endSuper = System.currentTimeMillis();
        log.info("[超级白词过滤]：耗时{}毫秒", endSuper - startSuper);
        if (isExistSuperWhiteWords) {
            return chain.filter(exchange);
        }

        /**
         * 系统敏感词过滤
         */
        long start = System.currentTimeMillis();
        WordsSensitiveFilter sensitiveWordsFilter = FilterInitialize.sensitiveWordsFilter;
        Set<String> sensitiveWords = sensitiveWordsFilter.getSensitiveWords(model.getMessage(), 1);
        long end = System.currentTimeMillis();

        log.info("[敏感词过滤]：耗时{}毫秒", end - start);
        log.info("[敏感词过滤]：{}", sensitiveWords);

        WordsCheckFilter checkWordsFilter = FilterInitialize.checkWordsFilter;
        Set<String> checkWords = checkWordsFilter.getCheckWords(model.getMessage(), 1);
        log.info("[审核词过滤]：{}", checkWords);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 40;
    }


}
