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
