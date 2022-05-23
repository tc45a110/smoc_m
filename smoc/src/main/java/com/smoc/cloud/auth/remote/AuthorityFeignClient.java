package com.smoc.cloud.auth.remote;

import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.OrgValidator;
import com.smoc.cloud.common.auth.validator.UserPasswordValidator;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.ServiceAuthInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

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

    /**
     * 根据ID查询用户信息
     */
    @RequestMapping(value = "/authority/user/findById/{id}", method = RequestMethod.GET)
    ResponseData findById(@PathVariable String op) throws Exception;

    /**
     * 用户重置密码
     * @param userPasswordValidator
     * @return
     */
    @RequestMapping(value = "/authority/user/resetPassword", method = RequestMethod.POST)
    ResponseData resetPassword(@RequestBody UserPasswordValidator userPasswordValidator) throws Exception;

    /**
     * 注销、启用用户
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/authority/user/forbiddenUser/{id}/{status}", method = RequestMethod.GET)
    ResponseData forbiddenUser(@PathVariable String id, @PathVariable String status) throws Exception;

    @RequestMapping(value = "/authority/user/batchForbiddenUser/{status}", method = RequestMethod.POST)
    ResponseData batchForbiddenUser(@RequestBody List<SecurityUser> userList, @PathVariable String status) throws Exception;

    /**
     * 根据用户id查询自服务平台角色
     * @param id
     * @return
     */
    @RequestMapping(value = "/authority/user/webLoginAuth/{id}", method = RequestMethod.GET)
    ResponseData<List<ServiceAuthInfo>> webLoginAuth(@PathVariable String id);

    /**
     * WEB登录账号授权
     * @param serviceAuthInfo
     * @return
     */
    @RequestMapping(value = "/authority/user/webAuthSave", method = RequestMethod.POST)
    ResponseData webAuthSave(@RequestBody ServiceAuthInfo serviceAuthInfo);
}
