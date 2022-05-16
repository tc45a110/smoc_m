package com.smoc.cloud.intelligence.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackTemplateStatusReportValidator;
import com.smoc.cloud.intelligence.entity.IntellectCallbackTemplateStatusReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IntellectCallbackTemplateStatusReportRepository extends JpaRepository<IntellectCallbackTemplateStatusReport, String> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<IntellectCallbackTemplateStatusReportValidator> page(PageParams<IntellectCallbackTemplateStatusReportValidator> pageParams);

    /**
     * @param tplId      模版id
     * @param status     模版状态
     * @param auditState 审核状态
     * @param auditDesc  审核意见
     */
    @Modifying
    @Query(value = "update intellect_template_info set TEMPLATE_STATUS = :status,TEMPLATE_CHECK_STATUS =:auditState,TEMPLATE_CHECK_MESSAGE=:auditDesc where TPL_ID = :tplId ", nativeQuery = true)
    void updateTemplateStatus(@Param("tplId") String tplId, @Param("status") Integer status, @Param("auditState") Integer auditState, @Param("auditDesc") String auditDesc);
}