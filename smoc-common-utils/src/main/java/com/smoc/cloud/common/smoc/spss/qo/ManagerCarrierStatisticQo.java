package com.smoc.cloud.common.smoc.spss.qo;


import com.smoc.cloud.common.smoc.spss.model.StatisticRatioModel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    private BigDecimal total;

    private String statisticType;
    private String startDate;
    private String endDate;

    List<StatisticRatioModel> ratio = new ArrayList<>();
}
