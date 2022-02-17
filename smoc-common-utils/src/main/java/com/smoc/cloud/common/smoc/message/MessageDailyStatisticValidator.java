package com.smoc.cloud.common.smoc.message;

import lombok.Getter;
import lombok.Setter;


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

    private String messageDate;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private String updatedTime;


    private String enterpriseName;
    private String startDate;
    private String endDate;
}
