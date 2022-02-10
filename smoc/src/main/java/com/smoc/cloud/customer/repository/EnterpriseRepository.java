package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.customer.entity.EnterpriseBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 企业接入操作类
 */
public interface EnterpriseRepository extends CrudRepository<EnterpriseBasicInfo, String>, JpaRepository<EnterpriseBasicInfo, String> {


    Iterable<EnterpriseBasicInfo> findByEnterpriseName(String enterpriseName);

    PageList<EnterpriseBasicInfoValidator> page(PageParams<EnterpriseBasicInfoValidator> pageParams);

    @Modifying
    @Query(value = "update enterprise_basic_info set ENTERPRISE_STATUS = :status where ENTERPRISE_ID = :id",nativeQuery = true)
    void updateEnterpriseStatus(@Param("id") String id, @Param("status") String status);


    /**
     * 根据企业id，查询企业id和子企业id
     * @param enterpriseId
     * @return
     */
    List<String> findEnterpriseAndSubsidiaryId(String enterpriseId);
}
