package com.smoc.cloud.saler.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.saler.qo.ChannelStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.CustomerChannelInfoQo;
import com.smoc.cloud.saler.remote.ChannelFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


/**
 * 通道管理
 */
@Slf4j
@Service
public class ChannelService {

    @Autowired
    private ChannelFeignClient channelFeignClient;

    /**
     * 客户通道列表
     * @param params
     * @return
     */
    public ResponseData<PageList<CustomerChannelInfoQo>> page(PageParams<CustomerChannelInfoQo> params) {
        try {
            ResponseData<PageList<CustomerChannelInfoQo>> page = channelFeignClient.page(params);
            return page;
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询通道信息
     * @param channelId
     * @return
     */
    public ResponseData<ChannelBasicInfoValidator> findChannelById(String channelId) {
        try {
            ResponseData<ChannelBasicInfoValidator> data = this.channelFeignClient.findChannelById(channelId);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 通道发送量统计按月
     * @param statisticSendData
     * @return
     */
    public ChannelStatisticSendData statisticSendNumberMonthByChannel(ChannelStatisticSendData statisticSendData) {
        ResponseData<List<ChannelStatisticSendData>> responseData = this.channelFeignClient.statisticSendNumberMonthByChannel(statisticSendData);
        List<ChannelStatisticSendData> list = responseData.getData();

        //月份
        String[] month = list.stream().map(ChannelStatisticSendData::getMonth).toArray(String[]::new);
        //发送量
        BigDecimal[] sendNumber = list.stream().map(ChannelStatisticSendData::getSendNumber).toArray(BigDecimal[]::new);

        ChannelStatisticSendData channelStatisticSendData = new ChannelStatisticSendData();
        channelStatisticSendData.setMonthArray(month);
        channelStatisticSendData.setSendNumberArray(sendNumber);

        return channelStatisticSendData;
    }

    /**
     * 通道总发送量统计
     * @param statisticSendData
     * @return
     */
    public ChannelStatisticSendData statisticTotalSendNumberByChannel(ChannelStatisticSendData statisticSendData) {
        ResponseData<List<ChannelStatisticSendData>> responseData = this.channelFeignClient.statisticTotalSendNumberByChannel(statisticSendData);
        List<ChannelStatisticSendData> list = responseData.getData();

        //月份
        String[] month = list.stream().map(ChannelStatisticSendData::getMonth).toArray(String[]::new);
        //发送量
        BigDecimal[] sendNumber = list.stream().map(ChannelStatisticSendData::getSendNumber).toArray(BigDecimal[]::new);

        ChannelStatisticSendData channelStatisticSendData = new ChannelStatisticSendData();
        channelStatisticSendData.setMonthArray(month);
        channelStatisticSendData.setSendNumberArray(sendNumber);

        return channelStatisticSendData;
    }
}
