package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import com.smoc.cloud.customer.entity.EnterpriseWebAccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 企业WEB登录操作类
 */
public interface EnterpriseWebRepository extends CrudRepository<EnterpriseWebAccountInfo, String>, JpaRepository<EnterpriseWebAccountInfo, String> {


    Iterable<EnterpriseWebAccountInfo> findByWebLoginName(String webLoginName);

    List<EnterpriseWebAccountInfoValidator> page(EnterpriseWebAccountInfoValidator enterpriseWebAccountInfoValidator);

    @Modifying
    @Query(value = "update enterprise_web_account_info set ACCOUNT_STATUS = :status where ID = :id",nativeQuery = true)
    void updateAccountStatus(@Param("id") String id, @Param("status") String status);

    //注销、启用WEB账号状态
    @Modifying
    @Query(value = "update enterprise_web_account_info set ACCOUNT_STATUS = :status where ENTERPRISE_ID = :enterpriseId",nativeQuery = true)
    void batchWebAccountStatusByentErpriseId(@Param("enterpriseId") String enterpriseId, @Param("status") String status);

    /**
     * 查询所有web账号
     * @param params
     * @return
     */
    PageList<EnterpriseWebAccountInfoValidator> webAll(PageParams<EnterpriseWebAccountInfoValidator> params);
}
