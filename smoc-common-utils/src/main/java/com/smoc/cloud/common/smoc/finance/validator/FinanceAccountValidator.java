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
    //对应两种账号，一个是短信的业务账号，一种是身份认证的账号
    private String accountName;
    private BigDecimal accountTotalSum;
    private BigDecimal accountUsableSum;
    private BigDecimal accountFrozenSum;
    private BigDecimal accountConsumeSum;
    private BigDecimal accountRechargeSum;
    private BigDecimal accountCreditSum;
    private String accountStatus;
    private String isShare;
    private String shareId;
    private String createdBy;
    private String createdTime;
    private String updatedBy;
    private Date updatedTime;

    private String enterpriseName;
    private String businessAccount;

    //企业ID
    private String enterpriseId;

    //下面是创建财务共享账户的参数
    //是否可用余额归集
    private String isUsableSumPool;
    //是否冻结金额归集
    private String isFreezeSumPool;

    //合并账户ids
    private String accountIds;
}
