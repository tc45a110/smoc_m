package com.smoc.cloud.saler.remote;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.CustomerComplaintQo;
import com.smoc.cloud.common.smoc.saler.qo.IndexData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@FeignClient(name = "smoc", path = "/smoc")
public interface StatisticsFeignClient {


    /**
     * 客户发送总量、客户消费总额、账户总余额
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/saler/statistics/index/statisticsSalerIndexData/{startDate}/{endDate}/{salerId}", method = RequestMethod.GET)
    ResponseData<IndexData> statisticsSalerIndexData(@PathVariable String startDate, @PathVariable String endDate, @PathVariable String salerId);

    /**
     * 查看首页信息:通道发送总量、通道消费总额
     * @param startDate
     * @param endDate
     * @param salerId
     * @return
     */
    @RequestMapping(value = "/saler/statistics/index/statisticsSalerIndexChannelData/{startDate}/{endDate}/{salerId}", method = RequestMethod.GET)
    ResponseData<IndexData> statisticsSalerIndexChannelData(@PathVariable String startDate, @PathVariable String endDate, @PathVariable String salerId);

    /**
     * 账户余额-后付费和预付费
     * @param startDate
     * @param endDate
     * @param salerId
     * @return
     */
    @RequestMapping(value = "/saler/statistics/index/statisticsSalerIndexAccountData/{startDate}/{endDate}/{salerId}", method = RequestMethod.GET)
    ResponseData<Map<String, Object>> statisticsSalerIndexAccountData(@PathVariable String startDate, @PathVariable String endDate, @PathVariable String salerId);

    /**
     *  首页：客户近12个月账号短信发送量统计
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/saler/statistics/index/indexStatisticAccountMessageSendSum", method = RequestMethod.POST)
    ResponseData<List<AccountStatisticSendData>> indexStatisticAccountMessageSendSum(@RequestBody AccountStatisticSendData statisticSendData);

    /**
     * 通道近12个月短信发送量统计
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/saler/statistics/index/indexStatisticChannelMessageSendSum", method = RequestMethod.POST)
    ResponseData<List<AccountStatisticSendData>> indexStatisticChannelMessageSendSum(@RequestBody AccountStatisticSendData statisticSendData);

    /**
     * 客户近12个月投诉率统计
     * @param customerComplaintQo
     * @return
     */
    @RequestMapping(value = "/saler/statistics/index/indexStatisticAccountComplaint", method = RequestMethod.POST)
    ResponseData<List<CustomerComplaintQo>> indexStatisticAccountComplaint(@RequestBody CustomerComplaintQo customerComplaintQo);

    /**
     * 通道近12个月投诉率统计
     * @param customerComplaintQo
     * @return
     */
    @RequestMapping(value = "/saler/statistics/index/indexStatisticChannelComplaint", method = RequestMethod.POST)
    ResponseData<List<CustomerComplaintQo>> indexStatisticChannelComplaint(@RequestBody CustomerComplaintQo customerComplaintQo);


}
