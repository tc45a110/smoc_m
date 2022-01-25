package com.smoc.cloud.finance.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import com.smoc.cloud.finance.entity.FinanceAccountRecharge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceAccountRechargeRepository extends JpaRepository<FinanceAccountRecharge, String> {

    /**
     * 分页查询  认证账号充值记录
     * @param pageParams
     * @return
     */
    PageList<FinanceAccountRechargeValidator> pageIdentification(PageParams<FinanceAccountRechargeValidator> pageParams);
}