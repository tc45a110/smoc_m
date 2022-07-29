package com.smoc.cloud.saler.controller;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.CustomerComplaintQo;
import com.smoc.cloud.common.smoc.saler.qo.IndexData;
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
     * 查看首页信息:客户发送总量、客户消费总额
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/index/statisticsSalerIndexData/{startDate}/{endDate}/{salerId}", method = RequestMethod.GET)
    public ResponseData<IndexData> statisticsSalerIndexData(@PathVariable String startDate, @PathVariable String endDate, @PathVariable String salerId) {

        return salerStatisticsService.statisticsSalerIndexData(startDate,endDate,salerId);
    }

    /**
     * 查看首页信息:通道发送总量、通道消费总额
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/index/statisticsSalerIndexChannelData/{startDate}/{endDate}/{salerId}", method = RequestMethod.GET)
    public ResponseData<IndexData> statisticsSalerIndexChannelData(@PathVariable String startDate, @PathVariable String endDate, @PathVariable String salerId) {

        return salerStatisticsService.statisticsSalerIndexChannelData(startDate,endDate,salerId);
    }

    /**
     * 账户余额-后付费和预付费
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/index/statisticsSalerIndexAccountData/{startDate}/{endDate}/{salerId}", method = RequestMethod.GET)
    public ResponseData<Map<String, Object>> statisticsSalerIndexAccountData(@PathVariable String startDate, @PathVariable String endDate, @PathVariable String salerId){

        return salerStatisticsService.statisticsSalerIndexAccountData(startDate,endDate,salerId);
    }

    /**
     * 首页：客户近12个月账号短信发送量统计
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/index/indexStatisticAccountMessageSendSum", method = RequestMethod.POST)
    public ResponseData<List<AccountStatisticSendData>> indexStatisticAccountMessageSendSum(@RequestBody AccountStatisticSendData statisticSendData){

        return salerStatisticsService.indexStatisticAccountMessageSendSum(statisticSendData);
    }

    /**
     * 首页：通道近12个月短信发送量统计
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/index/indexStatisticChannelMessageSendSum", method = RequestMethod.POST)
    public ResponseData<List<AccountStatisticSendData>> indexStatisticChannelMessageSendSum(@RequestBody AccountStatisticSendData statisticSendData){

        return salerStatisticsService.indexStatisticChannelMessageSendSum(statisticSendData);
    }

    /**
     * 首页：客户近12个月投诉率统计
     * @param customerComplaintQo
     * @return
     */
    @RequestMapping(value = "/index/indexStatisticAccountComplaint", method = RequestMethod.POST)
    public ResponseData<List<CustomerComplaintQo>> indexStatisticAccountComplaint(@RequestBody CustomerComplaintQo customerComplaintQo){

        return salerStatisticsService.indexStatisticAccountComplaint(customerComplaintQo);
    }

    /**
     * 首页：通道近12个月投诉率统计
     * @param customerComplaintQo
     * @return
     */
    @RequestMapping(value = "/index/indexStatisticChannelComplaint", method = RequestMethod.POST)
    public ResponseData<List<CustomerComplaintQo>> indexStatisticChannelComplaint(@RequestBody CustomerComplaintQo customerComplaintQo){

        return salerStatisticsService.indexStatisticChannelComplaint(customerComplaintQo);
    }
}
