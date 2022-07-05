package com.smoc.cloud.scheduler.service;

import com.smoc.cloud.common.filters.utils.RedisConstant;
import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.batch.filters.repository.RouteMessageRepository;
import com.smoc.cloud.scheduler.initialize.Reference;
import com.smoc.cloud.scheduler.service.filters.FullFilterParamsFilterService;
import com.smoc.cloud.scheduler.service.filters.service.FiltersRedisDataService;
import com.smoc.cloud.scheduler.service.filters.service.message.ChannelMessageFilter;
import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import com.smoc.cloud.scheduler.tools.utils.FilterResponseCodeConstant;
import com.smoc.cloud.scheduler.tools.utils.InsideStatusCodeConstant;
import com.smoc.cloud.scheduler.tools.utils.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MessageBusinessHandler {

    @Autowired
    private ChannelMessageFilter channelMessageFilter;

    @Autowired
    private FiltersRedisDataService filtersRedisDataService;

    @Autowired
    private RouteMessageRepository routeMessageRepository;

    @Autowired
    private FullFilterParamsFilterService fullFilterParamsFilterService;

    /**
     * 短信业务处理
     *
     * @param list
     */
    public void handleMessageBusiness(List<? extends BusinessRouteValue> list) {

        if (null == list || list.size() < 1) {
            return;
        }

        /**
         * 过滤操作
         */
        //通过过滤
        List<BusinessRouteValue> successList = new ArrayList<>();
        //需要审核，审核列表
        List<BusinessRouteValue> auditList = new ArrayList<>();
        //没通过过滤，直接生成报告
        List<BusinessRouteValue> errorList = new ArrayList<>();

        Integer count = list.size();
        for (BusinessRouteValue businessRouteValue : list) {

            /**
             * 业务逻辑-判断短信签名
             */
            if (StringUtils.isEmpty(businessRouteValue.getMessageSignature())) {
                businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.UNSIGNA.name());
                businessRouteValue.setStatusMessage("无短信签名！");
                errorList.add(businessRouteValue);
                continue;
            }

            /**
             * 加载数据-获取业务账号 account 基本信息
             */
            AccountBaseInfo accountInfo = Reference.accounts.get(businessRouteValue.getAccountId());
            if (null == accountInfo) {
                businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.ILLEGAL.name());
                businessRouteValue.setStatusMessage("无效业务账号！");
                errorList.add(businessRouteValue);
                continue;
            }

            /**
             * 逻辑处理-运营商
             */
            this.carrierBusiness(businessRouteValue, accountInfo);
            if (!StringUtils.isEmpty(businessRouteValue.getStatusCode())) {
                errorList.add(businessRouteValue);
                continue;
            }

            /**
             * 业务逻辑-完善省份信息
             */
            this.provinceBusiness(businessRouteValue, accountInfo);

            /**
             * 账号、号码、内容、区域、限流......
             * 考虑到优化操作，此处通道过滤排除
             */
            Map<String, String> filterResult = fullFilterParamsFilterService.filter(businessRouteValue.getPhoneNumber(), businessRouteValue.getAccountId(), businessRouteValue.getMessageContent(), businessRouteValue.getAccountTemplateId(), businessRouteValue.getBusinessCarrier(), businessRouteValue.getAreaCode(), null);
            if ("1208".equals(filterResult.get("code")) || "1211".equals(filterResult.get("code"))) { //1208、1211表示包含审核词
                businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.AUDIT.name());
                businessRouteValue.setStatusMessage(filterResult.get("message"));
                auditList.add(businessRouteValue);
                continue;
            } else if ("true".equals(filterResult.get("result"))) {//没有通过过滤
                businessRouteValue.setStatusMessage(filterResult.get("message"));
                businessRouteValue.setStatusCode(FilterResponseCodeConstant.mapping(filterResult.get("code")));
                errorList.add(businessRouteValue);
                continue;
            }

            /**
             * 通道路由
             * 根据accountId、segmentCarrier、messageContent
             */


            /**
             * 逻辑处理-通道过滤，优化通道过滤位置
             */
            Map<String, String> channelMessageParamsFilterResult = channelMessageFilter.filter(null, businessRouteValue.getMessageContent());
            if (!"false".equals(channelMessageParamsFilterResult.get("result"))) {
                businessRouteValue.setStatusMessage(channelMessageParamsFilterResult.get("message"));
                businessRouteValue.setStatusCode(FilterResponseCodeConstant.mapping(filterResult.get("code")));
                errorList.add(businessRouteValue);
                continue;
            }


        }
        //log.info("[完成过滤操作]条数：{}:{}", count, System.currentTimeMillis());
        Boolean limiter = filtersRedisDataService.limiterMessageFilter("filter:speed:test", 10000, 10000, 1, count);
        if (!limiter) {
            log.info("[------------过滤触发限流 触发限流-----------------------]");
        }
        /**
         * 根据过滤结果，进行分业务处理
         */
        //成功通过过滤
        if (successList.size() > 0) {
            log.info("[数据过滤]条数：{}", successList.size());
            routeMessageRepository.handlerBusinessBatch(successList);
        }

        //要经过审核
        if (auditList.size() > 0) {
            log.info("[审核过滤]条数：{}", auditList.size());
            routeMessageRepository.generateMessageAudit(auditList);
        }

        //没有通过过滤,直接生成报告
        if (errorList.size() > 0) {
            //log.info("[没通过过滤]条数：{}", errorList.size());
            routeMessageRepository.generateErrorMessageResponse(errorList);
        }

    }

    /**
     * 省份、地域-业务逻辑处理
     *
     * @param businessRouteValue
     * @param messageRouteAccountParams
     */
    private void provinceBusiness(BusinessRouteValue businessRouteValue, AccountBaseInfo messageRouteAccountParams) {

        //如果是国际运营商
        if ("INTL".equals(messageRouteAccountParams.getBusinessCarrier())) {
            String areaName = NumberUtils.getInternationalAreaName(businessRouteValue.getPhoneNumber());
            businessRouteValue.setAreaName(StringUtils.isEmpty(areaName) ? "未知" : areaName);
            String areaCode = "" + NumberUtils.getCountryCode(businessRouteValue.getPhoneNumber());
            businessRouteValue.setAreaCode(StringUtils.isEmpty(areaCode) ? "00" : areaCode);
            return;
        }

        //根据手机号前7位获取国内省份信息
        Object province = filtersRedisDataService.hget(RedisConstant.FILTERS_CONFIG_SYSTEM_PROVINCE_NUMBER, businessRouteValue.getPhoneNumber().substring(0, 7));
        if (StringUtils.isEmpty(province)) {
            businessRouteValue.setAreaCode(province.toString().split("-")[0]);
            businessRouteValue.setAreaName(province.toString().split("-")[1]);
            businessRouteValue.setCityName("未知");
        } else {
            businessRouteValue.setAreaCode("00");
            businessRouteValue.setAreaName("未知");
            businessRouteValue.setCityName("未知");
        }
    }


    /**
     * 运营商-业务逻辑处理
     *
     * @param businessRouteValue
     * @return
     */
    private void carrierBusiness(BusinessRouteValue businessRouteValue, AccountBaseInfo messageRouteAccountParams) {

        Object segmentCarrier;

        //如果业务账号配置的运营商为空
        if (StringUtils.isEmpty(messageRouteAccountParams.getBusinessCarrier())) {
            businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.NOROUTE.name());
            businessRouteValue.setStatusMessage("业务账号运营商为空！");
            return;
        }

        //如果是国际运营商
        if ("INTL".equals(messageRouteAccountParams.getBusinessCarrier())) {
            segmentCarrier = "INTL";
            businessRouteValue.setBusinessCarrier(messageRouteAccountParams.getBusinessCarrier());
            businessRouteValue.setSegmentCarrier(segmentCarrier.toString());
            return;
        }

        /**
         * 根据手机号获取运营商
         */
        segmentCarrier = filtersRedisDataService.hget(RedisConstant.FILTERS_CONFIG_SYSTEM_CARRIER_NUMBER, businessRouteValue.getPhoneNumber().substring(0, 3));
        if (null == segmentCarrier) {
            segmentCarrier = filtersRedisDataService.hget(RedisConstant.FILTERS_CONFIG_SYSTEM_CARRIER_NUMBER, businessRouteValue.getPhoneNumber().substring(0, 4));
        }

        //判断号段运营商及业务账号配置的运营商
        if (StringUtils.isEmpty(segmentCarrier)) {
            businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.NOROUTE.name());
            businessRouteValue.setStatusMessage("号段运营商为空！");
            return;
        }

        //账号配置运营商与号段运营商是否匹配
        if (messageRouteAccountParams.getBusinessCarrier().contains(segmentCarrier.toString())) {
            businessRouteValue.setBusinessCarrier(messageRouteAccountParams.getBusinessCarrier());
            businessRouteValue.setSegmentCarrier(segmentCarrier.toString().split("-")[0]);
            return;
        }
        //是否可以携号转网
        if ("1".equals(messageRouteAccountParams.getTransferType())) {
            businessRouteValue.setBusinessCarrier(messageRouteAccountParams.getBusinessCarrier());
            businessRouteValue.setSegmentCarrier(segmentCarrier.toString().split("-")[0]);
            return;
        }

        businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.INVAREQ.name());
        businessRouteValue.setStatusMessage("运营商-业务逻辑处理，未知错误！");
    }
}
