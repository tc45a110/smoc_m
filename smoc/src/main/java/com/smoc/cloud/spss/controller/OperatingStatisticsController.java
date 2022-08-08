package com.smoc.cloud.spss.controller;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.spss.qo.StatisticIncomeQo;
import com.smoc.cloud.spss.service.OperatingStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 运营数据统计分析
 */
@Slf4j
@RestController
@RequestMapping("spss")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class OperatingStatisticsController {

    @Autowired
    private OperatingStatisticsService operatingStatisticsService;


    /**
     * 运营净毛利统计
     * @param statisticIncomeQo
     * @return
     */
    @RequestMapping(value = "/income/incomeMonthStatistic", method = RequestMethod.POST)
    public ResponseData<List<StatisticIncomeQo>> incomeMonthStatistic(@RequestBody StatisticIncomeQo statisticIncomeQo){

        return operatingStatisticsService.incomeMonthStatistic(statisticIncomeQo);
    }


}
