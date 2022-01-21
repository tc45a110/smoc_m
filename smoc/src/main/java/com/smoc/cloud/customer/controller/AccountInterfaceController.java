package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountInterfaceInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.AccountFinanceService;
import com.smoc.cloud.customer.service.AccountInterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 账号接口信息
 **/
@Slf4j
@RestController
@RequestMapping("account/interface")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class AccountInterfaceController {

    @Autowired
    private AccountInterfaceService accountInterfaceService;

    /**
     *  根据id查询数据
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/findById/{accountId}", method = RequestMethod.GET)
    public ResponseData findById(@PathVariable String accountId) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        return accountInterfaceService.findById(accountId);
    }

    /**
     * 添加、修改
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody AccountInterfaceInfoValidator accountInterfaceInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(accountInterfaceInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(accountInterfaceInfoValidator));
        }

        //保存操作
        ResponseData data = accountInterfaceService.save(accountInterfaceInfoValidator, op);

        return data;
    }
}
