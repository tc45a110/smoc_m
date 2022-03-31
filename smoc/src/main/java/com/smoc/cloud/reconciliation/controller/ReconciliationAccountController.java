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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

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
}
