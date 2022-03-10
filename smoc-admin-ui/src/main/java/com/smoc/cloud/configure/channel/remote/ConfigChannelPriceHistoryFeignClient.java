package com.smoc.cloud.configure.channel.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelPriceHistoryValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "smoc", path = "/smoc")
public interface ConfigChannelPriceHistoryFeignClient {

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/configure/channel/price/history/page", method = RequestMethod.POST)
    ResponseData<PageList<ConfigChannelPriceHistoryValidator>> page(@RequestBody PageParams<ConfigChannelPriceHistoryValidator> pageParams) throws Exception;
}
