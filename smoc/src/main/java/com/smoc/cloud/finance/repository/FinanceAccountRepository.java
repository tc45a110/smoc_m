package com.smoc.cloud.finance.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.finance.entity.FinanceAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
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
     * 分页查询 身份认证财务账户
     * @param pageParams
     * @return
     */
    PageList<FinanceAccountValidator> pageIdentification(PageParams<FinanceAccountValidator> pageParams);

    /**
     * 分页查询 业务财务账户
     * @param pageParams
     * @return
     */
    PageList<FinanceAccountValidator> pageBusinessType(PageParams<FinanceAccountValidator> pageParams);

    /**
     * 分页查询 共享财务账户
     *
     * @param pageParams
     * @return
     */
     PageList<FinanceAccountValidator> pageShare(PageParams<FinanceAccountValidator> pageParams);

    /**
     * 汇总金额统计
     * @param flag  1表示业务账号 账户  2表示认证账号 账户 3表示财务共享账号 4表示共用的账号财务账户
     * @return
     */
    Map<String,Object> countSum(String flag,FinanceAccountValidator qo);

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
     * @param amount 金额
     * @return
     */
    boolean checkAccountUsableSum(String accountId,BigDecimal amount);

    /**
     * 冻结金额
     * @param accountId
     * @param amount
     */
    void freeze(String accountId,BigDecimal amount);

    /**
     * 解冻并扣费
     * @param accountId
     * @param amount
     */
    void unfreeze(String accountId,BigDecimal amount);

    /**
     * 解冻不扣费
     * @param accountId
     * @param amount
     */
    void unfreezeFree(String accountId,BigDecimal amount);

    /**
     * 根据企业id，查询企业所有财务账户
     * @param enterpriseId
     * @return
     */
    List<FinanceAccountValidator> findEnterpriseFinanceAccount(String enterpriseId);

    /**
     * 根据企业enterpriseIds，查询企业所有财务账户(包括子企业财务账户)
     * @param enterpriseIds
     * @return
     */
    List<FinanceAccountValidator> findEnterpriseAndSubsidiaryFinanceAccount(List<String> enterpriseIds);

    /**
     * 根据enterpriseId 汇总企业金额统计
     * @param enterpriseId
     * @return
     */
    Map<String,Object> countEnterpriseSum(String enterpriseId);

    /**
     * 创建财务共享账户
     * 包括了创建共享账户流水记录，修改原账户状态，并创建共享账户
     */
    void createShareFinanceAccount(FinanceAccountValidator qo);

    /**
     * 修改财务共享账户
     * qo 新传递过来的
     * financeAccountValidator 现有
     * 包括了修改共享账户流水记录，修改原账户状态，并修改共享账户
     */
    void editShareFinanceAccount(FinanceAccountValidator qo, FinanceAccountValidator financeAccountValidator);

    /**
     * 查询共享账号的子账号信息
     * @param accountId
     * @return
     */
    List<FinanceAccountValidator> findSubsidiaryFinanceAccountByAccountId(String accountId);

    /**
     * 分页查询 系统共用账号财务账户 保障账号类型非空
     *
     * @param pageParams
     * @return
     */
    PageList<FinanceAccountValidator> pageSystemAccount(PageParams<FinanceAccountValidator> pageParams);

    /**
     * 账户退款
     * @param refundSum
     * @param accountId
     */
    @Modifying
    @Query(value = "update finance_account set ACCOUNT_TOTAL_SUM = ACCOUNT_TOTAL_SUM-:refundSum,ACCOUNT_USABLE_SUM =ACCOUNT_USABLE_SUM-:refundSum where ACCOUNT_ID = :accountId",nativeQuery = true)
    void refund(@Param("refundSum") BigDecimal refundSum, @Param("accountId") String accountId);
}
