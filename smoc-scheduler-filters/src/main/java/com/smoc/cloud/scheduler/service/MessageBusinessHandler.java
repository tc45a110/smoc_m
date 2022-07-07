package com.smoc.cloud.scheduler.service;

import com.google.gson.Gson;
import com.smoc.cloud.scheduler.batch.filters.model.BusinessRouteValue;
import com.smoc.cloud.scheduler.batch.filters.repository.RouteMessageRepository;
import com.smoc.cloud.scheduler.initialize.Reference;
import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import com.smoc.cloud.scheduler.service.area.AreaService;
import com.smoc.cloud.scheduler.service.carrier.CarrierService;
import com.smoc.cloud.scheduler.service.channel.ChannelService;
import com.smoc.cloud.scheduler.service.filters.FullFilterParamsFilterService;
import com.smoc.cloud.scheduler.service.filters.service.message.ChannelMessageFilter;
import com.smoc.cloud.scheduler.service.finance.FinanceService;
import com.smoc.cloud.scheduler.service.log.LogService;
import com.smoc.cloud.scheduler.tools.utils.FilterResponseCodeConstant;
import com.smoc.cloud.scheduler.tools.utils.InsideStatusCodeConstant;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MessageBusinessHandler {

    @Autowired
    private LogService logService;

    @Autowired
    private FinanceService financeService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private CarrierService carrierService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelMessageFilter channelMessageFilter;

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

        //通过过滤
        List<BusinessRouteValue> successList = new ArrayList<>();
        //需要审核，审核列表
        List<BusinessRouteValue> auditList = new ArrayList<>();
        //没通过过滤，直接生成报告
        List<BusinessRouteValue> errorList = new ArrayList<>();

        /**
         * 批次处理
         */
        for (BusinessRouteValue businessRouteValue : list) {

            /**
             * 业务逻辑-判断短信签名
             */
            if (StringUtils.isEmpty(businessRouteValue.getMessageSignature())) {
                businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.UNSIGNA.name());
                businessRouteValue.setStatusMessage("无短信签名！");
                errorList.add(businessRouteValue);
                logService.error(businessRouteValue);
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
                logService.error(businessRouteValue);
                continue;
            }

            /**
             * 逻辑处理-运营商
             */
            this.carrierService.carrierBusiness(businessRouteValue, accountInfo);
            if (!StringUtils.isEmpty(businessRouteValue.getStatusCode())) {
                errorList.add(businessRouteValue);
                logService.error(businessRouteValue);
                continue;
            }

            /**
             * 业务逻辑-完善省份信息
             */
            this.areaService.provinceBusiness(businessRouteValue, accountInfo);
            if ("00".equals(businessRouteValue.getAreaCode())) {
                businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.SHIELD.name());
                businessRouteValue.setStatusMessage("手机好未解析出对应区域！");
                errorList.add(businessRouteValue);
                logService.error(businessRouteValue);
                continue;
            }

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
                logService.error(businessRouteValue);
                continue;
            }

            /**
             * 账户路由到通道
             */
            String channelId = channelService.accountRouteChannel(businessRouteValue.getAccountId(), businessRouteValue.getSegmentCarrier(), businessRouteValue.getAreaCode());
            if (StringUtils.isEmpty(channelId)) {
                businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.NOROUTE.name());
                businessRouteValue.setStatusMessage("没找到对英的通道！");
                errorList.add(businessRouteValue);
                logService.error(businessRouteValue);
                continue;
            }

            /**
             * 内容路由到通道
             */
            String routeChannelId = channelService.contentRouteChannel(businessRouteValue.getAccountId(), businessRouteValue.getPhoneNumber(), businessRouteValue.getSegmentCarrier(), businessRouteValue.getAreaCode(), businessRouteValue.getMessageContent());
            if (!StringUtils.isEmpty(routeChannelId)) {
                channelId = routeChannelId;
            }

            /**
             * 逻辑处理-通道过滤，优化通道过滤位置
             */
            Map<String, String> channelMessageParamsFilterResult = channelMessageFilter.filter(channelId, businessRouteValue.getMessageContent());
            if (!"false".equals(channelMessageParamsFilterResult.get("result"))) {
                businessRouteValue.setStatusMessage(channelMessageParamsFilterResult.get("message"));
                businessRouteValue.setStatusCode(FilterResponseCodeConstant.mapping(filterResult.get("code")));
                errorList.add(businessRouteValue);
                logService.error(businessRouteValue);
                continue;
            }

            /**
             * 加载数据-获得响应运营商短信单价
             */
            BigDecimal messagePrice = financeService.getAccountMessagePrice(businessRouteValue.getAccountId(), businessRouteValue.getSegmentCarrier(), businessRouteValue.getAreaCode());
            if (null == messagePrice) {
                businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.NOPRICE.name());
                businessRouteValue.setStatusMessage("未找到对应的通道价格！");
                errorList.add(businessRouteValue);
                logService.error(businessRouteValue);
                continue;
            }

            /**
             * 验证账户余额，是否够本次消费
             */
            Boolean checked = financeService.checkingAccountFinance(businessRouteValue.getAccountId(), businessRouteValue.getMessageTotal(), messagePrice);
            if (!checked) {
                businessRouteValue.setStatusCode(InsideStatusCodeConstant.StatusCode.NOMONEY.name());
                businessRouteValue.setStatusMessage("财务账户余额不足！");
                errorList.add(businessRouteValue);
                logService.error(businessRouteValue);
                continue;
            }
            successList.add(businessRouteValue);

        }


        /**
         * 根据过滤结果，进行分业务处理
         */
        if (successList.size() > 0) {//成功通过过滤
            log.info("[数据过滤]条数：{}", successList.size());
            routeMessageRepository.handlerBusinessBatch(successList);
        }

        if (auditList.size() > 0) { //要经过审核
            log.info("[审核过滤]条数：{}", auditList.size());
            routeMessageRepository.generateMessageAudit(auditList);
        }

        if (errorList.size() > 0) {//没有通过过滤,直接生成报告
            //log.info("[没通过过滤]条数：{}", errorList.size());
            //log.info(new Gson().toJson(errorList));
            routeMessageRepository.generateErrorMessageResponse(errorList);
        }

    }


}
