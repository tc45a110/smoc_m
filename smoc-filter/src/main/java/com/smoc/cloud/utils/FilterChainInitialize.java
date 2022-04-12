package com.smoc.cloud.utils;

import com.smoc.cloud.filters.FilterChain;
import com.smoc.cloud.filters.account.*;
import com.smoc.cloud.filters.carrier.CarrierBlackWordsFilter;
import com.smoc.cloud.filters.carrier.CarrierCheckWordsFilter;
import com.smoc.cloud.filters.carrier.CarrierWhiteWordsFilter;
import com.smoc.cloud.filters.channel.ChannelBlackWordsFilter;
import com.smoc.cloud.filters.channel.ChannelCheckWordsFilter;
import com.smoc.cloud.filters.channel.ChannelWhiteWordsFilter;
import com.smoc.cloud.filters.info.type.InfoTypeBalckWordsFilter;
import com.smoc.cloud.filters.info.type.InfoTypeCheckWordsFilter;
import com.smoc.cloud.filters.info.type.InfoTypeWhiteWordsFilter;
import com.smoc.cloud.filters.system.*;

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
        //系统白词过滤，，必须紧跟在系统黑词过滤之后
        filterChain.addFilter(new SystemWhiteWordsFilter());
        //业务账号黑词过滤
        filterChain.addFilter(new AccountBlackWordsFilter());
        //业务账号白词过滤，必须紧放在业务账号黑词过滤器和系统黑词过滤器后面；
        filterChain.addFilter(new AccountWhiteWordsFilter());
        //运营商黑词过滤器
        filterChain.addFilter(new CarrierBlackWordsFilter());
        //运营商白词过滤器，必须紧跟在运营商黑词过滤器之后
        filterChain.addFilter(new CarrierWhiteWordsFilter());
        //通道黑词过滤器
        filterChain.addFilter(new ChannelBlackWordsFilter());
        //通道白词过滤器，必须紧跟在通道黑词过滤器之后
        filterChain.addFilter(new ChannelWhiteWordsFilter());
        //信息分类黑词过滤器
        filterChain.addFilter(new InfoTypeBalckWordsFilter());
        //信息分类白词过滤器，必须紧跟在信息分类黑词过滤器之后
        filterChain.addFilter(new InfoTypeWhiteWordsFilter());

        /**
         * 业务账号相关过滤器
         */
        //业务账号日限量过滤器
        filterChain.addFilter(new AccountDailyLimitFilter());
        //业务账号发送时间限制
        filterChain.addFilter(new AccountTimeLimitFilter());
        //业务账号，单手机好发送次数限制
        filterChain.addFilter(new AccountPhoneNumberLimitFilter());


        /**
         * 系统级过滤器
         */
        //系统黑名单过滤器
        filterChain.addFilter(new SystemBlackListFilter());
        //系统白名单过滤器，必须紧跟放在系统黑名单过滤器之后，用来尝试洗白黑名单中的手机号
        filterChain.addFilter(new SystemWhiteListFilter());
        //签名频次限制过滤器
        filterChain.addFilter(new SystemSignFilter());


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
