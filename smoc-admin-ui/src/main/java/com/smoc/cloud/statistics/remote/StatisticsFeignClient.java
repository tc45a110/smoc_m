package com.smoc.cloud.statistics.remote;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.StatisticProfitData;
import com.smoc.cloud.common.smoc.index.CheckRemindModel;
import com.smoc.cloud.common.smoc.message.MessageChannelComplaintValidator;
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
     * 统计(客户数、活跃数、通道数)
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/statistics/index/statisticsCountData/{startDate}/{endDate}", method = RequestMethod.GET)
    ResponseData<Map<String, Object>> statisticsCountData(@PathVariable String startDate, @PathVariable String endDate);

    /**
     * 短信发送总量、营收总额、充值总额、账户总余额
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/statistics/index/statisticsAccountData/{startDate}/{endDate}", method = RequestMethod.GET)
    ResponseData<Map<String, Object>> statisticsAccountData(@PathVariable String startDate, @PathVariable String endDate);

    /**
     * 近12个月营业收入
     * @param statisticProfitData
     * @return
     */
    @RequestMapping(value = "/statistics/index/statisticProfitMonth", method = RequestMethod.POST)
    ResponseData<List<StatisticProfitData>> statisticProfitMonth(@RequestBody StatisticProfitData statisticProfitData);

    /**
     * 查询通道排行
     * @param messageChannelComplaintValidator
     * @return
     */
    @RequestMapping(value = "/complaint/channelComplaintRanking", method = RequestMethod.POST)
    ResponseData<List<MessageChannelComplaintValidator>> channelComplaintRanking(@RequestBody MessageChannelComplaintValidator messageChannelComplaintValidator);

    /**
     * 首页:签名资质、web模板、待下发短信提醒
     * @param checkRemindModel
     * @return
     */
    @RequestMapping(value = "/statistics/index/remind/check", method = RequestMethod.POST)
    ResponseData<CheckRemindModel> remindCheck(@RequestBody CheckRemindModel checkRemindModel);
}
