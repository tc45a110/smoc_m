package com.smoc.cloud.common.iot.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class IotPackageInfoValidator {

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

    private String  isFunctionFee;

//    private BigDecimal cycleFunctionFee;

    private Integer warningLevel;

    private Integer packageCardsNum;

    private String remark;

    private String useStatus;

    private BigDecimal thisMonthUsedAmount;

    //本月剩余量
    private BigDecimal thisMonthSurplusAmount;

    private BigDecimal lastMonthCarryAmount;

    private String packageStatus;

    private String syncDate;

    private String createdBy;

    private String createdTime;

    private String updatedBy;

    private Date updatedTime;
}
