package com.smoc.cloud.statistic.data.remote;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.spss.qo.ManagerStatisticQo;
import com.smoc.cloud.common.smoc.spss.qo.StatisticIncomeQo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "smoc", path = "/smoc")
public interface OperatingStatisticsFeignClient {

    /**
     * 运营净毛利统计
     * @param statisticIncomeQo
     * @return
     */
    @RequestMapping(value = "/spss/income/incomeMonthStatistic", method = RequestMethod.POST)
    ResponseData<List<StatisticIncomeQo>> incomeMonthStatistic(@RequestBody StatisticIncomeQo statisticIncomeQo);

    /**
     * 运营数据月查询:统计每月发送数据
     * @param managerStatisticQo
     * @return
     */
    @RequestMapping(value = "/spss/query/operating/operatingStatisticSendMessageMonth", method = RequestMethod.POST)
    ResponseData<List<ManagerStatisticQo>> operatingStatisticSendMessageMonth(@RequestBody ManagerStatisticQo managerStatisticQo);
}
