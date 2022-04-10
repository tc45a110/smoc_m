package com.smoc.cloud.filters.account;

import com.smoc.cloud.filters.Filter;
import com.smoc.cloud.filters.FilterChain;
import com.smoc.cloud.filters.model.ParamModel;
import com.smoc.cloud.filters.utils.Constant;
import com.smoc.cloud.service.LoadDataService;

import java.util.Map;

/**
 * 省份过滤器
 * filterResult 操作说明  value 为 black表示，被系统黑词拦截；value为check表示被审核词拦截
 */
public class AccountProvinceLimitFilter implements Filter {

    public static final String FILTER_KEY = Constant.ACCOUNT_PROVINCE_LIMIT_FILTER;

    @Override
    public void doFilter(ParamModel params, LoadDataService loadDataService, Map<String, String> filterResult, FilterChain chain){

    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
