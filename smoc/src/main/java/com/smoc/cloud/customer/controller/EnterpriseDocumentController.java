package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseDocumentInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseDocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;


/**
 * 签名资质管理信息接口
 **/
@Slf4j
@RestController
@RequestMapping("ec/customer/document")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class EnterpriseDocumentController {

    @Autowired
    private EnterpriseDocumentService enterpriseDocumentService;

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public PageList<EnterpriseDocumentInfoValidator> page(@RequestBody PageParams<EnterpriseDocumentInfoValidator> pageParams) {

        return enterpriseDocumentService.page(pageParams);
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

        ResponseData data = enterpriseDocumentService.findById(id);
        return data;
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(enterpriseDocumentInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(enterpriseDocumentInfoValidator));
        }

        //保存操作
        ResponseData data = enterpriseDocumentService.save(enterpriseDocumentInfoValidator, op);

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

        return enterpriseDocumentService.deleteById(id);
    }

    /**
     * 审核
     * @return
     */
    @RequestMapping(value = "/sign/check", method = RequestMethod.POST)
    public ResponseData check(@RequestBody EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(enterpriseDocumentInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(enterpriseDocumentInfoValidator));
        }

        //保存操作
        ResponseData data = enterpriseDocumentService.check(enterpriseDocumentInfoValidator);

        return data;
    }

    /**
     * 查询签名列表
     * @param enterpriseDocumentInfoValidator
     * @return
     */
    @RequestMapping(value = "/findMessageSign", method = RequestMethod.POST)
    public ResponseData<List<EnterpriseDocumentInfoValidator>> findMessageSign(@RequestBody EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator) {

        return enterpriseDocumentService.findMessageSign(enterpriseDocumentInfoValidator);
    }

}
