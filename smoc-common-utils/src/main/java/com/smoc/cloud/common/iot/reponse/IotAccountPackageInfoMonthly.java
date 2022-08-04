package com.smoc.cloud.common.iot.reponse;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class IotAccountPackageInfoMonthly {

    private String packageId;

    private String packageName;

    private String chargingType;

    private String chargingCycle;

    private BigDecimal packageCharging;

    private BigDecimal aboveQuotaCharging;

    private BigDecimal packageTempAmount;

    private BigDecimal packageTempAmountFee;

    private String isFunctionFee;

    private BigDecimal cycleFunctionFee;

    private Integer packageCardsNum;

    private BigDecimal packagePoolSize;

    private BigDecimal usedAmount;

    private BigDecimal surplusAmount;

    private BigDecimal settlementFee;

    private String packageMonth;

    private String settlementStatus;

    private String dataStatus;

}
