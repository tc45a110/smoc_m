package com.smoc.cloud.common.smoc.spss.qo;

import com.smoc.cloud.common.smoc.spss.model.StatisticRatioModel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class StatisticIncomeQo {


    private int year;

    private String month;

    private BigDecimal income;

    /**
     * 月份
     */
    private String[] monthArray;

    /**
     * 收入
     */
    private BigDecimal[] incomeArray;

    /**
     * 起点
     */
    private BigDecimal[] startImcomeArray;

    private BigDecimal totalImcome;

    /**
     * 占比
     */
    private List<StatisticRatioModel> ratios = new ArrayList<>();
}
