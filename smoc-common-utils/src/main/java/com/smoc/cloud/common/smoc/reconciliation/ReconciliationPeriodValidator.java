package com.smoc.cloud.common.smoc.reconciliation;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ReconciliationPeriodValidator {

    private String id;
    private String accountPeriod;
    private String accountPeriodType;
    private String accountPeriodStartDate;
    private String accountPeriodEndDate;
    private String accountPeriodStatus;
    private String businessType;
    private String status;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;
}
