package com.smoc.cloud.auth.api;


import com.smoc.cloud.auth.data.provider.service.BaseUserService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 * 用户管理对外接口
 * 2019/3/29 14:29
 **/
@Slf4j
@RestController
@RequestMapping("/users")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class UserController {

    @Autowired
    private BaseUserService baseUserService;

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<Users> saveUser(@RequestBody PageParams<Users> pageParams) {

        return baseUserService.page(pageParams);
    }

    /**
     * 根据ID查询用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<UserValidator> findUserById(@PathVariable String id) {

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
     * 添加用户信息
     *
     * @param userValidator
     * @param op            操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody UserValidator userValidator, @PathVariable String op) {

        /**
         * 完成参数规则验证
         */
        if (!MpmValidatorUtil.validate(userValidator.getBaseUserValidator())) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(userValidator.getBaseUserValidator()));
        }

        if (!MpmValidatorUtil.validate(userValidator.getBaseUserExtendsValidator())) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(userValidator.getBaseUserExtendsValidator()));
        }

        return baseUserService.save(userValidator, op);
    }

    /**
     * 根据ID删除用户信息
     *
     * @param id
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ResponseData deleteUserById(@PathVariable String id) {

        /**
         * 完成参数规则验证
         */
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        return baseUserService.deleteById(id);
    }

    /**
     * 重置用户密码
     *
     * @param userPasswordValidator
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseData resetPassword(@RequestBody UserPasswordValidator userPasswordValidator) {

        /**
         * 完成参数规则验证
         */
        if (!MpmValidatorUtil.validate(userPasswordValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(userPasswordValidator));
        }

        ResponseData data = baseUserService.resetPassword(userPasswordValidator.getId(), userPasswordValidator.getPassword());

        return data;
    }

    /**
     * 禁用、启用用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/forbiddenUser/{id}/{status}", method = RequestMethod.GET)
    public ResponseData forbiddenUser(@PathVariable String id,@PathVariable Integer status) {
        /**
         * 完成参数规则验证
         */
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = baseUserService.forbiddenUser(id,status);

        return data;
    }


    @RequestMapping(value = "/findByUserId/{id}", method = RequestMethod.GET)
    public ResponseData<UserValidator> findByUserId(@PathVariable String id) {

        /**
         * 完成参数规则验证
         */
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        return baseUserService.findByUserId(id);
    }

    @RequestMapping(value = "/findUserByPhone/{phone}", method = RequestMethod.GET)
    public ResponseData<UserValidator> findUserByPhone(@PathVariable String phone) {

        return baseUserService.findUserByPhone(phone);
    }

    @RequestMapping(value = "/findUserByCode/{teamCode}", method = RequestMethod.GET)
    public ResponseData<UserValidator> findUserByCode(@PathVariable String teamCode) {

        return baseUserService.findUserByCode(teamCode);
    }
}