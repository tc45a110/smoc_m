package com.smoc.cloud.spss.controller;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.spss.qo.ManagerCarrierStatisticQo;
import com.smoc.cloud.common.smoc.spss.qo.ManagerStatisticQo;
import com.smoc.cloud.spss.service.ManagerStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 运营数据统计分析
 */
@Slf4j
@RestController
@RequestMapping("spss")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ManagerStatisticsController {

    @Autowired
    private ManagerStatisticsService managerStatisticsService;

    /**
     * 运营管理综合日统计
     * @param managerStatisticQo
     * @return
     */
    @RequestMapping(value = "/daily/managerDailyStatistic", method = RequestMethod.POST)
    public ResponseData<List<ManagerStatisticQo>> managerDailyStatistic(@RequestBody ManagerStatisticQo managerStatisticQo){

        return managerStatisticsService.managerDailyStatistic(managerStatisticQo);
    }

    /**
     * 运营管理综合月统计
     * @param managerStatisticQo
     * @return
     */
    @RequestMapping(value = "/month/managerMonthStatistic", method = RequestMethod.POST)
    public ResponseData<List<ManagerStatisticQo>> managerMonthStatistic(@RequestBody ManagerStatisticQo managerStatisticQo){

        return managerStatisticsService.managerMonthStatistic(managerStatisticQo);
    }

    /**
     * 运营管理运营商按月分类统计
     * @param managerCarrierStatisticQo
     * @return
     */
    @RequestMapping(value = "/carrier/month/managerCarrierMonthStatistic", method = RequestMethod.POST)
    public ResponseData<List<ManagerCarrierStatisticQo>> managerCarrierMonthStatistic(@RequestBody ManagerCarrierStatisticQo managerCarrierStatisticQo){

        return managerStatisticsService.managerCarrierMonthStatistic(managerCarrierStatisticQo);
    }

}
