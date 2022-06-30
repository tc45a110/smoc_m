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
     * message 消息签名
     */
    private String messageSignature;

    /**
     * account 业务账号名称
     */
    private String accountName;


    /**
     * account 业务账号优先级
     */
    private String accountPriority;

    /**
     * account 计费账号ID
     */
    private String financeAccountId;

    /**
     * account 计费单价
     */
    private String messagePrice;

    /**
     * account 业务类型：普通短信、多媒体短信、5G短信、国际短信、彩信的编码
     */
    private String businessType;

    /**
     * account 扣费方式 1：下发时扣费 2：回执返回时扣费
     */
    private String consumeType;

    /**
     * account 账号在平台的扩展码包含随机扩展码加上账号自带扩展码
     */
    private String accountExtendCode;

    /**
     * account 行业分类：多个行业用&分隔
     */
    private String industryTypes;

    /**
     * account 信息分类:按照投诉高低分为：行业、会销、拉新、催收
     */
    private String infoType;

    /**
     * account 企业ID
     */
    private String enterpriseFlag;


    /**
     * phone number 号段运营商:根据号段识别的运营商标识
     */
    private String segmentCarrier;
    /**
     * 业务运营商:平台业务处理的运营商标识
     */
    private String businessCarrier;

    /**
     * 区域编码：国家编码或省份编码
     */
    private String areaCode;
    /**
     * 区域名称:国家名称或省份名称
     */
    private String areaName;

    /**
     * 地市名称:
     */
    private String cityName;


    /**
     * 价格区域编码
     */
    private String priceAreaCode;


    /**
     * 路由标签：MO上行，RP状态报告，MT下行
     */
    private String routeLabel;


    /**
     * 本条消息总条数
     */
    private int messageTotal;

    /**
     * 本条消息当前条数
     */
    private int messageIndex;

    /**
     * 本条信息计费条数
     */
    private String messageNumber;

    /**
     * 计费金额
     */
    private String messageAmount;


    /**
     * 发送批次号
     */
    private String accountTaskId = "0";


    /**
     * 标识一条消息，长短信该值相同
     */
    private String businessMessageId;

    /**
     * 到审核表时间
     */
    private String tableAuditTime;

    /**
     * 到下行通道表时间
     */
    private String tableSubmitTime;

    /**
     * 上行内容
     */
    private String MOContent;

    /**
     * 到下发队列时间
     */
    private String queueSubmitTime;

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

    /**
     * 提交到通道的码号
     */
    private String channelSubmitSRCID;

    /**
     * 提交到通道总条数
     */
    private int channelTotal;

    /**
     * 提交到通道本条序号
     */
    private int channelIndex;

    /**
     * 提交到通道时间
     */
    private String channelSubmitTime;

    /**
     * 提交到通道响应消息ID
     */
    private String channelMessageID;

    /**
     * 是否进入了审核的code
     */
    private int executeCheckCode;

    /**
     * 路由下一个节点的code：0成功；其他代表失败
     */
    private String nextNodeCode;

    /**
     * 路由下一个的节点的错误码
     */
    private String nextNodeErrorCode;

    /**
     * 通道返回状态报告时间
     */
    private String channelReportTime;

    /**
     * 成功码0代表成功2代表失败
     */
    private String successCode;

    /**
     * 状态码
     */
    private String statusCode;

    /**
     * 状态子码
     */
    private String subStatusCode;

    /**
     * 状态码来源0:内部;1:通道提交码;2:通道状态码
     */
    private String statusCodeSource;

    /**
     * 通道返回状态报告的码号，用于当通过消息ID未能匹配到提交记录时，可以通过码号+手机号+通道ID 进行模糊匹配，类似上行。
     */
    private String channelReportSRCID;

    /**
     * 通道上行码号
     */
    private String channelMOSRCID;

    /**
     * 通道模板id
     */
    private String channelTemplateID = "0";

    /**
     * 重复发送次数
     */
    private int repeatSendTimes;


    /**
     * 进入审核原因
     */
    private String auditReason;

    /**
     * 补发通道
     */
    private String channelRepairID;

    //过滤响应码
    private String filterCode;
    //过滤响应提示
    private String filterMessage;

}
