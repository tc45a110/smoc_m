package com.smoc.cloud.statistics.controller;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.StatisticProfitData;
import com.smoc.cloud.statistics.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * 统计首页数据
 */
@Slf4j
@RestController
@RequestMapping("statistics")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 统计(客户数、活跃数、通道数)
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/index/statisticsCountData/{startDate}/{endDate}", method = RequestMethod.GET)
    public ResponseData<Map<String, Object>> statisticsCountData(@PathVariable String startDate, @PathVariable String endDate) {

        return statisticsService.statisticsCountData(startDate,endDate);
    }

    /**
     * 短信发送总量、营收总额、充值总额、账户总余额
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/index/statisticsAccountData/{startDate}/{endDate}", method = RequestMethod.GET)
    public ResponseData<Map<String, Object>> statisticsAccountData(@PathVariable String startDate, @PathVariable String endDate) {

        return statisticsService.statisticsAccountData(startDate,endDate);
    }

    /**
     * 近12个月营业收入
     * @param statisticProfitData
     * @return
     */
    @RequestMapping(value = "/index/statisticProfitMonth", method = RequestMethod.POST)
    public ResponseData<List<StatisticProfitData>> statisticProfitMonth(@RequestBody StatisticProfitData statisticProfitData){

        return statisticsService.statisticProfitMonth(statisticProfitData);
    }
}
