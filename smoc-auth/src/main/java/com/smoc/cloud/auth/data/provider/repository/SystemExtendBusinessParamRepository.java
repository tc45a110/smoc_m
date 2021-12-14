package com.smoc.cloud.auth.data.provider.repository;


import com.smoc.cloud.auth.data.provider.entity.SystemExtendBusinessParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * 系统共用业务扩展参数 管理
 */
public interface SystemExtendBusinessParamRepository extends JpaRepository<SystemExtendBusinessParam, String>, JpaSpecificationExecutor<SystemExtendBusinessParam> {

    /**
     * 按业务类型查询系统扩展参数
     * @param businessType
     * @param paramStatus
     * @return
     */
    List<SystemExtendBusinessParam> findSystemExtendBusinessParamByBusinessTypeAndParamStatusOrderByDisplaySortAsc(String businessType, String paramStatus);

    /**
     * 按业务类型,参数key查询系统扩展参数
     * @param businessType
     * @param paramKey
     * @return
     */
    List<SystemExtendBusinessParam> findSystemExtendBusinessParamByBusinessTypeAndParamKeyAndParamStatus(String businessType,String paramKey,String paramStatus);

    /**
     *
     * @param id
     * @param status
     */
    @Modifying
    @Query(value = "update system_extend_business_param set PARAM_STATUS = :status where ID = :id",nativeQuery = true)
    void updateSystemExtendBusinessParamStatus(@Param("id") String id,@Param("status") String status);
}
