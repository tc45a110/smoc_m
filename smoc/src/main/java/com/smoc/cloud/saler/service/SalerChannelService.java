package com.smoc.cloud.saler.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.saler.qo.ChannelStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.CustomerChannelInfoQo;
import com.smoc.cloud.saler.repository.SalerChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通道管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SalerChannelService {

    @Resource
    private SalerChannelRepository salerChannelRepository;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<CustomerChannelInfoQo>> page(PageParams<CustomerChannelInfoQo> pageParams) {

        PageList<CustomerChannelInfoQo> list = salerChannelRepository.page(pageParams);
        return ResponseDataUtil.buildSuccess(list);
    }

    /**
     * 单个通道发送量统计按月
     * @param statisticSendData
     * @return
     */
    public ResponseData<List<ChannelStatisticSendData>> statisticSendNumberMonthByChannel(ChannelStatisticSendData statisticSendData) {
        List<ChannelStatisticSendData> list = salerChannelRepository.statisticSendNumberMonthByChannel(statisticSendData);
        return ResponseDataUtil.buildSuccess(list);
    }
}
