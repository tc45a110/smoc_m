package com.smoc.cloud.filters.account;

import com.smoc.cloud.filters.Filter;
import com.smoc.cloud.filters.FilterChain;
import com.smoc.cloud.filters.utils.Constant;
import com.smoc.cloud.service.LoadDataService;

import java.util.Map;
import java.util.logging.Logger;

public class AccountTimeLimitFilter implements Filter {

    public static Logger logger = Logger.getLogger(AccountTimeLimitFilter.class.toString());

    private LoadDataService loadDataService;

    //业务账号
    private String account;

    //日限量返回状态
    private Boolean status;

    public AccountTimeLimitFilter(LoadDataService loadDataService, String account) {
        this.loadDataService = loadDataService;
        this.account = account;
    }

    @Override
    public void doFilter(String phone, String message, Map<String, String> filterResult, FilterChain chain) {
        //过滤过程中已出现失败情况，跳过该过滤器
        if (null == filterResult || filterResult.size() > 0) {
            chain.doFilter(phone, message, filterResult, chain);
            return;
        }

        this.status = this.loadDataService.validateAccountTimeLimit(account);
        if (null != this.status && !this.status) {
            filterResult.put(Constant.ACCOUNT_TIME_LIMIT_FILTER, "false");
        }

        logger.info("[Filters]:业务账号日限量过滤");
        chain.doFilter(phone, message, filterResult, chain);
    }
}
