package com.smoc.cloud.finance.repository;


import com.smoc.cloud.finance.entity.FinanceAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;


/**
 * 账号操作类
 */
public interface FinanceAccountRepository extends CrudRepository<FinanceAccount, String>, JpaRepository<FinanceAccount, String> {


    //修改账户的业务类型
    @Modifying
    @Query(value = "update finance_account set ACCOUNT_TYPE = :accountTye where ACCOUNT_ID = :accountId",nativeQuery = true)
    void updateAccountTypeByAccountId(@Param("accountTye") String accountTye, @Param("accountId") String accountId);

    //修改账户的授信额度
    @Modifying
    @Query(value = "update finance_account set ACCOUNT_CREDIT_SUM = :accountCreditSum where ACCOUNT_ID = :accountId",nativeQuery = true)
    void updateAccountCreditSumByAccountId(@Param("accountId") String accountId, @Param("accountCreditSum") BigDecimal accountCreditSum);
}
