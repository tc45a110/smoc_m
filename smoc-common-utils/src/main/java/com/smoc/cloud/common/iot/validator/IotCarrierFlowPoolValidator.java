package com.smoc.cloud.common.iot.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class IotCarrierFlowPoolValidator {

    private String id;

    private String carrierId;

    private String poolName;

    private Integer poolCardNumber;

    private BigDecimal poolSize;

    private String syncDate;

    private String continueType;

    private String poolStatus;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;
}
