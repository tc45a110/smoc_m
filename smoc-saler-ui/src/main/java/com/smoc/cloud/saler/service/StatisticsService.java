package com.smoc.cloud.saler.service;


import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.CustomerComplaintQo;
import com.smoc.cloud.common.smoc.saler.qo.IndexData;
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
     * 客户发送总量、客户消费总额
     * @param startDate
     * @param endDate
     * @return
     */
    public ResponseData<IndexData> statisticsSalerIndexData(String startDate, String endDate, String salerId) {
        try {
            ResponseData<IndexData> count = statisticsFeignClient.statisticsSalerIndexData(startDate,endDate,salerId);
            return count;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查看首页信息:通道发送总量、通道消费总额
     * @param startDate
     * @param endDate
     * @param salerId
     * @return
     */
    public ResponseData<IndexData> statisticsSalerIndexChannelData(String startDate, String endDate, String salerId) {
        try {
            ResponseData<IndexData> count = statisticsFeignClient.statisticsSalerIndexChannelData(startDate,endDate,salerId);
            return count;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     *  账户余额-后付费和预付费
     */
    public ResponseData<Map<String, Object>> statisticsSalerIndexAccountData(String startDate, String endDate, String salerId) {
        try {
            ResponseData<Map<String, Object>> count = statisticsFeignClient.statisticsSalerIndexAccountData(startDate,endDate,salerId);
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
    public AccountStatisticSendData indexStatisticAccountMessageSendSum(AccountStatisticSendData statisticSendData) {
        //截至本年发送量
        ResponseData<List<AccountStatisticSendData>> responseData = this.statisticsFeignClient.indexStatisticAccountMessageSendSum(statisticSendData);
        List<AccountStatisticSendData> list = responseData.getData();

        List<AccountStatisticSendData> newList = list.subList(12, 24);
        List<AccountStatisticSendData> listBefore = list.subList(0, 12);

        //月份
        String[] month = newList.stream().map(AccountStatisticSendData::getMonth).toArray(String[]::new);
        //发送量
        BigDecimal[] sendNumber = newList.stream().map(AccountStatisticSendData::getSendNumber).toArray(BigDecimal[]::new);
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

    /**
     * 通道近12个月短信发送量统计
     * @param statisticSendData
     * @return
     */
    public AccountStatisticSendData indexStatisticChannelMessageSendSum(AccountStatisticSendData statisticSendData) {
        //截至本年发送量
        ResponseData<List<AccountStatisticSendData>> responseData = this.statisticsFeignClient.indexStatisticChannelMessageSendSum(statisticSendData);
        List<AccountStatisticSendData> list = responseData.getData();

        List<AccountStatisticSendData> newList = list.subList(12, 24);
        List<AccountStatisticSendData> listBefore = list.subList(0, 12);

        //月份
        String[] month = newList.stream().map(AccountStatisticSendData::getMonth).toArray(String[]::new);
        //发送量
        BigDecimal[] sendNumber = newList.stream().map(AccountStatisticSendData::getSendNumber).toArray(BigDecimal[]::new);
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

    /**
     * 客户近12个月投诉率统计
     * @param customerComplaintQo
     * @return
     */
    public CustomerComplaintQo indexStatisticAccountComplaint(CustomerComplaintQo customerComplaintQo) {
        //截至本年发送量
        ResponseData<List<CustomerComplaintQo>> responseData = this.statisticsFeignClient.indexStatisticAccountComplaint(customerComplaintQo);
        List<CustomerComplaintQo> list = responseData.getData();

        List<CustomerComplaintQo> newList = list.subList(12, 24);
        List<CustomerComplaintQo> listBefore = list.subList(0, 12);

        //月份
        String[] month = newList.stream().map(CustomerComplaintQo::getMonth).toArray(String[]::new);
        //投诉量
        String[] complaintNumber = newList.stream().map(CustomerComplaintQo::getComplaint).toArray(String[]::new);
        //去年投诉量
        String[] complaintNumberBefore = listBefore.stream().map(CustomerComplaintQo::getComplaint).toArray(String[]::new);
        //年份
        String[] year =  {""+ (DateTimeUtils.getNowYear()-1) +"年近12个月",""+ DateTimeUtils.getNowYear() +"年近12个月"};

        CustomerComplaintQo info = new CustomerComplaintQo();
        info.setMonthArray(month);
        info.setComplaintArray(complaintNumber);
        info.setComplaintArrayBefore(complaintNumberBefore);
        info.setYear(year);

        return info;
    }

    /**
     * 通道近12个月投诉率统计
     * @param customerComplaintQo
     * @return
     */
    public CustomerComplaintQo indexStatisticChannelComplaint(CustomerComplaintQo customerComplaintQo) {
        //截至本年发送量
        ResponseData<List<CustomerComplaintQo>> responseData = this.statisticsFeignClient.indexStatisticChannelComplaint(customerComplaintQo);
        List<CustomerComplaintQo> list = responseData.getData();

        List<CustomerComplaintQo> newList = list.subList(12, 24);
        List<CustomerComplaintQo> listBefore = list.subList(0, 12);

        //月份
        String[] month = newList.stream().map(CustomerComplaintQo::getMonth).toArray(String[]::new);
        //投诉量
        String[] complaintNumber = newList.stream().map(CustomerComplaintQo::getComplaint).toArray(String[]::new);
        //去年投诉量
        String[] complaintNumberBefore = listBefore.stream().map(CustomerComplaintQo::getComplaint).toArray(String[]::new);
        //年份
        String[] year =  {""+ (DateTimeUtils.getNowYear()-1) +"年近12个月",""+ DateTimeUtils.getNowYear() +"年近12个月"};

        CustomerComplaintQo info = new CustomerComplaintQo();
        info.setMonthArray(month);
        info.setComplaintArray(complaintNumber);
        info.setComplaintArrayBefore(complaintNumberBefore);
        info.setYear(year);

        return info;
    }


}
