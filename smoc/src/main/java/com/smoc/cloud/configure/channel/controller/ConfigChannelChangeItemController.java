package com.smoc.cloud.configure.channel.controller;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.configure.channel.entity.ConfigChannelChangeItem;
import com.smoc.cloud.configure.channel.service.ConfigChannelChangeItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通道切换明细
 **/
@Slf4j
@RestController
@RequestMapping("configure/channel/change")
public class ConfigChannelChangeItemController {

    @Autowired
    private ConfigChannelChangeItemService configChannelChangeItemService;

    /**
     * 查询列表
     *
     * @param changeId
     * @return
     */
    @RequestMapping(value = "/findChangeItems/{changeId}", method = RequestMethod.POST)
    public ResponseData<List<ConfigChannelChangeItem>> findChangeItems(@PathVariable String changeId) {

        return configChannelChangeItemService.findChangeItems(changeId);
    }

    /**
     * 查询列表
     *
     * @param changeId
     * @return
     */
    @RequestMapping(value = "/findChangeAllItems/{changeId}", method = RequestMethod.POST)
    public ResponseData<List<ConfigChannelChangeItem>> findChangeAllItems(@PathVariable String changeId) {

        return configChannelChangeItemService.findChangeAllItems(changeId);
    }
}
