package com.smoc.cloud.common.smoc.finance.validator;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class FinanceAccountValidator {

    private String accountId;
    private String accountType;
    private BigDecimal accountTotalSum;
    private BigDecimal accountUsableSum;
    private BigDecimal accountFrozenSum;
    private BigDecimal accountConsumeSum;
    private BigDecimal accountRechargeSum;
    private BigDecimal accountCreditSum;
    private String accountStatus;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String enterpriseName;
    //对应两种账号，一个是短信的业务账号，一种是身份认证的账号
    private String accountName;
    private String businessAccount;

    //业务的id，
    private String id;
}
