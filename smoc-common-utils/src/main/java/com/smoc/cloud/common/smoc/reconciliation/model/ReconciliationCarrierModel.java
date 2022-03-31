package com.smoc.cloud.common.smoc.reconciliation.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 对账，运营商对象
 */
@Setter
@Getter
public class ReconciliationCarrierModel {

    //运营商类型
    private String carrierType;

    //价格
    private BigDecimal price;

    //发送量
    private Long sendSum;

    //总金额
    private BigDecimal totalSum;


}
