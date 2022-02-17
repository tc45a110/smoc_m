package com.smoc.cloud.message.remote;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
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
}
