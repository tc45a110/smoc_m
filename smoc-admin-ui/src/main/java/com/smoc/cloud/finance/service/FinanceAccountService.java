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



import java.util.Map;

@Slf4j
@Service
public class FinanceAccountService {

    @Autowired
    private FinanceAccountFeignClient financeAccountFeignClient;

    /**
     * 分查询列表
     * @param pageParams
     * @param flag 1表示业务账号 账户  2表示认证账号 账户
     * @return
     */
    public ResponseData<PageList<FinanceAccountValidator>>  page(PageParams<FinanceAccountValidator> pageParams,String flag) {

        try {
            return this.financeAccountFeignClient.page(pageParams,flag);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 统计账户金额
     * @param flag 1表示业务账号 账户  2表示认证账号 账户
     * @return
     */
    public ResponseData<Map<String,Object>>  count(String flag) {
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
    public ResponseData recharge(FinanceAccountRechargeValidator financeAccountRechargeValidator){
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
    public ResponseData<FinanceAccountValidator> findById( String accountId){
        try {
            return this.financeAccountFeignClient.findById(accountId);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
