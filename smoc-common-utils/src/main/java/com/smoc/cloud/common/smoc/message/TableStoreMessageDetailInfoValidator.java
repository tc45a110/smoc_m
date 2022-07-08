package com.smoc.cloud.common.smoc.message;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TableStoreMessageDetailInfoValidator {

    //日志记录时间
    private String mtTime;

    //业务账号ID
    private String businessAccount;

    //业务账号名称
    private String accountName;

    //企业标识
    private String enterpriseFlag;

    //客户提交方式
    private String userSubmitType;

    //客户提交时间
    private String userSubmitTime;

    //业务消息标识
    private String businessMessageFlag;

    //手机号
    private String phoneNumber;

    //号段运营商
    private String segmentCarrier;

    //业务运营商
    private String businessCarrier;

    //区域编码
    private String areaCode;

    //区域中文名称
    private String areaName;

    //地市中文名称
    private String cityName;

    //客户完整接入代码
    private String userSubmitSrcid;

    //平台下发扩展码
    private String extcode;

    //业务代码
    private String businessCode;

    //短信内容或标题
    private String messageContent;

    //签名
    private String signature;

    //计费条数
    private String messageNumber;

    //信息分类
    private String infoType;

    //行业分类：多个行业用&分隔
    private String industryTypes;

    //优先级
    private String accountPriority;

    //接入层协议消息ID
    private String accessMessageId;

    //通道ID
    private String channelId;

    //通道基础接入码
    private String channelSrcid;

    //计费账号
    private String financeAccountId;

    //计费单价
    private String messagePrice;

    //计费总额
    private String messageFee;

    //业务类型
    private String businessType;

    //模板ID
    private String templateId;

    //扣费类型
    private String consumeType;

    //扩展参数
    private String userPayload;

    //价格区域编码
    private String priceAreacode;

    //状态报告时间
    private String mrTime;

    //间隔时间
    private String timeElapsed;

    //完整的状态码数据，长短信会追加状态码
    private String statusCodeExtend;

    private String startDate;
    private String endDate;
}
