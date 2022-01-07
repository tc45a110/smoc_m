package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 * 企业WE登录账号接口
 **/
@Slf4j
@RestController
@RequestMapping("enterprise/web")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class EnterpriseWebController {

    @Autowired
    private EnterpriseWebService enterpriseWebService;

    /**
     * 根据id获取信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = enterpriseWebService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(enterpriseWebAccountInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(enterpriseWebAccountInfoValidator));
        }

        //保存操作
        ResponseData data = enterpriseWebService.save(enterpriseWebAccountInfoValidator, op);

        return data;
    }

}
