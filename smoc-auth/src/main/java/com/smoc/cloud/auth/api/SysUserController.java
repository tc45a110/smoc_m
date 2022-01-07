package com.smoc.cloud.auth.api;

import com.smoc.cloud.auth.common.utils.MpmEncryptPasswordEncoder;
import com.smoc.cloud.auth.data.provider.entity.BaseUser;
import com.smoc.cloud.auth.data.provider.service.BaseUserService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.auth.validator.UserPasswordValidator;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统用户属性
 * 2019/5/14 12:19
 **/
@RestController
@RequestMapping("/sysUser")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class SysUserController {

    @Autowired
    private BaseUserService baseUserService;
    @Resource
    private RedisTemplate<String, SecurityUser> redisTemplate;

    /**
     * 根据ID查询用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/userProfile/{id}", method = RequestMethod.GET)
    public ResponseData<UserValidator> userProfile(@PathVariable String id) {

        /**
         * 完成参数规则验证
         */
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        return baseUserService.findById(id);
    }

    /**
     * 重置用户密码
     *
     * @param userPasswordValidator
     * @return
     */
    @RequestMapping(value = "/resetOwnPassword", method = RequestMethod.POST)
    public ResponseData resetOwnPassword(@RequestBody UserPasswordValidator userPasswordValidator) {

        /**
         * 完成参数规则验证
         */
        if (!MpmValidatorUtil.validate(userPasswordValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(userPasswordValidator));
        }

        /**
         * 判断旧密码是否正确
         */
       /* BaseUser user = baseUserService.findBaseUserById(userPasswordValidator.getId());
        if (!new MpmEncryptPasswordEncoder().getPasswordEncoder().matches(userPasswordValidator.getOldPassword(), user.getPassword())) {
            return ResponseDataUtil.buildError(ResponseCode.USER_PASSWORD_NULL);
        }
*/
        ResponseData data = baseUserService.resetPassword(userPasswordValidator.getId(), userPasswordValidator.getPassword());

        return data;
    }

    /**
     * 根据组织id查询用户信息  携带安全的角色信息
     *
     * @param orgId
     * @return
     */
    @RequestMapping(value = "/findSecurityUserByOrgId/{orgId}", method = RequestMethod.GET)
    public ResponseData<List<SecurityUser>> findSecurityUserByOrgId(@PathVariable String orgId) {

        /**
         * 完成参数规则验证
         */
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(orgId);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        return baseUserService.findSecurityUserByOrgId(orgId);
    }

    /**
     * 根据条件查询用户列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<Users> page(@RequestBody PageParams<Users> pageParams) {

        return baseUserService.page(pageParams);
    }

}
