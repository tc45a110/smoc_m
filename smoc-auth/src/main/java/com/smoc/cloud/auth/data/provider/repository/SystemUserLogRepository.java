package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.SystemUserLog;
import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 用户操作日志
 */
public interface SystemUserLogRepository extends JpaRepository<SystemUserLog, String>, JpaSpecificationExecutor<SystemUserLog> {


    /**
     * 分页查询
     *
     * @param pageParams 分页参数
     * @return
     */
    PageList page(PageParams<SystemUserLogValidator> pageParams);
}