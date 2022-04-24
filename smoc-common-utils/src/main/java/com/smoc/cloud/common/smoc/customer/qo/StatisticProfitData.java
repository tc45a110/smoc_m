package com.smoc.cloud.common.smoc.customer.qo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class StatisticProfitData {

    private String month;

    private BigDecimal profit;

    private String[] monthArray;

    private BigDecimal[] profitArray;

    private String startDate;
    private String endDate;

    private BigDecimal maxValue;
    private BigDecimal minxValue;
    private String maxMonth;
    private String minMonth;
}
