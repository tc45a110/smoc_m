package com.smoc.cloud.reconciliation.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationChannelCarrierModel;
import com.smoc.cloud.reconciliation.service.ReconciliationCarrierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

/**
 * 运营商对账
 */
@Slf4j
@RestController
@RequestMapping("reconciliation/carrier")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ReconciliationCarrierController {

    @Autowired
    private ReconciliationCarrierService reconciliationCarrierService;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ResponseData<PageList<ReconciliationChannelCarrierModel>> page(@RequestBody PageParams<ReconciliationChannelCarrierModel> pageParams) {

        return reconciliationCarrierService.page(pageParams);
    }


}
