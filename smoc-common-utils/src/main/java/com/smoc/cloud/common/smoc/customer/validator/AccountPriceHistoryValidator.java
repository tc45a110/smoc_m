package com.smoc.cloud.common.smoc.customer.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountPriceHistoryValidator {

    private String id;
    private String sourceId;
    private String accountId;
    private String carrierType;
    private String carrier;
    private BigDecimal carrierPrice;
    private String priceDate;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private String updatedTime;

    private String startDate;
    private String endDate;
}
