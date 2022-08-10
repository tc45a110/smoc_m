package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterValidator;
import com.smoc.cloud.customer.entity.AccountSignRegister;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountSignRegisterRepository extends JpaRepository<AccountSignRegister, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<AccountSignRegisterValidator> page(PageParams<AccountSignRegisterValidator> pageParams);
}
