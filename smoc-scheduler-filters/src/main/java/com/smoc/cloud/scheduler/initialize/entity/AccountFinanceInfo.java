package com.smoc.cloud.scheduler.initialize.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AccountFinanceInfo {

    /**
     * 业务账号
     */
    private String accountId;

    /**
     * 可用余额
     */
    private BigDecimal accountUsableSum;

    /**
     * 授信金额
     */
    private BigDecimal accountCreditSum;

    /**
     * 是否共享账户有
     */
    private String isShare;

    /**
     * 共享账户id
     */
    private String shareId;
}
