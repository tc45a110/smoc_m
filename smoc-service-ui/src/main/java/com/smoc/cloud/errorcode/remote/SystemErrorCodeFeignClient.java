package com.smoc.cloud.errorcode.remote;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.parameter.SystemErrorCodeValidator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 错误码管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc", path = "/smoc")
public interface SystemErrorCodeFeignClient {

    /**
     * 根据群id查询
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/errorcode/page", method = RequestMethod.POST)
    PageList<SystemErrorCodeValidator> page(@RequestBody PageParams<SystemErrorCodeValidator> pageParams);

}
