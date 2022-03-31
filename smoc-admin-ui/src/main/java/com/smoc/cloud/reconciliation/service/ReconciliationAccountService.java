package com.smoc.cloud.reconciliation.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationEnterpriseModel;
import com.smoc.cloud.reconciliation.remote.ReconciliationAccountRemoteClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 业务账号对账
 */
@Slf4j
@Service
public class ReconciliationAccountService {

    @Autowired
    private ReconciliationAccountRemoteClient reconciliationAccountRemoteClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ReconciliationEnterpriseModel>> page(PageParams<ReconciliationEnterpriseModel> pageParams) {
        try {
            return reconciliationAccountRemoteClient.page(pageParams);
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
