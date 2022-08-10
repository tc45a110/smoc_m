package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseSignCertifyValidator;
import com.smoc.cloud.customer.entity.EnterpriseSignCertify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnterpriseSignCertifyRepository extends JpaRepository<EnterpriseSignCertify, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<EnterpriseSignCertifyValidator> page(PageParams<EnterpriseSignCertifyValidator> pageParams);
}
