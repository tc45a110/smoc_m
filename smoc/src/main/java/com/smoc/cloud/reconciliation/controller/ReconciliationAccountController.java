package com.smoc.cloud.reconciliation.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationPeriodValidator;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationEnterpriseModel;
import com.smoc.cloud.reconciliation.service.ReconciliationAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

/**
 * 业务账号对账
 */
@Slf4j
@RestController
@RequestMapping("reconciliation/account")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ReconciliationAccountController {

    @Autowired
    private ReconciliationAccountService reconciliationAccountService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/ec/page", method = RequestMethod.POST)
    public ResponseData<PageList<ReconciliationEnterpriseModel>> page(@RequestBody PageParams<ReconciliationEnterpriseModel> pageParams) {

        return reconciliationAccountService.page(pageParams);
    }

    /**
     * 根据企业ID 和账期 查询企业账单明细
     *
     * @param enterpriseId
     * @param accountPeriod
     * @return
     */
    @RequestMapping(value = "/ec/getEnterpriseBills/{enterpriseId}/{accountPeriod}", method = RequestMethod.POST)
    public ResponseData<Map<String, Object>> getEnterpriseBills(@PathVariable String enterpriseId, @PathVariable String accountPeriod) {

        return reconciliationAccountService.getEnterpriseBills(enterpriseId, accountPeriod);
    }
}
