package com.smoc.cloud.statistics.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.statistics.repository.IndexStatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计首页数据
 */
@Slf4j
@Service
public class StatisticsService {

    @Resource
    private IndexStatisticsRepository indexStatisticsRepository;

    /**
     * 统计(客户数、活跃数、通道数)
     * @param startDate
     * @param endDate
     * @return
     */
    public ResponseData<Map<String, Object>> statisticsCountData(String startDate, String endDate) {

        Map<String, Object> map = new HashMap<>();

        //所有客户数
        Long totalAccount = indexStatisticsRepository.getAccountCount();
        map.put("TOTAL_ACCOUNT",totalAccount);

        //活跃账户数
        Long activeAccount = indexStatisticsRepository.getActiveAccount(startDate,endDate);
        map.put("ACTIVE_ACCOUNT",activeAccount);

        //活跃通道数
        Long activeChannel = indexStatisticsRepository.getActiveChannel(startDate,endDate);
        map.put("ACTIVE_CHANNEL",activeChannel);

        return ResponseDataUtil.buildSuccess(map);
    }

    /**
     * 短信发送总量、营收总额、充值总额、账户总余额
     * @param startDate
     * @param endDate
     * @return
     */
    public ResponseData<Map<String, Object>> statisticsAccountData(String startDate, String endDate) {

        Map<String, Object> map = new HashMap<>();

        //短信发送总量
        Long messageSendTotal = indexStatisticsRepository.getMessageSendTotal(startDate,endDate);
        map.put("MESSAGE_SEND_TOTAL",messageSendTotal);

        //营收总额

        //充值总额

        //账户总余额

        return ResponseDataUtil.buildSuccess(map);
    }
}
