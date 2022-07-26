package com.smoc.cloud.api.response.account;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 账号套餐信息
 */
@Setter
@Getter
public class IotAccountPackageInfo {

    private String id;

    private String packageName;

    private String packageType;

    private BigDecimal packageChanging;

    private BigDecimal packagePoolSize;

    private String changingCycle;

    private BigDecimal cycleQuota;

    private BigDecimal aboveQuotaChanging;

    private BigDecimal packageTempAmount;

    private BigDecimal packageTempAmountFee;

    private BigDecimal cycleFunctionFee;

    private Integer warningLevel;

    private Integer packageCardsNum;

    private BigDecimal thisMonthUsedAmount;
    //本月剩余量
    private BigDecimal thisMonthSurplusAmount;

    private BigDecimal lastMonthCarryAmount;

    private String syncDate;

    private String createdTime;

}
