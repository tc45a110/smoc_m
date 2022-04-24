package com.smoc.cloud.statistics.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.StatisticProfitData;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.finance.repository.FinanceAccountRechargeRepository;
import com.smoc.cloud.statistics.repository.IndexStatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计首页数据
 */
@Slf4j
@Service
public class StatisticsService {

    @Resource
    private IndexStatisticsRepository indexStatisticsRepository;

    @Resource
    private FinanceAccountRechargeRepository financeAccountRechargeRepository;

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
        Map<String, Object> profit = indexStatisticsRepository.getProfitSum(startDate,endDate);
        map.put("PROFIT_SUM",profit.get("PROFIT_SUM"));

        //充值总额
        FinanceAccountRechargeValidator qo = new FinanceAccountRechargeValidator();
        qo.setStartDate(startDate);
        qo.setEndDate(endDate);
        Map<String, Object> recharge = financeAccountRechargeRepository.countRechargeSum(qo);
        map.put("RECHARGE_SUM",recharge.get("RECHARGE_SUM"));

        //账户总余额
        Map<String, Object> usableAccount = indexStatisticsRepository.getCountUsableAccount();
        map.put("ACCOUNT_USABLE_SUM",usableAccount.get("ACCOUNT_USABLE_SUM"));

        return ResponseDataUtil.buildSuccess(map);
    }

    /**
     * 近12个月营业收入
     * @param statisticProfitData
     * @return
     */
    public ResponseData<List<StatisticProfitData>> statisticProfitMonth(StatisticProfitData statisticProfitData) {

        List<StatisticProfitData> list = indexStatisticsRepository.statisticProfitMonth(statisticProfitData);
        return ResponseDataUtil.buildSuccess(list);
    }
}
