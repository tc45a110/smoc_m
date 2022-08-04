package com.smoc.cloud.statistic.data.remote;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.spss.qo.ManagerCarrierStatisticQo;
import com.smoc.cloud.common.smoc.spss.qo.ManagerStatisticQo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "smoc", path = "/smoc")
public interface ManagerStatisticsFeignClient {

    /**
     * 运营管理综合日统计
     * @param managerStatisticQo
     * @return
     */
    @RequestMapping(value = "/spss/daily/managerDailyStatistic", method = RequestMethod.POST)
    ResponseData<List<ManagerStatisticQo>> managerDailyStatistic(@RequestBody ManagerStatisticQo managerStatisticQo);

    /**
     * 运营管理综合月统计
     * @param managerStatisticQo
     * @return
     */
    @RequestMapping(value = "/spss/month/managerMonthStatistic", method = RequestMethod.POST)
    ResponseData<List<ManagerStatisticQo>> managerMonthStatistic(@RequestBody ManagerStatisticQo managerStatisticQo);

    /**
     * 运营管理运营商按月分类统计
     * @param managerCarrierStatisticQo
     * @return
     */
    @RequestMapping(value = "/spss/carrier/month/managerCarrierMonthStatistic", method = RequestMethod.POST)
    ResponseData<List<ManagerCarrierStatisticQo>> managerCarrierMonthStatistic(@RequestBody ManagerCarrierStatisticQo managerCarrierStatisticQo);
}
