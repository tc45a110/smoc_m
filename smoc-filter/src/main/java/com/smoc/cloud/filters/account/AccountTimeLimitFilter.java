package com.smoc.cloud.filters.account;

import com.smoc.cloud.filters.Filter;
import com.smoc.cloud.filters.FilterChain;
import com.smoc.cloud.model.ParamModel;
import com.smoc.cloud.filters.utils.Constant;
import com.smoc.cloud.service.LoadDataService;

import java.util.Map;
import java.util.logging.Logger;

/**
 * 业务账号发送时间限制过滤器
 * filterResult 操作说明  value 为 black表示，被系统黑词拦截；value为check表示被审核词拦截
 */
public class AccountTimeLimitFilter implements Filter {

    public static Logger logger = Logger.getLogger(AccountTimeLimitFilter.class.toString());

    public static final String FILTER_KEY = Constant.ACCOUNT_TIME_LIMIT_FILTER;

    @Override
    public void doFilter(ParamModel params,LoadDataService loadDataService, Map<String, String> filterResult, FilterChain chain){
        //过滤过程中已出现失败情况，跳过该过滤器
        if (null == filterResult || filterResult.size() > 0) {
            chain.doFilter(params,loadDataService, filterResult, chain);
            return;
        }

        Boolean status = loadDataService.validateAccountTimeLimit(params.getAccount());
        if (null != status && !status) {
            filterResult.put(Constant.ACCOUNT_TIME_LIMIT_FILTER, "false");
        }

        //logger.info("[Filters]:业务账号日限量过滤");
        chain.doFilter(params,loadDataService, filterResult, chain);
    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
