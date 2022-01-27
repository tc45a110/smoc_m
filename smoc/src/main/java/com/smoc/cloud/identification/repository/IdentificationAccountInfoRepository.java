package com.smoc.cloud.identification.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationAccountInfoValidator;
import com.smoc.cloud.identification.entity.IdentificationAccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IdentificationAccountInfoRepository extends JpaRepository<IdentificationAccountInfo, String> {

    PageList<IdentificationAccountInfoValidator> page(PageParams<IdentificationAccountInfoValidator> pageParams);

    //注销认证账户
    @Modifying
    @Query(value = "update identification_account_info set ACCOUNT_STATUS = :status where ID = :id",nativeQuery = true)
    void logoutAccount(@Param("id") String id, @Param("status") String status);


}