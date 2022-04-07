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

    public FilterService(LoadDataService loadDataService){
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
     * @param phone     手机号
     * @param message   消息内容
     * @return Map<String, String>  返回map.size<0表示通过验证，map.size>0时候，value为每个过滤器约定的含义来进行业务逻辑处理
     */
    public Map<String, String> validateMessage(String channelId, String account, String carrier, String infoType, String phone, String message) {

        //过滤链
        FilterChain filterChain = new FilterChain();

        /**
         * 系统关键字过滤
         */
        filterChain.addFilter(new SystemKeyWordsFilter(this.loadDataService));

        /**
         * 业务账号关键词过滤  必须在系统关键词过滤器后面
         */
        filterChain.addFilter(new AccountKeyWordsFilter(this.loadDataService, account));

        /**
         * 通道关键词过滤
         */
        filterChain.addFilter(new ChannelKeyWordsFilter(this.loadDataService, channelId));

        /**
         * 运营商关键词过滤器
         */
        filterChain.addFilter(new CarrierKeyWordsFilter(this.loadDataService, carrier));

        /**
         * 信息分类关键词过滤器
         */
        filterChain.addFilter(new InfoTypeKeyWordsFilter(this.loadDataService, infoType));


        //filterResult 存放执行完结果，如果filterResult size=0，则执行通过,如果 size>0,可以根据对应返回值，处理对应业务逻辑
        Map<String, String> filterResult = new HashMap<>();
        filterChain.doFilter(phone, message, filterResult, filterChain);

        return filterResult;

    }
}
