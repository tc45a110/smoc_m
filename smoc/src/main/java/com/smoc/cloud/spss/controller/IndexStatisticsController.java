package com.smoc.cloud.spss.controller;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.spss.service.IndexStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

/**
 * 统计首页数据
 */
@Slf4j
@RestController
@RequestMapping("spss")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IndexStatisticsController {

    @Autowired
    private IndexStatisticsService indexStatisticsService;

    /**
     * 营收总额、毛利总额、发送总量、客户总数、新增客户、通道总数、新增通道
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/index/statisticsIndexData/{startDate}/{endDate}", method = RequestMethod.GET)
    public ResponseData<Map<String, Object>> statisticsIndexData(@PathVariable String startDate, @PathVariable String endDate) {

        return indexStatisticsService.statisticsIndexData(startDate,endDate);
    }

}
