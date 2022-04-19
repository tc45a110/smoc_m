package com.smoc.cloud.statistics.service;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.statistics.remote.StatisticsFeignClient;
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
     * 统计(客户数、活跃数、通道数)
     * @param startDate
     * @param endDate
     * @return
     */
    public ResponseData<Map<String, Object>> statisticsCountData(String startDate, String endDate) {
        try {
            ResponseData<Map<String, Object>> count = statisticsFeignClient.statisticsCountData(startDate,endDate);
            return count;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 短信发送总量、营收总额、充值总额、账户总余额
     * @param year
     * @return
     */
    public ResponseData<Map<String, Object>> statisticsAccountData(String startDate, String endDate) {
        try {
            ResponseData<Map<String, Object>> count = statisticsFeignClient.statisticsAccountData(startDate,endDate);
            return count;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
