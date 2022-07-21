package com.smoc.cloud.saler.service;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.saler.remote.StatisticsFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
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

    /**
     * 首页：客户近12个月账号短信发送量统计
     * @param statisticSendData
     * @return
     */
    public AccountStatisticSendData indexStatisticMessageSendSum(AccountStatisticSendData statisticSendData) {
        //截至本年发送量
        statisticSendData.setDimension("1");
        ResponseData<List<AccountStatisticSendData>> responseData = this.statisticsFeignClient.indexStatisticMessageSendSum(statisticSendData);
        List<AccountStatisticSendData> list = responseData.getData();

        //同比去年发送量
        statisticSendData.setDimension("2");
        ResponseData<List<AccountStatisticSendData>> responseDataBefore = this.statisticsFeignClient.indexStatisticMessageSendSum(statisticSendData);
        List<AccountStatisticSendData> listBefore = responseDataBefore.getData();

        //月份
        String[] month = list.stream().map(AccountStatisticSendData::getMonth).toArray(String[]::new);
        //发送量
        BigDecimal[] sendNumber = list.stream().map(AccountStatisticSendData::getSendNumber).toArray(BigDecimal[]::new);
        //去年发送量
        BigDecimal[] sendNumberBefore = listBefore.stream().map(AccountStatisticSendData::getSendNumber).toArray(BigDecimal[]::new);
        //年份
        String[] year =  {""+ (DateTimeUtils.getNowYear()-1) +"年近12个月",""+ DateTimeUtils.getNowYear() +"年近12个月"};

        AccountStatisticSendData accountStatisticSendData = new AccountStatisticSendData();
        accountStatisticSendData.setMonthArray(month);
        accountStatisticSendData.setSendNumberArray(sendNumber);
        accountStatisticSendData.setSendNumberArrayBefore(sendNumberBefore);
        accountStatisticSendData.setYear(year);

        return accountStatisticSendData;
    }
}
