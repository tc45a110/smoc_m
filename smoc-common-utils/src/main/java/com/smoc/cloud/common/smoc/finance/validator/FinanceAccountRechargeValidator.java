package com.smoc.cloud.common.smoc.finance.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class FinanceAccountRechargeValidator {

    private String id;
    private String accountId;
    private String rechargeFlowNo;
    private String rechargeSource;
    private BigDecimal rechargeSum;
    private BigDecimal rechargeCost;
    private BigDecimal rechargeAccountUsable;
    private String createdBy;
    private String createdTime;

    private String enterpriseName;
}
