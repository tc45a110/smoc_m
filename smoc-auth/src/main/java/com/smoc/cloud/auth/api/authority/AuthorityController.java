package com.smoc.cloud.auth.api.authority;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.auth.data.provider.entity.BaseOrganization;
import com.smoc.cloud.auth.data.provider.service.BaseOrganizationService;
import com.smoc.cloud.auth.data.provider.service.BaseUserService;
import com.smoc.cloud.common.auth.validator.OrgValidator;
import com.smoc.cloud.common.auth.validator.UserValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

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

        log.info("[接口创建组织结构]数据");

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(orgValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(orgValidator));
        }

        log.info("[接口创建组织结构]数据:{}={}",  JSON.toJSONString(orgValidator));

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

        log.info("[接口创建用户]数据:{}={}",  JSON.toJSONString(userValidator));
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
     *  注销
     * @return
     */
    @RequestMapping(value = "user/closeUser/{entcode}/{status}", method = RequestMethod.GET)
    public ResponseData closeUser(@PathVariable String entcode, @PathVariable String status) {

        log.info("[接口请求][帐号欠停销]数据:{}={}",  entcode);

        return baseUserService.closeUser(entcode, status);
    }
}
