package com.smoc.cloud.configure.channel.controller;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.configure.channel.service.ChannelPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 通道价格接口
 **/
@Slf4j
@RestController
@RequestMapping("configure/channel")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class ChannelPriceController {

    @Autowired
    private ChannelPriceService channelPriceService;

    /**
     * 根据通道id和区域编号通道价格
     * @param channelPriceValidator
     * @return
     */
    @RequestMapping(value = "/editChannelPrice", method = RequestMethod.POST)
    public ResponseData<Map<String, BigDecimal>> editChannelPrice(@RequestBody ChannelPriceValidator channelPriceValidator) {

        return channelPriceService.editChannelPrice(channelPriceValidator);
    }

    /**
     * 区域计价保存
     * @param channelPriceValidator
     * @param op
     * @return
     */
    @RequestMapping(value = "/savePrice/{op}", method = RequestMethod.POST)
    public ResponseData savePrice(@RequestBody ChannelPriceValidator channelPriceValidator, @PathVariable String op) {

        //保存操作
        ResponseData data = channelPriceService.batchSave(channelPriceValidator, op);

        return data;
    }

    /**
     * 根据通道id和区域查询价格
     * @param channelPriceValidator
     * @return
     */
    @RequestMapping(value = "/findByChannelIdAndAreaCode", method = RequestMethod.POST)
    public ResponseData findByChannelIdAndAreaCode(@RequestBody ChannelPriceValidator channelPriceValidator) {

        ResponseData data = channelPriceService.findByChannelIdAndAreaCode(channelPriceValidator);

        return data;
    }
}
