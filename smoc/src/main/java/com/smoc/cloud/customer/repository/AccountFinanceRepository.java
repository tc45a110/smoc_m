package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import com.smoc.cloud.customer.entity.AccountFinanceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 账号财务操作类
 */
public interface AccountFinanceRepository extends CrudRepository<AccountFinanceInfo, String>, JpaRepository<AccountFinanceInfo, String> {

    /**
     * 查询账号配置运营商价格
     * @param accountFinanceInfoValidator
     * @return
     */
    List<AccountFinanceInfoValidator> findByAccountId(AccountFinanceInfoValidator accountFinanceInfoValidator);

    void deleteByAccountIdAndCarrier(String accountId, String carrier);

    void deleteByAccountId(String accountId);

    void batchSave(AccountFinanceInfoValidator accountFinanceInfoValidator);


}
