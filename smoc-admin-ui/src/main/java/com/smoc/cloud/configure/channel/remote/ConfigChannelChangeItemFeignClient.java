package com.smoc.cloud.configure.channel.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelChangeItemValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 通道切换明细
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface ConfigChannelChangeItemFeignClient {


    /**
     * 查询列表
     *
     * @param changeId
     * @return
     */
    @RequestMapping(value = "/configure/channel/change/findChangeItems/{changeId}", method = RequestMethod.POST)
    ResponseData<List<ConfigChannelChangeItemValidator>> findChangeItems(@PathVariable String changeId) throws Exception;

    /**
     * 查询列表
     *
     * @param changeId
     * @return
     */
    @RequestMapping(value = "/configure/channel/change/findChangeAllItems/{changeId}", method = RequestMethod.POST)
    ResponseData<List<ConfigChannelChangeItemValidator>> findChangeAllItems(@PathVariable String changeId) throws Exception;
}
