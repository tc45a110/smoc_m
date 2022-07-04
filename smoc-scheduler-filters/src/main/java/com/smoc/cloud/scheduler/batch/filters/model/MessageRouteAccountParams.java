package com.smoc.cloud.scheduler.batch.filters.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 通过业务账号、运营商要查询的业务账号信息
 */
@Setter
@Getter
public class MessageRouteAccountParams {

    /**
     * 业务账号
     */
    private String accountId;

    /**
     * account 业务账号名称
     */
    private String accountName;


    /**
     * account 企业ID
     */
    private String enterpriseFlag;


    /**
     * account 业务类型：普通短信、多媒体短信、5G短信、国际短信、彩信的编码
     */
    private String businessType;

    /**
     * account 行业分类：多个行业用&分隔
     */
    private String industryTypes;

    /**
     * account 业务账号优先级
     */
    private String accountPriority;


    /**
     * account 计费方式 1：下发时扣费 2：回执返回时扣费
     */
    private String consumeType;

    /**
     * account 信息分类:按照投诉高低分为：行业、会销、拉新、催收
     */
    private String infoType;

    /**
     * account 平台账号配置的运营商
     */
    private String businessCarrier;

    /**
     * account 是否支持携号转网
     */
    private String transferType;


    /**
     * account 计费账号ID
     */
    private String financeAccountId;

    /**
     * account 计费单价  key 是运营商，value 是价格
     */
    private Map<String,Double> messagePrice;

    /**
     * account 账号在平台的扩展码包含随机扩展码加上账号自带扩展码
     */
    private String accountExtendCode;



}
