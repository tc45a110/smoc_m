package com.smoc.cloud.finance.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRefundValidator;
import com.smoc.cloud.finance.entity.FinanceAccountRefund;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FinanceAccountRefundRepository extends JpaRepository<FinanceAccountRefund, String> {

    /**
     * 分页查询  业务账号退款记录
     * @param pageParams
     * @return
     */
    PageList<FinanceAccountRefundValidator> pageBusiness(PageParams<FinanceAccountRefundValidator> pageParams);

}