package com.smoc.cloud.utils;

import com.smoc.cloud.filters.FilterChain;
import com.smoc.cloud.filters.account.AccountDailyLimitFilter;
import com.smoc.cloud.filters.account.AccountTimeLimitFilter;
import com.smoc.cloud.filters.keywords.*;

/**
 * 静态初始化过滤链
 */
public class FilterChainInitialize {

    public static FilterChain filterChain;

    static {
        filterChain = new FilterChain();
        /**
         * 关键字黑词、白词过滤器
         */
        //系统黑词过滤
        filterChain.addFilter(new SystemBlackWordsFilter());
        //系统白词过滤，必须放在系统黑词后面
        filterChain.addFilter(new SystemWhiteWordsFilter());
        //业务账号黑词过滤
        filterChain.addFilter(new AccountBlackWordsFilter());
        //业务账号白词过滤，必须放在业务账号黑词过滤器和系统黑词过滤器后面；
        filterChain.addFilter(new AccountWhiteWordsFilter());
        //运营商黑词过滤器
        filterChain.addFilter(new CarrierBlackWordsFilter());
        //运营商白词过滤器
        filterChain.addFilter(new CarrierWhiteWordsFilter());
        //通道黑词过滤器
        filterChain.addFilter(new ChannelBlackWordsFilter());
        //通道白词过滤器
        filterChain.addFilter(new ChannelWhiteWordsFilter());
        //信息分类黑词过滤器
        filterChain.addFilter(new InfoTypeBalckWordsFilter());
        //信息分类白词过滤器
        filterChain.addFilter(new InfoTypeWhiteWordsFilter());

        /**
         * 业务账号相关过滤器
         */
        //业务账号日限量过滤器
        filterChain.addFilter(new AccountDailyLimitFilter());
        //业务账号发送时间限制
        filterChain.addFilter(new AccountTimeLimitFilter());


        /**
         * 关键字审核词过滤器
         */
        //系统审核词过滤
        filterChain.addFilter(new SystemCheckWordsFilter());
        //业务账号审核词过滤
        filterChain.addFilter(new AccountCheckWordsFilter());
        //运营商审核词过滤器
        filterChain.addFilter(new CarrierCheckWordsFilter());
        //通道审核词过滤
        filterChain.addFilter(new ChannelCheckWordsFilter());
        //信息分类审核词过滤器
        filterChain.addFilter(new InfoTypeCheckWordsFilter());

    }

}
