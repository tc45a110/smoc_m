package com.smoc.cloud.common.smoc.reconciliation.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ReconciliationExcelModel {

    private String period;

    private String account;

    private String carrier;

    private String type;

    private Long quantity;

    private BigDecimal price;

    private BigDecimal amount;

}
