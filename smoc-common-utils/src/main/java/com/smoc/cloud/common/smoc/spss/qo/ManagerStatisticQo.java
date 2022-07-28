package com.smoc.cloud.common.smoc.spss.qo;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ManagerStatisticQo {

    private String messageDate;

    private BigDecimal sendAmount;

    private BigDecimal incomeAmount;

    private BigDecimal costAmount;

    private BigDecimal profitAmount;

    private String startDate;
    private String endDate;
}
