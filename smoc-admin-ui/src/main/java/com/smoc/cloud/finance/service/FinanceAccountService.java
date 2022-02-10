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


import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FinanceAccountService {

    @Autowired
    private FinanceAccountFeignClient financeAccountFeignClient;

    /**
     * 分查询列表
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
     * 统计账户金额
     *
     * @param flag 1 表示业务账号 账户  2表示认证账号 账户 3表示财务共享账户
     * @return
     */
    public ResponseData<Map<String, Object>> count(String flag) {
        try {
            return this.financeAccountFeignClient.count(flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 账户充值,保存充值记录，变更财务账户
     *
     * @return
     */
    public ResponseData recharge(FinanceAccountRechargeValidator financeAccountRechargeValidator) {
        try {
            return this.financeAccountFeignClient.recharge(financeAccountRechargeValidator);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据id查询
     *
     * @param accountId
     * @return
     */
    public ResponseData<FinanceAccountValidator> findById(String accountId) {
        try {
            return this.financeAccountFeignClient.findById(accountId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据enterpriseId，查询企业所有财务账户
     *
     * @param enterpriseId
     * @return
     */
    public ResponseData<List<FinanceAccountValidator>> findEnterpriseFinanceAccounts(String enterpriseId) {
        try {
            return this.financeAccountFeignClient.findEnterpriseFinanceAccounts(enterpriseId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据企业enterpriseId，查询企业所有财务账户(包括子企业财务账户)
     *
     * @param enterpriseId
     * @return
     */
    public ResponseData<List<FinanceAccountValidator>> findEnterpriseAndSubsidiaryFinanceAccount(String enterpriseId) {
        try {
            return this.financeAccountFeignClient.findEnterpriseAndSubsidiaryFinanceAccount(enterpriseId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 根据enterpriseId 汇总企业金额统计
     *
     * @param enterpriseId
     * @return
     */
    public ResponseData<Map<String, Object>> countEnterpriseSum(String enterpriseId) {
        try {
            return this.financeAccountFeignClient.countEnterpriseSum(enterpriseId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 创建共享账户
     *
     * @param financeAccountValidator
     * @param op                      操作类型 为add、edit
     * @return
     */
    public ResponseData save(FinanceAccountValidator financeAccountValidator, String op) {
        try {
            return this.financeAccountFeignClient.save(financeAccountValidator, op);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
