package com.smoc.cloud.common.smoc.spss.qo;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ManagerCarrierStatisticQo {

    private String messageDate;

    private String carrier;

    private BigDecimal carrierData;

    private String[] date;

    private BigDecimal[] cmccArray;

    private BigDecimal[] unicArray;

    private BigDecimal[] telcArray;

    private BigDecimal[] intlArray;

    private String statisticType;
    private String startDate;
    private String endDate;
}
