package com.smoc.cloud.common.smoc.identification.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class IdentificationOrdersInfoValidator {

    private String id;
    private String identificationAccount;
    private String orderNo;
    private String orderType;
    private BigDecimal identificationPrice;
    private String identificationPriceStatus;
    private String identificationOrderNo;
    private String identificationStatus;
    private String identificationMessage;
    private BigDecimal costPrice;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private String updatedTime;

    private String enterpriseName;
}
