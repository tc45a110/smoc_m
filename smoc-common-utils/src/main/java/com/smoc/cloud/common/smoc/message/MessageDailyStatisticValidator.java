package com.smoc.cloud.common.smoc.message;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class MessageDailyStatisticValidator {

    private String id;

    private String businessAccount;

    private String channelId;

    private String areaCode;

    private String priceAreaCode;

    private String carrier;

    private String businessType;

    private Integer customerSubmitNum;

    private Integer successSubmitNum;

    private Integer failureSubmitNum;

    private Integer messageSuccessNum;

    private Integer messageFailureNum;

    private Integer messageNoReportNum;

    private Date messageDate;

    private String createdBy;

    private Date createdTime;

    private String updatedBy;

    private Date updatedTime;
}
