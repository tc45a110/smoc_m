package com.smoc.cloud.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自定义过滤链
 */
public class FilterChain implements Filter {

    List<Filter> filters = new ArrayList<>();

    int index = 0;

    public FilterChain addFilter(Filter filter) {
        filters.add(filter);
        return this;
    }

    /**
     * 短消息过滤器
     *
     * @param message      短消息内容
     * @param filterResult map结构，key为失败过滤器的key
     * @param chain        过滤链
     */
    @Override
    public void doFilter(String phone, String message, Map<String, String> filterResult, FilterChain chain) {

        if (index == filters.size()) return;
        Filter filter = filters.get(index);
        index++;
        filter.doFilter(phone, message, filterResult, chain);
    }


}
