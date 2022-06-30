package com.smoc.cloud.scheduler.batch.filters.model;

public class MessageRouteParams {

    /**
     * 业务账号ID
     */
    private String accountId;

    /**
     * 业务账号名称
     */
    private String accountName;

    /**
     * 业务账号优先级
     */
    private String accountPriority;

    /**
     * 计费账号ID
     */
    private String financeAccountId;

    /**
     * 计费单价
     */
    private String messagePrice;

    /**
     * 企业ID
     */
    private String enterpriseFlag;

    /**
     * 接口协议
     */
    private String protocol;

    /**
     * 区域编码：国家编码或省份编码
     */
    private String areaCode;
    /**
     * 区域名称:国家名称或省份名称
     */
    private String areaName;

    /**
     * 价格区域编码
     */
    private String priceAreaCode;

    /**
     * 业务类型：普通短信、多媒体短信、5G短信、国际短信、彩信的编码
     */
    private String businessType;

    /**
     * 扣费方式 1：下发时扣费 2：回执返回时扣费
     */
    private String consumeType;

    /**
     * 账号提交的码号或扩展码
     */
    private String accountSubmitSrcId;

    /**
     * 账号在平台的扩展码包含随机扩展码加上账号自带扩展码
     */
    private String accountExtendCode;

    /**
     * 账号需要状态报告标识，1为需要返回，其他为不需要返回
     */
    private int accountReportFlag = 1;

    /**
     * 批次包含的消息条数,长短信算多条
     */
    private long taskMessageNumber;

    /**
     * 行业分类：多个行业用&分隔
     */
    private String industryTypes;

    /**
     * 信息分类:按照投诉高低分为：行业、会销、拉新、催收
     */
    private String infoType;

    /**
     * 通道ID
     */
    private String channelId;

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 通道单价
     */
    private String channelPrice;

    /**
     * 通道接入码
     */
    private String channelSRCID;
}
