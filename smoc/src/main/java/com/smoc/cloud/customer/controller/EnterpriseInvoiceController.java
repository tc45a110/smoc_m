package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseExpressInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseInvoiceInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseInvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 企业发票信息接口
 **/
@Slf4j
@RestController
@RequestMapping("enterprise/invoice")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class EnterpriseInvoiceController {

    @Autowired
    private EnterpriseInvoiceService enterpriseInvoiceService;

    /**
     * 查询列表
     * @param enterpriseInvoiceInfoValidator
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<List<EnterpriseExpressInfoValidator>> page(@RequestBody EnterpriseInvoiceInfoValidator enterpriseInvoiceInfoValidator) {

        return enterpriseInvoiceService.page(enterpriseInvoiceInfoValidator);
    }

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

        ResponseData data = enterpriseInvoiceService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody EnterpriseInvoiceInfoValidator enterpriseInvoiceInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(enterpriseInvoiceInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(enterpriseInvoiceInfoValidator));
        }

        //保存操作
        ResponseData data = enterpriseInvoiceService.save(enterpriseInvoiceInfoValidator, op);

        return data;
    }

    /**
     * 根据ID 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ResponseData deleteById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        return enterpriseInvoiceService.deleteById(id);
    }
}
