package com.smoc.cloud.filters.system;

import com.smoc.cloud.filters.Filter;
import com.smoc.cloud.filters.FilterChain;
import com.smoc.cloud.filters.utils.Constant;
import com.smoc.cloud.model.ParamModel;
import com.smoc.cloud.service.LoadDataService;

import java.util.Map;
import java.util.logging.Logger;

/**
 * 系统签名单手机号发送频次限制过滤器
 */
public class SystemSignFilter implements Filter {

    public static Logger logger = Logger.getLogger(SystemBlackWordsFilter.class.toString());

    //系统黑词过滤器
    public static final String FILTER_KEY = Constant.SYSTEM_SIGN_FILTER;

    @Override
    public void doFilter(ParamModel params, LoadDataService loadDataService, Map<String, String> filterResult, FilterChain chain) {

        //过滤过程中已出现失败情况，跳过该过滤器
        if (null == filterResult || filterResult.size() > 0 || params == null || null == params.getSign() || null != params.getAccount()) {
            chain.doFilter(params, loadDataService, filterResult, chain);
            return;
        }

        Boolean status = loadDataService.validateSignLimit(params.getAccount(), params.getSign());
        if (null != status && !status) {
            filterResult.put(FILTER_KEY, "false");
        }

        //logger.info("[Filters]:签名单手机号发送频次限制过滤器");
        chain.doFilter(params, loadDataService, filterResult, chain);

    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
