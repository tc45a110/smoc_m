package com.smoc.cloud.auth.api.authority;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.data.provider.entity.BaseOrganization;
import com.smoc.cloud.auth.data.provider.service.BaseOrganizationService;
import com.smoc.cloud.auth.data.provider.service.BaseUserService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.OrgValidator;
import com.smoc.cloud.common.auth.validator.UserPasswordValidator;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 组织管理接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/authority")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AuthorityController {

    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    private BaseOrganizationService baseOrganizationService;

    /**
     * 添加、修改
     *
     * @param orgValidator
     * @return
     */
    @RequestMapping(value = "org/save/{op}", method = RequestMethod.POST)
    public ResponseData saveOrg(@RequestBody OrgValidator orgValidator, @PathVariable String op) {

        log.info("[企业接入][企业开户信息-创建组织机构]数据");

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(orgValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(orgValidator));
        }

        log.info("[企业接入][企业开户信息-创建组织机构]数据:{}",  JSON.toJSONString(orgValidator));

        //转BaseUser存放对象
        BaseOrganization baseOrganization = new BaseOrganization();
        BeanUtils.copyProperties(orgValidator, baseOrganization);

        return baseOrganizationService.save(baseOrganization, op);
    }

    /**
     * 添加用户信息
     *
     * @param userValidator
     * @param op            操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "user/save/{op}", method = RequestMethod.POST)
    public ResponseData saveUser(@RequestBody UserValidator userValidator, @PathVariable String op) {

        log.info("[企业接入][创建用户]数据:{}={}",  JSON.toJSONString(userValidator));
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
     * 根据ID查询用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "user/findById/{id}", method = RequestMethod.GET)
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
     * 重置用户密码
     * @param userPasswordValidator
     * @return
     */
    @RequestMapping(value = "user/resetPassword", method = RequestMethod.POST)
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
     * 启用、注销用户
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "user/forbiddenUser/{id}/{status}", method = RequestMethod.GET)
    public ResponseData<UserValidator> forbiddenUser(@PathVariable String id,@PathVariable String status) {

        /**
         * 完成参数规则验证
         */
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        return baseUserService.closeUser(id,status);
    }

    /**
     * 批量启用、注销用户
     * @param status
     * @return
     */
    @RequestMapping(value = "user/batchForbiddenUser/{status}", method = RequestMethod.POST)
    public ResponseData batchForbiddenUser(@RequestBody List<SecurityUser> userList, @PathVariable String status) {

        return baseUserService.batchForbiddenUser(userList,status);
    }
}
