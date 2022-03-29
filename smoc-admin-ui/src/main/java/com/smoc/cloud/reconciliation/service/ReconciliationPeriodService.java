package com.smoc.cloud.reconciliation.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationPeriodValidator;
import com.smoc.cloud.reconciliation.remote.ReconciliationPeriodRemoteClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
public class ReconciliationPeriodService {

    @Autowired
    private ReconciliationPeriodRemoteClient reconciliationPeriodRemoteClient;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public ResponseData<PageList<ReconciliationPeriodValidator>> page(PageParams<ReconciliationPeriodValidator> pageParams) {
        try {
            return reconciliationPeriodRemoteClient.page(pageParams);
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 创建业务账号对账
     */
    public ResponseData buildAccountPeriod(ReconciliationPeriodValidator validator) {
        try {
            return reconciliationPeriodRemoteClient.buildAccountPeriod(validator);
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }

    }

    /**
     * 查询近5个月账期
     */
    public ResponseData<Map<String, String>> findAccountPeriod() {
        try {
            return reconciliationPeriodRemoteClient.findAccountPeriod();
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }

    }

    /**
     * 删除账期
     */
    public ResponseData deleteAccountPeriod(String id) {
        try {
            return reconciliationPeriodRemoteClient.deleteAccountPeriod(id);
        } catch (Exception e) {
            return ResponseDataUtil.buildError(e.getMessage());
        }

    }
}
