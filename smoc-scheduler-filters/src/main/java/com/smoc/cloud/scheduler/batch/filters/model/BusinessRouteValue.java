package com.smoc.cloud.scheduler.batch.filters.model;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class BusinessRouteValue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id     **********
     */
    private Long id;

    /**
     * message 业务账号ID **********
     */
    private String accountId;

    /**
     * message 发送手机号 **********
     */
    private String phoneNumber;

    /**
     * message 消息内容 **********
     */
    private String messageContent;

    /**
     * message 消息格式 **********
     */
    private String messageFormat;

    /**
     * message 账号提交平台响应消息ID,多个用&拼接 **********
     */
    private String accountMessageIds;

    /**
     * message 接口协议 **********
     */
    private String protocol;

    /**
     * message 账号提交的码号或扩展码 **********
     */
    private String accountSubmitSrcId;

    /**
     * message 账号模板id **********
     */
    private String accountTemplateId;

    /**
     * message 账号提交的业务码号 **********
     */
    private String accountBusinessCode;

    /**
     * message 批次包含的手机号数量 **********
     */
    private long taskPhoneNumberNumber;


    /**
     * message 批次包含的消息条数,长短信算多条 **********
     */
    private long taskMessageNumber;

    /**
     * message 账号需要状态报告标识，1为需要返回，其他为不需要返回 **********
     */
    private int accountReportFlag = 1;

    /**
     * message 用于个性化接口规范中的可选参数 **********
     */
    private String optionParam;

    /**
     * message 账号提交信息到达平台时间 **********
     */
    private String accountSubmitTime;

    /**
     * message 本条消息计数条数
     */
    private int messageTotal;

    /**
     * message 消息签名
     */
    private String messageSignature;


    /**
     * phone number 号段运营商:根据号段识别的运营商标识
     */
    private String segmentCarrier;

    /**
     * phone number 区域编码：国家编码或省份编码
     */
    private String areaCode;
    /**
     * phone number 区域名称:国家名称或省份名称
     */
    private String areaName;

    /**
     * phone number 地市名称:
     */
    private String cityName;


    /**
     * account 平台账号配置的运营商
     */
    private String businessCarrier;

    /**
     * 数据状态
     */
    private String statusCode;

    /**
     * 状态提示
     */
    private String statusMessage;


}
