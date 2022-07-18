package com.smoc.cloud.message.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import com.smoc.cloud.common.smoc.query.model.AccountSendStatisticItemsModel;
import com.smoc.cloud.common.smoc.query.model.ChannelSendStatisticModel;
import com.smoc.cloud.message.service.MessageDailyStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

/**
 * 短信日统计
 */
@Slf4j
@RestController
@RequestMapping("message/daily")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MessageDailyStatisticController {

    @Autowired
    private MessageDailyStatisticService messageDailyStatisticService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<MessageDailyStatisticValidator>> page(@RequestBody PageParams<MessageDailyStatisticValidator> pageParams) {

        return messageDailyStatisticService.page(pageParams);
    }

    /**
     * 统计发送数量
     *
     * @param qo
     * @return
     */
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    public ResponseData<Map<String, Object>> count(@RequestBody MessageDailyStatisticValidator qo) {

        return messageDailyStatisticService.countSum(qo);
    }

    /**
     * 查询通道发送量
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/channel/queryChannelSendStatistics", method = RequestMethod.POST)
    public ResponseData<PageList<ChannelSendStatisticModel>> queryChannelSendStatistics(@RequestBody PageParams<ChannelSendStatisticModel> params) {

        return messageDailyStatisticService.queryChannelSendStatistics(params);
    }

    /**
     * 查询通道下面账号发送量
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/channel/accountMessageSendListByChannel", method = RequestMethod.POST)
    public ResponseData<PageList<AccountSendStatisticItemsModel>> accountMessageSendListByChannel(@RequestBody PageParams<AccountSendStatisticItemsModel> params) {

        return messageDailyStatisticService.accountMessageSendListByChannel(params);
    }
}
