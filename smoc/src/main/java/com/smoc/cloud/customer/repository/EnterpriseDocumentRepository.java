package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseDocumentInfoValidator;
import com.smoc.cloud.customer.entity.EnterpriseDocumentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * 企业资质操作类
 */
public interface EnterpriseDocumentRepository extends CrudRepository<EnterpriseDocumentInfo, String>, JpaRepository<EnterpriseDocumentInfo, String> {


    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    PageList<EnterpriseDocumentInfoValidator> page(PageParams<EnterpriseDocumentInfoValidator> pageParams);

    /**
     * 查重
     * @param enterpriseId
     * @param signName
     * @param s
     * @return
     */
    List<EnterpriseDocumentInfo> findByEnterpriseIdAndSignNameAndDocStatus(String enterpriseId, String signName, String s);

    @Modifying
    @Query(value = "update enterprise_document_info set DOC_STATUS = :docStatus, CHECK_DATE= :checkDate where ID = :id ",nativeQuery = true)
    void updateStatusById(@Param("id") String id, @Param("docStatus") String docStatus, @Param("checkDate") String checkDate);

    /**
     * 查询签名
     * @param enterpriseDocumentInfoValidator
     * @return
     */
    List<EnterpriseDocumentInfoValidator> findMessageSign(EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator);
}
