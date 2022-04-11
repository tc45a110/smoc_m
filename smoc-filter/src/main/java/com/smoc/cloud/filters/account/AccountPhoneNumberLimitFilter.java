package com.smoc.cloud.filters.account;

import com.smoc.cloud.filters.Filter;
import com.smoc.cloud.filters.FilterChain;
import com.smoc.cloud.filters.utils.Constant;
import com.smoc.cloud.model.ParamModel;
import com.smoc.cloud.service.LoadDataService;

import java.util.Map;
import java.util.logging.Logger;

/**
 * 业务账号，单手机好发送次数限制
 */
public class AccountPhoneNumberLimitFilter implements Filter {

    public static Logger logger = Logger.getLogger(AccountDailyLimitFilter.class.toString());

    public static final String FILTER_KEY = Constant.ACCOUNT_PHONE_NUMBER_LIMIT_FILTER;

    @Override
    public void doFilter(ParamModel params, LoadDataService loadDataService, Map<String, String> filterResult, FilterChain chain) {

        //过滤过程中已出现失败情况，跳过该过滤器
        if (null == filterResult || filterResult.size() > 0 || params == null || null == params.getAccount() || null == params.getPhone() || null == params.getInfoType()) {
            chain.doFilter(params, loadDataService, filterResult, chain);
            return;
        }


    }

    @Override
    public String getFilterKey() {
        return FILTER_KEY;
    }
}
