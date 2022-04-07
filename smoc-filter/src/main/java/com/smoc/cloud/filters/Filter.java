package com.smoc.cloud.filters;

import java.util.Map;

/**
 * 自定义过滤器接口
 */
public interface Filter {

    /**
     * 过滤器过滤操作
     * @param phone        手机号
     * @param message      短消息内容
     * @param filterResult map结构，key为失败过滤器的key，value 为每个过滤器约定的 错误类型或内容
     * @param chain        过滤链
     */
    void doFilter(String phone, String message, Map<String, String> filterResult, FilterChain chain);

}
