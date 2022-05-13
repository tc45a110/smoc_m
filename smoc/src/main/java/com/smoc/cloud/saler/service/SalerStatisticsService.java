package com.smoc.cloud.saler.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.saler.repository.SalerStatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计数据
 */
@Slf4j
@Service
public class SalerStatisticsService {

    @Resource
    private SalerStatisticsRepository salerStatisticsRepository;


    /**
     * 客户发送总量、客户消费总额、客户账户总余额
     * @param startDate
     * @param endDate
     * @return
     */
    public ResponseData<Map<String, Object>> statisticsSalerIndexData(String startDate, String endDate,String salerId) {

        Map<String, Object> map = new HashMap<>();

        //客户短信发送总量
        Long messageSendTotal = salerStatisticsRepository.getMessageSendTotal(startDate,endDate,salerId);
        map.put("MESSAGE_SEND_TOTAL",messageSendTotal);

        //客户账户总余额
        Map<String, Object> usableAccount = salerStatisticsRepository.getCustomerUsableTotal(salerId);
        map.put("ACCOUNT_USABLE_SUM",usableAccount.get("ACCOUNT_USABLE_SUM"));

        //客户消费总额
        Map<String, Object> profit = salerStatisticsRepository.getCustomerConsumeTotal(startDate,endDate,salerId);
        map.put("ACCOUNT_CONSUME_SUM",profit.get("ACCOUNT_CONSUME_SUM"));

        return ResponseDataUtil.buildSuccess(map);
    }

}
