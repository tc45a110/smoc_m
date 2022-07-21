package com.smoc.cloud.saler.controller;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.saler.service.SalerStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * 销售模块统计
 */
@Slf4j
@RestController
@RequestMapping("saler/statistics")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class SalerStatisticsController {

    @Autowired
    private SalerStatisticsService salerStatisticsService;

    /**
     * 客户发送总量、客户消费总额、账户总余额
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/index/statisticsSalerIndexData/{startDate}/{endDate}/{salerId}", method = RequestMethod.GET)
    public ResponseData<Map<String, Object>> statisticsSalerIndexData(@PathVariable String startDate, @PathVariable String endDate, @PathVariable String salerId) {

        return salerStatisticsService.statisticsSalerIndexData(startDate,endDate,salerId);
    }

    /**
     * 首页：客户近12个月账号短信发送量统计
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/index/indexStatisticMessageSendSum", method = RequestMethod.POST)
    public ResponseData<List<AccountStatisticSendData>> indexStatisticMessageSendSum(@RequestBody AccountStatisticSendData statisticSendData){

        return salerStatisticsService.indexStatisticMessageSendSum(statisticSendData);
    }
}
