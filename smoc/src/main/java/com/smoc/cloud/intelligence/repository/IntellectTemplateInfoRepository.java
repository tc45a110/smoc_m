package com.smoc.cloud.intelligence.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.intelligence.IntellectTemplateInfoValidator;
import com.smoc.cloud.intelligence.entity.IntellectTemplateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IntellectTemplateInfoRepository extends JpaRepository<IntellectTemplateInfo, String> {

    PageList<IntellectTemplateInfoValidator> page(PageParams<IntellectTemplateInfoValidator> pageParams);

    List<IntellectTemplateInfo> findIntellectTemplateInfoByTemplateId(String templateId);

    @Modifying
    @Query(value = "update intellect_template_info set TPL_ID = :tplId,TEMPLATE_CHECK_STATUS = :status  where TEMPLATE_ID = :templateId ", nativeQuery = true)
    void updateTplIdAndStatus(@Param("templateId") String templateId, @Param("tplId") String tplId, @Param("status") Integer status);

    @Modifying
    @Query(value = "update intellect_template_info set TEMPLATE_CHECK_STATUS = :status where TEMPLATE_ID = :templateId ", nativeQuery = true)
    void updateCheckStatus(@Param("templateId") String templateId, @Param("status") Integer status);

    @Modifying
    @Query(value = "update intellect_template_info set TEMPLATE_CHECK_STATUS = :status where TPL_ID = :tplId ", nativeQuery = true)
    void updateCheckStatusByTplId(@Param("tplId") String tplId, @Param("status") Integer status);

    @Modifying
    @Query(value = "update intellect_template_info set TEMPLATE_STATUS = :status where TEMPLATE_ID = :templateId ", nativeQuery = true)
    void updateStatusByTemplateId(@Param("templateId") String templateId, @Param("status") Integer status);
}