package com.smoc.cloud.common.smoc.finance.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class FinanceAccountRefundValidator {

    private String id;
    private String accountId;
    private String refundFlowNo;
    private String refundSource;
    private BigDecimal refundSum;
    private BigDecimal refundCost;
    private BigDecimal refundAccountUsable;
    private String createdBy;
    private String createdTime;
    private String remark;

    private String enterpriseName;
    private String enterpriseId;

    private String businessType;
    private String accountName;

    private String startDate;
    private String endDate;
}
