package com.smoc.cloud.customer.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseSignCertifyValidator;
import com.smoc.cloud.customer.entity.EnterpriseSignCertify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnterpriseSignCertifyRepository extends JpaRepository<EnterpriseSignCertify, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<EnterpriseSignCertifyValidator> page(PageParams<EnterpriseSignCertifyValidator> pageParams);

    /**
     * 注销、启用
     *
     * @param id
     * @param status
     */
    @Modifying
    @Query(value = "update enterprise_sign_certify set CERTIFY_STATUS = :status where ID = :id", nativeQuery = true)
    void delete(@Param("id") String id, @Param("status") String status);
}
