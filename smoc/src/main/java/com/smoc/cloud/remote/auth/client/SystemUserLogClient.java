package com.smoc.cloud.remote.auth.client;

import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户操作日志
 */
@FeignClient(name = "smoc-auth", path = "/auth")
public interface SystemUserLogClient {

    /**
     * 保存信息
     */
    @RequestMapping(value = "/user/logs/save", method = RequestMethod.POST)
    ResponseData save(@RequestBody SystemUserLogValidator systemUserLogValidator) throws Exception;

}
