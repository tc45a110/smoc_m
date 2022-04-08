package com.smoc.cloud.service;

import com.smoc.cloud.filters.FilterChain;
import com.smoc.cloud.filters.keywords.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息发送验证服务
 */
public class FilterService {

    //过滤基础数据接口
    private LoadDataService loadDataService;

    public FilterService(LoadDataService loadDataService) {
        this.loadDataService = loadDataService;
    }

    /**
     * 消息发送过滤验证
     * 描述：完成对消息内容进行系统关键字、账号关键字、通道关键字、运营商关键字、信息分类关键字进行黑词、审核词、白词进行校验
     *
     * @param channelId 通道id
     * @param account   业务账号
     * @param carrier   运营商 CMCC、UNIC、TELC、INTL
     * @param infoType  信息分类 INDUSTRY、MARKETING、NEW、COLLECTION
     * @param province  省份
     * @param phone     手机号
     * @param message   消息内容
     * @return Map<String, String>  返回map.size<0表示通过验证，map.size>0时候，value为每个过滤器约定的含义来进行业务逻辑处理
     */
    public Map<String, String> validateMessage(String channelId, String account, String carrier, String infoType, String province, String phone, String message) {

        //过滤链
        FilterChain filterChain = new FilterChain();

        /**
         * 关键字黑词、白词过滤器
         */
        //系统黑词过滤
        filterChain.addFilter(new SystemBlackWordsFilter(this.loadDataService, account));
        //系统白词过滤，必须放在系统黑词后面
        filterChain.addFilter(new SystemWhiteWordsFilter(this.loadDataService, account));
        //业务账号黑词过滤
        filterChain.addFilter(new AccountBlackWordsFilter(this.loadDataService, account));
        //业务账号白词过滤，必须放在业务账号黑词过滤器和系统黑词过滤器后面；
        filterChain.addFilter(new AccountWhiteWordsFilter(this.loadDataService, account));
        //运营商黑词过滤器
        filterChain.addFilter(new CarrierBlackWordsFilter(this.loadDataService, carrier));
        //运营商白词过滤器
        filterChain.addFilter(new CarrierWhiteWordsFilter(this.loadDataService, carrier));
        //通道黑词过滤器
        filterChain.addFilter(new ChannelBlackWordsFilter(this.loadDataService, channelId));
        //通道白词过滤器
        filterChain.addFilter(new ChannelWhiteWordsFilter(this.loadDataService, channelId));
        //信息分类黑词过滤器
        filterChain.addFilter(new InfoTypeBalckWordsFilter(this.loadDataService, infoType));
        //信息分类白词过滤器
        filterChain.addFilter(new InfoTypeWhiteWordsFilter(this.loadDataService, infoType));


        /**
         * 关键字审核词过滤器
         */
        //系统审核词过滤
        filterChain.addFilter(new SystemCheckWordsFilter(this.loadDataService, account));
        //业务账号审核词过滤
        filterChain.addFilter(new AccountCheckWordsFilter(this.loadDataService, account));
        //运营商审核词过滤器
        filterChain.addFilter(new CarrierCheckWordsFilter(this.loadDataService, carrier));
        //通道审核词过滤
        filterChain.addFilter(new ChannelCheckWordsFilter(this.loadDataService, channelId));
        //信息分类审核词过滤器
        filterChain.addFilter(new InfoTypeCheckWordsFilter(this.loadDataService, infoType));


        //filterResult 存放执行完结果，如果filterResult size=0，则执行通过,如果 size>0,可以根据对应返回值，处理对应业务逻辑
        Map<String, String> filterResult = new HashMap<>();
        filterChain.doFilter(phone, message, filterResult, filterChain);

        return filterResult;

    }
}
