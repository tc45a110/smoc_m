package com.smoc.cloud.saler.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.saler.qo.ChannelStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.CustomerChannelInfoQo;
import com.smoc.cloud.saler.service.SalerChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


/**
 * 销售通道管理
 */
@Slf4j
@RestController
@RequestMapping("saler/channel")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class SalerChannelController {

    @Autowired
    private SalerChannelService salerChannelService;

    /**
     * 通道列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<CustomerChannelInfoQo>> page(@RequestBody PageParams<CustomerChannelInfoQo> pageParams) {

        return salerChannelService.page(pageParams);
    }

    /**
     * 单个通道发送量统计按月
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/statisticSendNumberMonthByChannel", method = RequestMethod.POST)
    public ResponseData<List<ChannelStatisticSendData>> statisticSendNumberMonthByChannel(@RequestBody ChannelStatisticSendData statisticSendData){

        return salerChannelService.statisticSendNumberMonthByChannel(statisticSendData);
    }

    /**
     * 通道总发送量统计
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/statisticTotalSendNumberByChannel", method = RequestMethod.POST)
    public ResponseData<List<ChannelStatisticSendData>> statisticTotalSendNumberByChannel(@RequestBody ChannelStatisticSendData statisticSendData){

        return salerChannelService.statisticTotalSendNumberByChannel(statisticSendData);
    }
}
