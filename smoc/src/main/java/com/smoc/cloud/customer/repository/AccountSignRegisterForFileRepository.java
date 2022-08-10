package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterForFileValidator;
import com.smoc.cloud.customer.entity.AccountSignRegisterForFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountSignRegisterForFileRepository extends JpaRepository<AccountSignRegisterForFile, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<AccountSignRegisterForFileValidator> page(PageParams<AccountSignRegisterForFileValidator> pageParams);
}
