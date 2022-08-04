package com.smoc.cloud.api.response.flow;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class SimFlowUsedMonthlyResponse {


    private String iccid;

    private String packageId;

    private String packageName;

    private String chargingType;

    private BigDecimal openCardFee;

    private BigDecimal cycleFunctionFee;

    private BigDecimal cycleQuota;

    private BigDecimal usedAmount;

    private BigDecimal totalAmount;

    private BigDecimal remainAmount;

    private BigDecimal settlementFee;

    private String usedMonth;

    private String settlementStatus;

    private String createdTime;

}
