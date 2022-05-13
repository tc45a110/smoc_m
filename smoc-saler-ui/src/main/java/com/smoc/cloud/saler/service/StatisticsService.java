package com.smoc.cloud.saler.service;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.saler.remote.StatisticsFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 统计
 */
@Slf4j
@Service
public class StatisticsService {

    @Autowired
    private StatisticsFeignClient statisticsFeignClient;

    /**
     * 客户发送总量、客户消费总额、账户总余额
     * @param startDate
     * @param endDate
     * @return
     */
    public ResponseData<Map<String, Object>> statisticsSalerIndexData(String startDate, String endDate,String salerId) {
        try {
            ResponseData<Map<String, Object>> count = statisticsFeignClient.statisticsSalerIndexData(startDate,endDate,salerId);
            return count;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
