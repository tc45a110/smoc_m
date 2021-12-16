package com.smoc.cloud.configure.channel.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * 通道价格管理远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface ChannelPriceFeignClient {


    /**
     * 根据通道id和区域编号通道价格
     * @param channelPriceValidator
     * @return
     */
    @RequestMapping(value = "/configure/channel/editChannelPrice", method = RequestMethod.POST)
    ResponseData<Map<String, BigDecimal>> editChannelPrice(@RequestBody ChannelPriceValidator channelPriceValidator) throws Exception;

    /**
     * 区域计价保存
     * @param channelPriceValidator
     * @param op
     * @return
     */
    @RequestMapping(value = "/configure/channel/savePrice/{op}", method = RequestMethod.POST)
    ResponseData savePrice(@RequestBody ChannelPriceValidator channelPriceValidator, @PathVariable String op) throws Exception;

    /**
     * 根据通道id和区域查询价格
     * @param channelPriceValidator
     * @return
     */
    @RequestMapping(value = "/configure/channel/findByChannelIdAndAreaCode", method = RequestMethod.POST)
    ResponseData<List<ChannelPriceValidator>> findByChannelIdAndAreaCode(@RequestBody ChannelPriceValidator channelPriceValidator) throws Exception;
}
