package com.smoc.cloud.common.smoc.message.model;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class StatisticMessageSendData {

    private String enterpriseId;

    private String businessType;

    private String businessAccount;

    private String protocol;

    private String sign;

    private String carrier;

    //发送量
    private Integer sendNumber;

    //成功数
    private Integer successNumber;

    //失败总数
    private Integer failureNumber;

    //未知总数
    private Integer noReportNumber;

    //成功数
    private int successRate;

    //失败总数
    private int failureRate;

    //未知总数
    private int noReportRate;

    private String createdTime;

    //标识
    private String flag;

    private String startDate;

    private String endDate;

    private String enterpriseFlag;
}
