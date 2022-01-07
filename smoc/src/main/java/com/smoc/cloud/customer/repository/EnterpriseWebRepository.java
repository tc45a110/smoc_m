package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import com.smoc.cloud.customer.entity.EnterpriseWebAccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 企业WEB登录操作类
 */
public interface EnterpriseWebRepository extends CrudRepository<EnterpriseWebAccountInfo, String>, JpaRepository<EnterpriseWebAccountInfo, String> {


    Iterable<EnterpriseWebAccountInfo> findByWebLoginName(String webLoginName);

    List<EnterpriseWebAccountInfoValidator> page(EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator);

    @Modifying
    @Query(value = "update enterprise_web_account_info set ACCOUNT_STATUS = :status where ID = :id",nativeQuery = true)
    void updateAccountStatus(String id, String status);
}
