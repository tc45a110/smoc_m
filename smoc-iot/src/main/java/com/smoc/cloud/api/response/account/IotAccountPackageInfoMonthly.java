package com.smoc.cloud.api.response.account;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class IotAccountPackageInfoMonthly {

    private String packageId;

    private String packageName;

    private String changingType;

    private String changingCycle;

    private BigDecimal packageChanging;

    private BigDecimal aboveQuotaChanging;

    private BigDecimal packageTempAmount;

    private BigDecimal packageTempAmountFee;

    private BigDecimal cycleFunctionFee;

    private Integer packageCardsNum;

    private BigDecimal packagePoolSize;

    private BigDecimal usedAmount;

    private BigDecimal surplusAmount;

    private String packageMonth;

    private String settlementStatus;
}
