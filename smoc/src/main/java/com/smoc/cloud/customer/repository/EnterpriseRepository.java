package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.customer.entity.EnterpriseBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * 企业接入操作类
 */
public interface EnterpriseRepository extends CrudRepository<EnterpriseBasicInfo, String>, JpaRepository<EnterpriseBasicInfo, String> {


    Iterable<EnterpriseBasicInfo> findByEnterpriseNameAndEnterpriseParentId(String enterpriseName, String enterpriseParentId);

    PageList<EnterpriseBasicInfoValidator> page(PageParams<EnterpriseBasicInfoValidator> pageParams);
}
