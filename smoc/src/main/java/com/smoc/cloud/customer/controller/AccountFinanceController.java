package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.AccountFinanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 账号财务接口
 **/
@Slf4j
@RestController
@RequestMapping("account/finance")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class AccountFinanceController {

    @Autowired
    private AccountFinanceService accountFinanceService;

    /**
     * 根据账号ID查询运营商单价
     * @param accountFinanceInfoValidator
     * @return
     */
    @RequestMapping(value = "/editCarrierPrice", method = RequestMethod.POST)
    public ResponseData<Map<String, BigDecimal>> editCarrierPrice(@RequestBody AccountFinanceInfoValidator accountFinanceInfoValidator) {

        return accountFinanceService.editCarrierPrice(accountFinanceInfoValidator);
    }

    /**
     * 查询账号配置的运营商价格
     * @param accountFinanceInfoValidator
     * @return
     */
    @RequestMapping(value = "/findByAccountId", method = RequestMethod.POST)
    public ResponseData<List<AccountFinanceInfoValidator>> findByAccountId(@RequestBody AccountFinanceInfoValidator accountFinanceInfoValidator) {

        return accountFinanceService.findByAccountId(accountFinanceInfoValidator);
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody AccountFinanceInfoValidator accountFinanceInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(accountFinanceInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(accountFinanceInfoValidator));
        }

        //保存操作
        ResponseData data = accountFinanceService.save(accountFinanceInfoValidator, op);

        return data;
    }
}
