package com.smoc.cloud.statistics.service;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.qo.StatisticProfitData;
import com.smoc.cloud.common.smoc.message.MessageChannelComplaintValidator;
import com.smoc.cloud.customer.remote.BusinessAccountFeignClient;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.statistics.remote.StatisticsFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 统计
 */
@Slf4j
@Service
public class StatisticsService {

    @Autowired
    private StatisticsFeignClient statisticsFeignClient;
    @Autowired
    private BusinessAccountFeignClient businessAccountFeignClient;

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
     * @param startDate
     * @param endDate
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

    /**
     * 首页:近12个月短信发送量
     * @param statisticSendData
     * @return
     */
    public AccountStatisticSendData statisticSendNumber(AccountStatisticSendData statisticSendData) {
        ResponseData<List<AccountStatisticSendData>> responseData = this.businessAccountFeignClient.statisticAccountSendNumber(statisticSendData);
        List<AccountStatisticSendData> list = responseData.getData();

        //月份
        String[] month = list.stream().map(AccountStatisticSendData::getMonth).toArray(String[]::new);
        //发送量
        BigDecimal[] sendNumber = list.stream().map(AccountStatisticSendData::getSendNumber).toArray(BigDecimal[]::new);

        AccountStatisticSendData accountStatisticSendData = new AccountStatisticSendData();
        accountStatisticSendData.setMonthArray(month);
        accountStatisticSendData.setSendNumberArray(sendNumber);

        //最大值、最小值
        BigDecimal max = new BigDecimal(0);
        BigDecimal min = new BigDecimal(0);
        String maxMonth = "";
        String minMonth = "";
        if(!StringUtils.isEmpty(list) && list.size()>0){
            max = list.get(0).getSendNumber();
            min = list.get(0).getSendNumber();
            for (int i = 0; i < list.size(); i++) {
                AccountStatisticSendData data = list.get(i);
                if (data.getSendNumber().compareTo(max)>0){
                    max=data.getSendNumber();
                    maxMonth = data.getMonth();
                }
                if (data.getSendNumber().compareTo(min)<0){
                    min=data.getSendNumber();
                    minMonth = data.getMonth();
                }
            }
        }

        accountStatisticSendData.setMaxValue(max);
        accountStatisticSendData.setMinxValue(min);
        accountStatisticSendData.setMaxMonth(maxMonth);
        accountStatisticSendData.setMinMonth(minMonth);

        return accountStatisticSendData;
    }

    /**
     * 近12个月营业收入
     *
     * @return
     */
    public StatisticProfitData statisticProfitMonth(StatisticProfitData statisticProfitData) {
        ResponseData<List<StatisticProfitData>> responseData = this.statisticsFeignClient.statisticProfitMonth(statisticProfitData);
        List<StatisticProfitData> list = responseData.getData();

        //月份
        String[] month = list.stream().map(StatisticProfitData::getMonth).toArray(String[]::new);
        //发送量
        BigDecimal[] profit = list.stream().map(StatisticProfitData::getProfit).toArray(BigDecimal[]::new);

        StatisticProfitData statisticData = new StatisticProfitData();
        statisticData.setMonthArray(month);
        statisticData.setProfitArray(profit);

        //最大值、最小值
        BigDecimal max = new BigDecimal(0);
        BigDecimal min = new BigDecimal(0);
        String maxMonth = "";
        String minMonth = "";
        if(!StringUtils.isEmpty(list) && list.size()>0){
            max = list.get(0).getProfit();
            min = list.get(0).getProfit();
            for (int i = 0; i < list.size(); i++) {
                StatisticProfitData data = list.get(i);
                if (data.getProfit().compareTo(max)>0){
                    max=data.getProfit();
                    maxMonth = data.getMonth();
                }
                if (data.getProfit().compareTo(min)<0){
                    min=data.getProfit();
                    minMonth = data.getMonth();
                }
            }
        }

        statisticData.setMaxValue(max);
        statisticData.setMinxValue(min);
        statisticData.setMaxMonth(maxMonth);
        statisticData.setMinMonth(minMonth);

        return statisticData;
    }

    /**
     * 查询通道排行
     * @param messageChannelComplaintValidator
     * @return
     */
    public ResponseData<List<MessageChannelComplaintValidator>> channelComplaintRanking(MessageChannelComplaintValidator messageChannelComplaintValidator) {
        try {
            ResponseData<List<MessageChannelComplaintValidator>> data = statisticsFeignClient.channelComplaintRanking(messageChannelComplaintValidator);
            return data;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
