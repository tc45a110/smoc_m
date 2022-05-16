package com.smoc.cloud.finance.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.finance.entity.FinanceAccountRecharge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public interface FinanceAccountRechargeRepository extends JpaRepository<FinanceAccountRecharge, String> {

    /**
     * 分页查询  认证账号充值记录
     * @param pageParams
     * @return
     */
    PageList<FinanceAccountRechargeValidator> pageIdentification(PageParams<FinanceAccountRechargeValidator> pageParams);

    /**
     * 分页查询 共用账号充值记录
     * @param pageParams
     * @return
     */
     PageList<FinanceAccountRechargeValidator> pageSystem(PageParams<FinanceAccountRechargeValidator> pageParams);

    /**
     * 分页查询  业务账号充值记录
     * @param pageParams
     * @return
     */
    PageList<FinanceAccountRechargeValidator> pageBusiness(PageParams<FinanceAccountRechargeValidator> pageParams);

    /**
     * 统计充值金额
     *
     * @param qo
     * @return
     */
     Map<String, Object> countRechargeSum(FinanceAccountRechargeValidator qo);
}