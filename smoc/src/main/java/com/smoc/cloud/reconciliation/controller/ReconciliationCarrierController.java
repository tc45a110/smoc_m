package com.smoc.cloud.reconciliation.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationCarrierItemsValidator;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationChannelCarrierModel;
import com.smoc.cloud.reconciliation.service.ReconciliationCarrierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

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

    /**
     * 根据运营商和账单周期查询账单
     * @param startDate
     * @param channelProvder
     * @return
     */
    @RequestMapping(value = "/findReconciliationCarrier/{startDate}/{channelProvder}", method = RequestMethod.GET)
    public ResponseData<List<ReconciliationCarrierItemsValidator>> findReconciliationCarrier(@PathVariable String startDate, @PathVariable String channelProvder) {

        return reconciliationCarrierService.findReconciliationCarrier(startDate,channelProvder);
    }

    /**
     * 保存对账
     *
     * @param reconciliationChannelCarrierModel
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseData save(@RequestBody ReconciliationChannelCarrierModel reconciliationChannelCarrierModel) {

        return reconciliationCarrierService.save(reconciliationChannelCarrierModel);
    }
}
