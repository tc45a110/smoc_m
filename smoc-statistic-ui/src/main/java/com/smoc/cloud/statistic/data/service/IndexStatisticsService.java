package com.smoc.cloud.statistic.data.service;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.statistic.data.remote.IndexStatisticsFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 首页数据统计
 */
@Slf4j
@Service
public class IndexStatisticsService {

    @Autowired
    private IndexStatisticsFeignClient indexStatisticsFeignClient;

    /**
     * 营收总额、毛利总额、发送总量、客户总数、新增客户、通道总数、新增通道
     * @param startDate
     * @param endDate
     * @return
     */
    public ResponseData<Map<String, Object>> statisticsIndexData(String startDate, String endDate) {
        try {
            ResponseData<Map<String, Object>> count = indexStatisticsFeignClient.statisticsIndexData(startDate,endDate);
            return count;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
