package com.smoc.cloud.user.remote;

import com.smoc.cloud.common.auth.validator.UserPasswordValidator;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 用户管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc",path = "/smoc")
public interface WebUserFeignClient {

    /**
     * 重置密码
     *
     * @param userPasswordValidator
     * @return
     */
    @RequestMapping(value = "/enterprise/web/resetOwnPassword", method = RequestMethod.POST)
    ResponseData resetOwnPassword(@RequestBody UserPasswordValidator userPasswordValidator) throws Exception;

}
