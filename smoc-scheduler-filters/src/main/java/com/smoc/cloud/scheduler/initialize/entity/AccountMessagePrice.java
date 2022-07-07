package com.smoc.cloud.scheduler.initialize.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AccountMessagePrice {

    /**
     * 业务账号
     */
    private String accountId;
    /**
     * 付费方式 1 预付费 2后付费
     */
    private String payType;
    /**
     * 计费方式  1 下发运营商计费 2 回执成功计费
     */
    private String chargeType;
    /**
     * 运营商类型 1:国内运营商 2:国际运营商
     */
    private String carrierType;
    /**
     * 运营商
     */
    private String carrier;
    /**
     * 运营商单价
     */
    private BigDecimal carrierPrice;
}
