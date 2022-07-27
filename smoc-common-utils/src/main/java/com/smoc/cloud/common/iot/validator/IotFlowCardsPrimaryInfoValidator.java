package com.smoc.cloud.common.iot.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
@Getter
@Setter
public class IotFlowCardsPrimaryInfoValidator {

    private String id;

    private String carrier;

    private String cardType;

    private String orderNum;

    private String msisdn;

    private String imsi;

    private String iccid;

    private String flowPoolId;

    private String changingType;

    private BigDecimal cycleQuota;

    private BigDecimal openCardFee;

    private String activeDate;

    private String openDate;

    private String useStatus;

    private String cardStatus;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;

    private String carrierName;

    private String startDateTime;

    private String endDateTime;
}
