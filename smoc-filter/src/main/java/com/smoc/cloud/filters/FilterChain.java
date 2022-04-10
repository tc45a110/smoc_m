package com.smoc.cloud.filters;

import com.smoc.cloud.filters.model.ParamModel;
import com.smoc.cloud.service.LoadDataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 自定义过滤链
 */
public class FilterChain implements Filter {
    public static Logger logger = Logger.getLogger(FilterChain.class.toString());

    List<Filter> filters = new ArrayList<>();

    public int index = 0;

    public FilterChain addFilter(Filter filter) {
        logger.info("添加过滤器:"+filter.getFilterKey());
        filters.add(filter);
        return this;
    }

    /**
     * 短消息过滤链
     *
     * @param params       参数对象
     * @param filterResult map结构，key为失败过滤器的key
     * @param chain        过滤链
     */
    @Override
    public void doFilter(ParamModel params, LoadDataService loadDataService, Map<String, String> filterResult, FilterChain chain){
        //logger.info("filter index:"+index);
        if (index == filters.size()) return;
        Filter filter = filters.get(index);
        index++;
        filter.doFilter(params,loadDataService, filterResult, chain);
    }

    @Override
    public String getFilterKey() {
        return null;
    }

}
