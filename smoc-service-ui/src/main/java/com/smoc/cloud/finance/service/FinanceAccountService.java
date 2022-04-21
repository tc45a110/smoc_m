package com.smoc.cloud.finance.service;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.finance.remote.FinanceAccountFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class FinanceAccountService {

    @Autowired
    private FinanceAccountFeignClient financeAccountFeignClient;

    /**
     * 账户分查询列表
     *
     * @param pageParams
     * @param flag       1表示业务账号 账户  2表示认证账号 账户 3表示财务共享账号
     * @return
     */
    public ResponseData<PageList<FinanceAccountValidator>> page(PageParams<FinanceAccountValidator> pageParams, String flag) {

        try {
            return this.financeAccountFeignClient.page(pageParams, flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 充值分查询列表
     *
     * @param pageParams
     * @param flag       1表示业务账号 账户  2表示认证账号 账户
     * @return
     */
    public ResponseData<PageList<FinanceAccountRechargeValidator>> rechargePage(PageParams<FinanceAccountRechargeValidator> pageParams, String flag) {

        try {
            return this.financeAccountFeignClient.rechargePage(pageParams, flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
