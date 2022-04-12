package com.smoc.cloud.filters.system;

import com.smoc.cloud.filters.Filter;
import com.smoc.cloud.filters.FilterChain;
import com.smoc.cloud.filters.utils.Constant;
import com.smoc.cloud.model.ParamModel;
import com.smoc.cloud.service.LoadDataService;

import java.util.Map;

/**
 * 系统白名单过滤器
 */
public class SystemWhiteListFilter implements Filter {

    //系统黑词过滤器
    public static final String FILTER_KEY = Constant.SYSTEM_WHITE_LIST_FILTER;

    @Override
    public void doFilter(ParamModel params, LoadDataService loadDataService, Map<String, String> filterResult, FilterChain chain) {
        //过滤结果中，没有黑名单数据，则不用继续执行白名单过滤器
        if ((!"true".equals(filterResult.get(Constant.SYSTEM_BLACK_LIST_FILTER))) || params == null || null == params.getPhone()) {
            chain.doFilter(params, loadDataService, filterResult, chain);
            return;
        }

        //该手机好在黑名单中
        if ("true".equals(filterResult.get(Constant.SYSTEM_BLACK_LIST_FILTER))) {
            Boolean isExistWhiteList = loadDataService.isExistSystemWhiteList(params.getPhone());
            //同时存在白名单中
            if (null != isExistWhiteList && isExistWhiteList) {
                filterResult.remove(Constant.SYSTEM_BLACK_LIST_FILTER);
            }
        }

        //logger.info("[Filters]:利用白名单，洗白黑名单");
        chain.doFilter(params, loadDataService, filterResult, chain);


    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
