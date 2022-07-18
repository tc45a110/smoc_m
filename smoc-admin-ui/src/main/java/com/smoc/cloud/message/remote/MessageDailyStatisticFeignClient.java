package com.smoc.cloud.message.remote;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import com.smoc.cloud.common.smoc.query.model.AccountSendStatisticItemsModel;
import com.smoc.cloud.common.smoc.query.model.ChannelSendStatisticModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * 短信日统计
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface MessageDailyStatisticFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/message/daily/page", method = RequestMethod.POST)
    ResponseData<PageList<MessageDailyStatisticValidator>> page(@RequestBody PageParams<MessageDailyStatisticValidator> pageParams) throws Exception;

    /**
     * 统计发送数量
     *
     * @param qo
     * @return
     */
    @RequestMapping(value = "/message/daily/count", method = RequestMethod.POST)
    ResponseData<Map<String, Object>> count(@RequestBody MessageDailyStatisticValidator qo) throws Exception;

    /**
     * 查询通道发送量
     * @param params
     * @return
     */
    @RequestMapping(value = "/message/daily/channel/queryChannelSendStatistics", method = RequestMethod.POST)
    ResponseData<PageList<ChannelSendStatisticModel>> queryChannelSendStatistics(@RequestBody PageParams<ChannelSendStatisticModel> params);

    /**
     * 查询通道下面账号发送量
     * @param params
     * @return
     */
    @RequestMapping(value = "/message/daily/channel/accountMessageSendListByChannel", method = RequestMethod.POST)
    ResponseData<PageList<AccountSendStatisticItemsModel>> accountMessageSendListByChannel(PageParams<AccountSendStatisticItemsModel> params);
}
