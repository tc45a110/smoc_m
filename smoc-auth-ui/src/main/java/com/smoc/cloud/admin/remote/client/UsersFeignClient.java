package com.smoc.cloud.admin.remote.client;

import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.auth.validator.UserPasswordValidator;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 用户管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc-auth",path = "/auth")
public interface UsersFeignClient {

    /**
     * 查询列表
     */

    @RequestMapping(value = "/users/page", method = RequestMethod.POST)
    PageList<Users> page(@RequestBody PageParams<Users> pageParams) throws Exception;

    /**
     * 根据id获取信息
     */
    @RequestMapping(value = "/users/findById/{id}", method = RequestMethod.GET)
    ResponseData<UserValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/users/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody UserValidator userValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除菜单数据
     */
    @RequestMapping(value = "/users/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

    /**
     * 重置用户密码
     *
     * @param userPasswordValidator
     * @return
     */
    @RequestMapping(value = "/users/resetPassword", method = RequestMethod.POST)
    ResponseData resetPassword(@RequestBody UserPasswordValidator userPasswordValidator) throws Exception;

    /**
     * 禁用、启用用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/users/forbiddenUser/{id}/{status}", method = RequestMethod.GET)
    ResponseData forbiddenUser(@PathVariable String id, @PathVariable Integer status) throws Exception;

}
