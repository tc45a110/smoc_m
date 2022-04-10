package com.smoc.cloud.filters;

import com.smoc.cloud.filters.model.ParamModel;
import com.smoc.cloud.service.LoadDataService;

import java.util.Map;

/**
 * 自定义过滤器接口
 */
public interface Filter {

    /**
     * 定义接口Filter,具体的过滤规则需要实现这个接口
     *
     * @param params       参数对象
     * @param filterResult map结构，key为失败过滤器的key，value 为每个过滤器约定的 错误类型或内容
     * @param chain        过滤链
     */
    void doFilter(ParamModel params, LoadDataService loadDataService, Map<String, String> filterResult, FilterChain chain);

    /**
     * 获取过滤器的 KEY
     *
     * @return
     */
    String getFilterKey();

}
