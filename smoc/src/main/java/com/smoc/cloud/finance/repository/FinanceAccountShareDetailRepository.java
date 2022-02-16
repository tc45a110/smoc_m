package com.smoc.cloud.finance.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountShareDetailValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.finance.entity.FinanceAccountShareDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceAccountShareDetailRepository extends JpaRepository<FinanceAccountShareDetail, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<FinanceAccountShareDetailValidator> page(PageParams<FinanceAccountShareDetailValidator> pageParams);
}