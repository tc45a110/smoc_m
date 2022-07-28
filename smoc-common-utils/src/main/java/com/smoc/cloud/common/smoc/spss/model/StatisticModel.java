package com.smoc.cloud.common.smoc.spss.model;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class StatisticModel {

    private String[] date;

    private BigDecimal[] sendAmount;

    private BigDecimal[] incomeAmount;

    private BigDecimal[] costAmount;

    private BigDecimal[] profitAmount;

}
