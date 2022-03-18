package com.smoc.cloud.statistics.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 统计短信远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface StatisticsMessageFeignClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/message/detail/servicerPage", method = RequestMethod.POST)
    ResponseData<PageList<MessageDetailInfoValidator>> page(@RequestBody PageParams<MessageDetailInfoValidator> pageParams) throws Exception;

}
