package com.smoc.cloud.message.remote;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.message.model.RequestFullParams;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 过滤服务远程服务接口
 *
 **/
@FeignClient(name = "smoc-filters", path = "/smoc-filters")
public interface FilterServiceFeignClient {


    /**
     * 调用过滤服务
     *
     * @return
     */
    @RequestMapping(value = "/full-filter", method = RequestMethod.POST)
    ResponseData messageFilter(@RequestBody RequestFullParams model);


}
