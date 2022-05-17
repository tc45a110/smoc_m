package com.smoc.cloud.finance.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.finance.service.FinanceAccountRechargeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

/**
 * 财务账号
 */
@Slf4j
@RestController
@RequestMapping("finance/recharge")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class FinanceAccountRechargeController {

    @Autowired
    private FinanceAccountRechargeService financeAccountRechargeService;

    /**
     * 分页查询
     *
     * @param pageParams
     * @param flag       1表示业务账号 账户  2表示认证账号 账户
     * @return
     */
    @RequestMapping(value = "/page/{flag}", method = RequestMethod.POST)
    public ResponseData<PageList<FinanceAccountRechargeValidator>> page(@RequestBody PageParams<FinanceAccountRechargeValidator> pageParams, @PathVariable String flag) {

        return financeAccountRechargeService.page(pageParams, flag);
    }

    /**
     * 统计充值金额
     *
     * @param financeAccountRechargeValidator
     * @return
     */
    @RequestMapping(value = "/countRechargeSum", method = RequestMethod.POST)
    public ResponseData<Map<String, Object>> countRechargeSum(@RequestBody FinanceAccountRechargeValidator financeAccountRechargeValidator) {

        return financeAccountRechargeService.countRechargeSum(financeAccountRechargeValidator);
    }

    /**
     * 统计充值金额
     *
     * @param financeAccountRechargeValidator
     * @return
     */
    @RequestMapping(value = "/statisticRechargeSum/{flag}", method = RequestMethod.POST)
    public ResponseData<Map<String, Object>> statisticRechargeSum(@RequestBody FinanceAccountRechargeValidator financeAccountRechargeValidator, @PathVariable String flag) {

        return financeAccountRechargeService.statisticRechargeSum(financeAccountRechargeValidator,flag);
    }
}
