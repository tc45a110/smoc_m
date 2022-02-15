package com.smoc.cloud.common.smoc.finance.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class FinanceAccountShareDetailValidator {

    private String id;
    private String shareAccountId;
    private String accountId;
    private String isUsableSumPool;
    private String isFreezeSumPool;
    private BigDecimal usableSumPool;
    private BigDecimal freezeSumPool;
    private String shareStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;
}
