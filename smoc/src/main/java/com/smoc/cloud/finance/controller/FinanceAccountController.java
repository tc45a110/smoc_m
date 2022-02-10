package com.smoc.cloud.finance.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.finance.service.FinanceAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * 财务账号
 */
@Slf4j
@RestController
@RequestMapping("finance/account")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class FinanceAccountController {

    @Autowired
    private FinanceAccountService financeAccountService;

    /**
     * 分页查询
     *
     * @param pageParams
     * @param flag 1表示业务账号 账户  2表示认证账号 账户 3表示财务共享账号
     * @return
     */
    @RequestMapping(value = "/page/{flag}", method = RequestMethod.POST)
    public ResponseData<PageList<FinanceAccountValidator>> page(@RequestBody PageParams<FinanceAccountValidator> pageParams, @PathVariable String flag) {

        return financeAccountService.page(pageParams, flag);
    }

    /**
     * 统计账户金额
     *
     * @param flag 1 表示业务账号 账户  2表示认证账号 账户 3表示财务共享账户
     * @return
     */
    @RequestMapping(value = "/count/{flag}", method = RequestMethod.GET)
    public ResponseData<Map<String, Object>> count(@PathVariable String flag) {

        return financeAccountService.countSum(flag);
    }

    /**
     * 账户充值
     *
     * @param financeAccountRechargeValidator
     * @return
     */
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public ResponseData recharge(@RequestBody FinanceAccountRechargeValidator financeAccountRechargeValidator) {

        return financeAccountService.recharge(financeAccountRechargeValidator);
    }

    /**
     * 根据id查询
     *
     * @param accountId
     * @return
     */
    @RequestMapping(value = "/findById/{accountId}", method = RequestMethod.GET)
    public ResponseData<FinanceAccountValidator> findById(@PathVariable String accountId) {

        return financeAccountService.findById(accountId);
    }

    /**
     * 根据enterpriseId，查询企业所有财务账户
     *
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value = "/findEnterpriseFinanceAccounts/{enterpriseId}", method = RequestMethod.GET)
    public ResponseData<List<FinanceAccountValidator>> findEnterpriseFinanceAccounts(@PathVariable String enterpriseId) {

        return financeAccountService.findEnterpriseFinanceAccounts(enterpriseId);
    }

    /**
     * 根据企业enterpriseId，查询企业所有财务账户(包括子企业财务账户)
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value = "/findEnterpriseAndSubsidiaryFinanceAccount/{enterpriseId}", method = RequestMethod.GET)
    public ResponseData<List<FinanceAccountValidator>> findEnterpriseAndSubsidiaryFinanceAccount(@PathVariable String enterpriseId) {

        return financeAccountService.findEnterpriseAndSubsidiaryFinanceAccount(enterpriseId);
    }

    /**
     * 根据enterpriseId 汇总企业金额统计
     * @param enterpriseId
     * @return
     */
    @RequestMapping(value = "/countEnterpriseSum/{enterpriseId}", method = RequestMethod.GET)
    public ResponseData<Map<String, Object>> countEnterpriseSum(@PathVariable String enterpriseId) {

        return financeAccountService.countEnterpriseSum(enterpriseId);
    }

    /**
     * 创建共享账户
     *
     * @param financeAccountValidator
     * @param op 操作类型 为add、edit
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody FinanceAccountValidator financeAccountValidator,@PathVariable String op) {

        return financeAccountService.save(financeAccountValidator,op);
    }


}
