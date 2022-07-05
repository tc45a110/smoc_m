package com.smoc.cloud.scheduler.initialize.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务账号信息
 */
@Setter
@Getter
public class AccountBaseInfo {

    /**
     * 业务账号
     */
    private String accountId;

    /**
     * account 业务账号名称
     */
    private String accountName;


    /**
     * account 企业标识
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
     * account 业务账号配置扩展吗
     */
    private String accountExtendCode;


}
