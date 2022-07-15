package com.smoc.cloud.iot.system.service;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.iot.system.remote.FinanceAccountRechargeFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class FinanceAccountRechargeService {

    @Autowired
    private FinanceAccountRechargeFeignClient financeAccountRechargeFeignClient;

    /**
     * 分查询列表
     *
     * @param pageParams
     * @param flag       1表示业务账号 账户  2表示认证账号 账户
     * @return
     */
    public ResponseData<PageList<FinanceAccountRechargeValidator>> page(PageParams<FinanceAccountRechargeValidator> pageParams, String flag) {

        try {
            return this.financeAccountRechargeFeignClient.page(pageParams, flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 统计充值金额
     *
     * @param financeAccountRechargeValidator
     * @return
     */
    public ResponseData<Map<String, Object>> countRechargeSum(FinanceAccountRechargeValidator financeAccountRechargeValidator) {

        try {
            return this.financeAccountRechargeFeignClient.countRechargeSum(financeAccountRechargeValidator);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}


