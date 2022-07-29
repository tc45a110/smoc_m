package com.smoc.cloud.saler.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.CustomerComplaintQo;
import com.smoc.cloud.common.smoc.saler.qo.IndexData;
import com.smoc.cloud.saler.repository.SalerStatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
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
    public ResponseData<IndexData> statisticsSalerIndexData(String startDate, String endDate, String salerId) {

        IndexData indexData  =new IndexData();

        //客户消费总额
        Map<String, Object> profit = salerStatisticsRepository.getCustomerConsumeTotal(startDate,endDate,salerId);
        indexData.setAccountConsumeSum(""+profit.get("ACCOUNT_CONSUME_SUM"));
        indexData.setMessageSendTotal(""+profit.get("MESSAGE_TOTAL"));

        return ResponseDataUtil.buildSuccess(indexData);
    }

    /**
     * 查看首页信息:通道发送总量、通道消费总额
     * @param startDate
     * @param endDate
     * @param salerId
     * @return
     */
    public ResponseData<IndexData> statisticsSalerIndexChannelData(String startDate, String endDate, String salerId) {
        IndexData indexData  =new IndexData();

        //客户消费总额
        Map<String, Object> profit = salerStatisticsRepository.getChannelConsumeTotal(startDate,endDate,salerId);
        indexData.setAccountConsumeSum(""+profit.get("CHANNEL_CONSUME_SUM"));
        indexData.setMessageSendTotal(""+profit.get("MESSAGE_TOTAL"));

        return ResponseDataUtil.buildSuccess(indexData);
    }

    /**
     * 账户余额-后付费和预付费
     * @param startDate
     * @param endDate
     * @param salerId
     * @return
     */
    public ResponseData<Map<String, Object>> statisticsSalerIndexAccountData(String startDate, String endDate, String salerId) {
        Map<String, Object> map = new HashMap<>();

        //客户账户总余额（预付费）
        Map<String, Object> usableAccount = salerStatisticsRepository.getCustomerUsableTotal(salerId);
        map.put("ACCOUNT_USABLE_SUM",usableAccount.get("ACCOUNT_USABLE_SUM"));

        //客户账户总余额（后付费）
        Map<String, Object> usableAccountLater = salerStatisticsRepository.getCustomerUsableTotalLater(salerId);
        map.put("ACCOUNT_LATER_USABLE_SUM",usableAccountLater.get("ACCOUNT_LATER_USABLE_SUM"));

        return ResponseDataUtil.buildSuccess(map);
    }

    /**
     * 首页：客户近12个月账号短信发送量统计
     * @param statisticSendData
     * @return
     */
    public ResponseData<List<AccountStatisticSendData>> indexStatisticAccountMessageSendSum(AccountStatisticSendData statisticSendData) {
        List<AccountStatisticSendData> list = salerStatisticsRepository.indexStatisticAccountMessageSendSum(statisticSendData);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 首页：通道近12个月短信发送量统计
     * @param statisticSendData
     * @return
     */
    public ResponseData<List<AccountStatisticSendData>> indexStatisticChannelMessageSendSum(AccountStatisticSendData statisticSendData) {
        List<AccountStatisticSendData> list = salerStatisticsRepository.indexStatisticChannelMessageSendSum(statisticSendData);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 首页：客户近12个月投诉率统计
     * @param customerComplaintQo
     * @return
     */
    public ResponseData<List<CustomerComplaintQo>> indexStatisticAccountComplaint(CustomerComplaintQo customerComplaintQo) {
        List<CustomerComplaintQo> list = salerStatisticsRepository.indexStatisticAccountComplaint(customerComplaintQo);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 首页：通道近12个月投诉率统计
     * @param customerComplaintQo
     * @return
     */
    public ResponseData<List<CustomerComplaintQo>> indexStatisticChannelComplaint(CustomerComplaintQo customerComplaintQo) {
        List<CustomerComplaintQo> list = salerStatisticsRepository.indexStatisticChannelComplaint(customerComplaintQo);
        return ResponseDataUtil.buildSuccess(list);
    }


}
