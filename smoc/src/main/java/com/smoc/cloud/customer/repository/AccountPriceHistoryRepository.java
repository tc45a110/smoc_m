package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.AccountPriceHistoryValidator;
import com.smoc.cloud.customer.entity.AccountPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountPriceHistoryRepository extends JpaRepository<AccountPriceHistory, String> {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    PageList<AccountPriceHistoryValidator> page(PageParams<AccountPriceHistoryValidator> pageParams);
}
