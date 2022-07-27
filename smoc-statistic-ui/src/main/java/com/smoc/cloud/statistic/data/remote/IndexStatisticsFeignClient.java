package com.smoc.cloud.statistic.data.remote;


import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "smoc", path = "/smoc")
public interface IndexStatisticsFeignClient {


    /**
     * 营收总额、毛利总额、发送总量、客户总数、新增客户、通道总数、新增通道
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/spss/index/statisticsIndexData/{startDate}/{endDate}", method = RequestMethod.GET)
    ResponseData<Map<String, Object>> statisticsIndexData(@PathVariable String startDate, @PathVariable String endDate);


}
