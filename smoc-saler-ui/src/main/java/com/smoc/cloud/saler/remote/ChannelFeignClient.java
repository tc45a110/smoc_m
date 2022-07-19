package com.smoc.cloud.saler.remote;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.saler.qo.ChannelStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.CustomerChannelInfoQo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(name = "smoc", path = "/smoc")
public interface ChannelFeignClient {

    /**
     * 客户通道列表
     * @param params
     * @return
     */
    @RequestMapping(value = "/saler/channel/page", method = RequestMethod.POST)
    ResponseData<PageList<CustomerChannelInfoQo>> page(@RequestBody PageParams<CustomerChannelInfoQo> params);

    /**
     * 查询通道信息
     * @param channelId
     * @return
     */
    @RequestMapping(value = "/configure/channel/findById/{channelId}", method = RequestMethod.GET)
    ResponseData<ChannelBasicInfoValidator> findChannelById(@PathVariable String channelId);

    /**
     * 通道发送量统计按月
     * @param statisticSendData
     * @return
     */
    @RequestMapping(value = "/saler/channel/statisticSendNumberMonthByChannel", method = RequestMethod.POST)
    ResponseData<List<ChannelStatisticSendData>> statisticSendNumberMonthByChannel(@RequestBody ChannelStatisticSendData statisticSendData);
}
