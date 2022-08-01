package com.smoc.cloud.api.response.account;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 账号套餐信息
 */
@Setter
@Getter
public class IotAccountPackageInfo {

    private String id;

    private String packageName;

    private String packageType;

    private BigDecimal packageCharging;

    private BigDecimal packageChargingDiscount;

    private BigDecimal packagePoolSize;

    private String chargingCycle;

    private BigDecimal cycleQuota;

    private BigDecimal aboveQuotaCharging;

    private BigDecimal packageTempAmount;

    private BigDecimal packageTempAmountFee;

    private String isOpenFee;

    private String isFunctionFee;

//    private BigDecimal cycleFunctionFee;

    private Integer warningLevel;

    private Integer packageCardsNum;

    private BigDecimal thisMonthUsedAmount;
    //本月剩余量
    private BigDecimal thisMonthSurplusAmount;

    private BigDecimal lastMonthCarryAmount;

    private String syncDate;

    private String createdTime;

}
