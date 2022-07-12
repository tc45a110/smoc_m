package com.smoc.cloud.reconciliation.remote;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationCarrierItemsValidator;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationChannelCarrierModel;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationEnterpriseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * 运营商对账
 */
@FeignClient(name = "smoc", path = "/smoc")
public interface ReconciliationCarrierRemoteClient {

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    @RequestMapping(value = "/reconciliation/carrier/page", method = RequestMethod.POST)
    ResponseData<PageList<ReconciliationChannelCarrierModel>> page(@RequestBody PageParams<ReconciliationChannelCarrierModel> pageParams) throws Exception;

    /**
     * 根据运营商和账单周期查询账单
     * @param startDate
     * @param channelProvder
     * @return
     */
    @RequestMapping(value = "/reconciliation/carrier/findReconciliationCarrier/{startDate}/{channelProvder}", method = RequestMethod.GET)
    ResponseData<List<ReconciliationCarrierItemsValidator>> findReconciliationCarrier(@PathVariable String startDate, @PathVariable String channelProvder);

    /**
     * 保存对账
     * @param reconciliationChannelCarrierModel
     * @return
     */
    @RequestMapping(value = "/reconciliation/carrier/save", method = RequestMethod.POST)
    ResponseData save(@RequestBody ReconciliationChannelCarrierModel reconciliationChannelCarrierModel);

    /**
     * 运营商对账记录
     * @param params
     * @return
     */
    @RequestMapping(value = "/reconciliation/carrier/reconciliationCarrierRecord", method = RequestMethod.POST)
    ResponseData<PageList<ReconciliationChannelCarrierModel>> reconciliationCarrierRecord(@RequestBody PageParams<ReconciliationChannelCarrierModel> params);
}
