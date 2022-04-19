package com.smoc.cloud.http.repository;

import com.smoc.cloud.http.entity.AccountTemplateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountTemplateInfoRepository extends JpaRepository<AccountTemplateInfo, String> {


    /**
     * 注销模板
     * @param templateId
     */
    @Modifying
    @Query(value = "update account_template_info set TEMPLATE_STATUS = :templateStatus where TEMPLATE_ID=:templateId ",nativeQuery = true)
    void cancelTemplate(@Param("templateId") String templateId,@Param("templateStatus") String templateStatus);

}