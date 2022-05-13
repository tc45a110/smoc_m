package com.smoc.cloud.saler.remote;


import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    ResponseData<Map<String, Object>> statisticsSalerIndexData(@PathVariable String startDate, @PathVariable String endDate,@PathVariable String salerId);

}
