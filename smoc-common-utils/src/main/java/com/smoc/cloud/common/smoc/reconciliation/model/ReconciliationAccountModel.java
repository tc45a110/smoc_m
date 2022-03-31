package com.smoc.cloud.common.smoc.reconciliation.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
/**
 * 对账，业务账号对象
 */
@Setter
@Getter
public class ReconciliationAccountModel {

    //账期
    private String accountingPeriod;

    //业务账号
    private String account;

    private String accountName;

    //运营商
    private String carrier;

    //业务类型
    private String businessType;

    //发送量
    private Long sendSum;

    //价格
    private BigDecimal price;

    //总金额
    private BigDecimal totalSum;

    //付费方式
    private String payType;

    //运营商
    //private List<ReconciliationCarrierModel> carriers;
}
