package com.smoc.cloud.common.smoc.customer.qo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AccountStatisticSendData {

    private String accountId;

    private int index;

    private String month;

    private BigDecimal sendNumber;

    private String[] monthArray;

    private BigDecimal[] sendNumberArray;

    private BigDecimal[] sendNumberArrayBefore;

    //统计维度
    private String dimension;
    private String startDate;
    private String endDate;

    private BigDecimal maxValue;
    private BigDecimal minxValue;

    private String maxMonth;
    private String minMonth;

    private String[] year;

    private String saler;
    private String enterpriseName;

}
