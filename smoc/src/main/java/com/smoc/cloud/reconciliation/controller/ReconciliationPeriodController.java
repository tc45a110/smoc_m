package com.smoc.cloud.reconciliation.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationPeriodValidator;
import com.smoc.cloud.reconciliation.service.ReconciliationPeriodService;
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
public class ReconciliationPeriodController {

    @Autowired
    private ReconciliationPeriodService reconciliationPeriodService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<ReconciliationPeriodValidator>> page(@RequestBody PageParams<ReconciliationPeriodValidator> pageParams) {

        return reconciliationPeriodService.page(pageParams);
    }

    /**
     * 创建业务账号对账
     */
    @RequestMapping(value = "/buildAccountPeriod", method = RequestMethod.POST)
    public ResponseData buildAccountPeriod(@RequestBody ReconciliationPeriodValidator validator) {

        return reconciliationPeriodService.buildAccountPeriod(validator);
    }

    /**
     * 删除账期
     */
    @RequestMapping(value = "/deleteAccountPeriod/{id}", method = RequestMethod.GET)
    public ResponseData deleteAccountPeriod(@PathVariable String id) {
        return reconciliationPeriodService.deleteAccountPeriod(id);
    }

    /**
     * 查询近5个月账期
     */
    @RequestMapping(value = "/findAccountPeriod", method = RequestMethod.GET)
    public ResponseData<Map<String, String>> findAccountPeriod() {

        return reconciliationPeriodService.findAccountPeriod();
    }
}
