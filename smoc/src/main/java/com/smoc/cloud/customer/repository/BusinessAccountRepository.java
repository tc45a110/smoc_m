package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.customer.entity.AccountBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 业务账号操作类
 */
public interface BusinessAccountRepository extends CrudRepository<AccountBasicInfo, String>, JpaRepository<AccountBasicInfo, String> {

    /**
     * 查询-分页
     * @param pageParams
     * @return
     */
    PageList<AccountBasicInfoValidator> page(PageParams<AccountBasicInfoValidator> pageParams);


    Iterable<AccountBasicInfo> findByAccountId(String account);

    /**
     * 注销、启用业务账号
     * @param id
     * @param status
     */
    @Modifying
    @Query(value = "update account_base_info set ACCOUNT_STATUS = :status where ACCOUNT_ID = :id",nativeQuery = true)
    void updateAccountStatusById(@Param("id") String id, @Param("status") String status);

    /**
     * 查询企业所有的业务账号
     * @param enterpriseId
     * @return
     */
    List<AccountBasicInfoValidator> findBusinessAccountByEnterpriseId(String enterpriseId);
}
