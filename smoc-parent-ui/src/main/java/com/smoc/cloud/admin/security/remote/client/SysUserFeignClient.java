package com.smoc.cloud.admin.security.remote.client;

import com.smoc.cloud.common.auth.entity.SecurityUser;
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

import java.util.List;


/**
 * 用户管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc-auth",path = "/auth")
public interface SysUserFeignClient {


    /**
     * 获取用户属性
     */
    @RequestMapping(value = "/sysUser/userProfile/{id}", method = RequestMethod.GET)
    ResponseData<UserValidator> userProfile(@PathVariable(value = "id") String id) throws Exception;

    /**
     * 重置密码
     *
     * @param userPasswordValidator
     * @return
     */
    @RequestMapping(value = "/sysUser/resetOwnPassword", method = RequestMethod.POST)
    ResponseData resetOwnPassword(@RequestBody UserPasswordValidator userPasswordValidator) throws Exception;

    /**
     * 根据组织id查询用户信息 携带安全信息
     *
     * @param orgId
     * @return
     */
    @RequestMapping(value = "/sysUser/findSecurityUserByOrgId/{orgId}", method = RequestMethod.GET)
    ResponseData<List<SecurityUser>> findSecurityUserByOrgId(@PathVariable("orgId") String orgId) throws Exception;

    /**
     * 分页查询用户信息
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/sysUser/page", method = RequestMethod.POST)
    PageList<Users> page(@RequestBody PageParams<Users> pageParams);

}
