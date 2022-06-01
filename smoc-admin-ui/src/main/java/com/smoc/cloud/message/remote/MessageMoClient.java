package com.smoc.cloud.message.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.MessageMoInfoValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 短信上行远程服务接口
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface MessageMoClient {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/message/mo/page", method = RequestMethod.POST)
    ResponseData<PageList<MessageMoInfoValidator>> page(@RequestBody PageParams<MessageMoInfoValidator> pageParams) throws Exception;

}
