package com.smoc.cloud.finance.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.finance.entity.FinanceAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Map;


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

    /**
     * 分页查询 身份认证账号
     * @param pageParams
     * @return
     */
    PageList<FinanceAccountValidator> pageIdentification(PageParams<FinanceAccountValidator> pageParams);

    /**
     * 分页查询 业务账号
     * @param pageParams
     * @return
     */
    PageList<FinanceAccountValidator> pageBusinessType(PageParams<FinanceAccountValidator> pageParams);

    /**
     * 汇总金额统计
     * @param flag 1表示业务账号 账户  2表示认证账号 账户
     * @return
     */
    Map<String,Object> countSum(String flag);

    /**
     * 账户充值 改变账户金额
     * @param  rechargeSum 充值金额
     */
    @Modifying
    @Query(value = "update finance_account set ACCOUNT_TOTAL_SUM = ACCOUNT_TOTAL_SUM+:rechargeSum,ACCOUNT_USABLE_SUM =ACCOUNT_USABLE_SUM+:rechargeSum,ACCOUNT_RECHARGE_SUM=ACCOUNT_RECHARGE_SUM+:rechargeSum where ACCOUNT_ID = :accountId",nativeQuery = true)
    void recharge(@Param("rechargeSum") BigDecimal rechargeSum,@Param("accountId") String accountId);

    /**
     * 检查账户余额，包括了授信金额  true 表示余额 够用
     * @param accountId
     * @param ammount 金额
     * @return
     */
    boolean checkAccountUsableSum(String accountId,BigDecimal ammount);

    /**
     * 冻结金额
     * @param accountId
     * @param ammount
     */
    void freeze(String accountId,BigDecimal ammount);

    /**
     * 解冻扣费
     * @param accountId
     * @param ammount
     */
    void unfreeze(String accountId,BigDecimal ammount);

    /**
     * 解冻不扣费
     * @param accountId
     * @param ammount
     */
    void unfreezeFree(String accountId,BigDecimal ammount);
}
