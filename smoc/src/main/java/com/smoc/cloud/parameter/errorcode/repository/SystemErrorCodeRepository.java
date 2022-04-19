package com.smoc.cloud.parameter.errorcode.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.parameter.SystemErrorCodeValidator;
import com.smoc.cloud.parameter.errorcode.entity.SystemErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SystemErrorCodeRepository extends JpaRepository<SystemErrorCode, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<SystemErrorCodeValidator> page(PageParams<SystemErrorCodeValidator> pageParams);

    List<SystemErrorCode> findByCodeTypeAndErrorCode(String codeType, String errorCode);

    void bathSave(SystemErrorCodeValidator systemErrorCodeValidator);
}