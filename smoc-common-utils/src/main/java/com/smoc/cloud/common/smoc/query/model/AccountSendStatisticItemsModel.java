package com.smoc.cloud.common.smoc.query.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
public class AccountSendStatisticItemsModel {

    private String messageDate;

    private String businessAccount;

    private String channelId;

    private String carrier;

    private String businessType;

    private BigDecimal price;

    private Integer customerSubmitNum;

    private Integer successSubmitNum;

    private Integer failureSubmitNum;

    private Integer messageSuccessNum;

    private Integer messageFailureNum;

    private Integer messageNoReportNum;


}
