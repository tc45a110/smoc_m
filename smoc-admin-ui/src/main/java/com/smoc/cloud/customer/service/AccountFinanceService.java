package com.smoc.cloud.customer.service;

import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import com.smoc.cloud.customer.remote.AccountFinanceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * 账号财务管理服务
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountFinanceService {

    @Autowired
    private AccountFinanceFeignClient accountFinanceFeignClient;

    /**
     * 根据运营商和账号ID查询运营商单价
     * @param accountFinanceInfoValidator
     * @return
     */
    public ResponseData<Map<String, BigDecimal>> editCarrierPrice(AccountFinanceInfoValidator accountFinanceInfoValidator) {
        try {
            ResponseData<Map<String, BigDecimal>> list = this.accountFinanceFeignClient.editCarrierPrice(accountFinanceInfoValidator);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 查询账号配置的运营商价格
     * @param accountFinanceInfoValidator
     * @return
     */
    public ResponseData<List<AccountFinanceInfoValidator>> findByAccountId(AccountFinanceInfoValidator accountFinanceInfoValidator) {
        try {
            ResponseData<List<AccountFinanceInfoValidator>> list = this.accountFinanceFeignClient.findByAccountId(accountFinanceInfoValidator);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }

    /**
     * 保存、修改数据
     *  op 是类型 表示了保存或修改
     * @param accountFinanceInfoValidator
     * @param op
     * @return
     */
    public ResponseData save(AccountFinanceInfoValidator accountFinanceInfoValidator, String op) {
        try {
            ResponseData data = this.accountFinanceFeignClient.save(accountFinanceInfoValidator, op);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDataUtil.buildError(e.getMessage());
        }
    }
}
