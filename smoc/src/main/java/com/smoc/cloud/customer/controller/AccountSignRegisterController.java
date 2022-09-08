package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.qo.ExcelRegisterImportData;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.AccountSignRegisterService;
import com.smoc.cloud.sign.service.SignRegisterService;
import feign.Body;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("sign/register")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AccountSignRegisterController {

    @Autowired
    private SignRegisterService signRegisterService;

    @Autowired
    private AccountSignRegisterService accountSignRegisterService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<AccountSignRegisterValidator>> page(@RequestBody PageParams<AccountSignRegisterValidator> pageParams) {

        return accountSignRegisterService.page(pageParams);
    }

    /**
     * 添加、修改
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody AccountSignRegisterValidator accountSignRegisterValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(accountSignRegisterValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(accountSignRegisterValidator));
        }

        //保存操作
        ResponseData data = accountSignRegisterService.save(accountSignRegisterValidator, op);

        //生成签名报备
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            signRegisterService.generateSignRegisterByRegisterId(accountSignRegisterValidator.getId());
        }

        return data;
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData<AccountSignRegisterValidator> findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData<AccountSignRegisterValidator> data = accountSignRegisterService.findById(id);
        return data;
    }

    /**
     * 根据业务账号，查询已占用的签名自定义扩展号
     *
     * @param account
     * @param id      当id 不为空时候，不查询本id的签名自定义扩展号
     * @return
     */
    @RequestMapping(value = "/findExtendDataByAccount/{account}/{id}", method = RequestMethod.GET)
    public ResponseData<List<String>> findExtendDataByAccount(@PathVariable String account, @PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData<List<String>> data = accountSignRegisterService.findExtendDataByAccount(account, id);
        return data;
    }

    /**
     * 注销
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

        ResponseData data = accountSignRegisterService.deleteById(id);
        return data;
    }

    /**
     * 报备数据导入
     *
     * @param importList
     * @return
     */
    @RequestMapping(value = "/registerImport", method = RequestMethod.POST)
    public ResponseData registerImport(@RequestBody List<ExcelRegisterImportData> importList) {

        signRegisterService.importExcel(importList);
        return ResponseDataUtil.buildSuccess();
    }
}
