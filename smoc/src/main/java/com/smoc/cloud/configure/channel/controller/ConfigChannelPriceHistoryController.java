package com.smoc.cloud.configure.channel.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelPriceHistoryValidator;
import com.smoc.cloud.configure.channel.service.ConfigChannelPriceHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

/**
 * 通道价格历史
 **/
@Slf4j
@RestController
@RequestMapping("configure/channel/price/history")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ConfigChannelPriceHistoryController {

    @Autowired
    private ConfigChannelPriceHistoryService channelPriceHistoryService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<ConfigChannelPriceHistoryValidator>> page(@RequestBody PageParams<ConfigChannelPriceHistoryValidator> pageParams) {

        return channelPriceHistoryService.page(pageParams);
    }
}
