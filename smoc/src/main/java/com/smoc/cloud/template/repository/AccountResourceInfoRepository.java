package com.smoc.cloud.template.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.template.AccountResourceInfoValidator;
import com.smoc.cloud.template.entity.AccountResourceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountResourceInfoRepository extends JpaRepository<AccountResourceInfo, String> {


    PageList<AccountResourceInfoValidator> page(PageParams<AccountResourceInfoValidator> pageParams);

    @Modifying
    @Query(value = "update account_resource_info set RESOURCE_STATUS = :status where ID=:id ",nativeQuery = true)
    void updateStatus(@Param("id") String id, @Param("status") String status);
}