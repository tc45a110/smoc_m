package com.smoc.cloud.reconciliation.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationChannelCarrierModel;
import com.smoc.cloud.reconciliation.remote.ReconciliationCarrierRemoteClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 运营商对账
 */
@Slf4j
@Service
public class ReconciliationCarrierService {

    @Autowired
    private ReconciliationCarrierRemoteClient reconciliationCarrierRemoteClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ReconciliationChannelCarrierModel>> page(PageParams<ReconciliationChannelCarrierModel> pageParams) {
        try {
            return reconciliationCarrierRemoteClient.page(pageParams);
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

}
