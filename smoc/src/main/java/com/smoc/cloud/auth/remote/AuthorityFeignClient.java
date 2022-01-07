package com.smoc.cloud.auth.remote;

import com.smoc.cloud.common.auth.validator.OrgValidator;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 组织管理远程服务接口
 *
 **/
@FeignClient(name = "smoc-auth", path = "/auth")
public interface AuthorityFeignClient {


    /**
     * 组织机构添加、修改
     *
     * @param orgValidator
     * @return
     */
    @RequestMapping(value = "/authority/org/save/{op}", method = RequestMethod.POST)
    ResponseData saveOrg(@RequestBody OrgValidator orgValidator, @PathVariable String op) throws Exception;


    /**
     * 用户保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/authority/user/save/{op}", method = RequestMethod.POST)
    ResponseData saveUser(@RequestBody UserValidator userValidator, @PathVariable String op) throws Exception;

}
