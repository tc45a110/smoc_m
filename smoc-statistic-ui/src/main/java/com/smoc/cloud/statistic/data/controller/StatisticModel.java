package com.smoc.cloud.statistic.data.controller;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class StatisticModel {

    private String[] date;

    private Integer[] sendAmount;

    private BigDecimal[] incomeAmount;

    private BigDecimal[] costAmount;

    private BigDecimal[] profitAmount;

    private String startDate;
}
