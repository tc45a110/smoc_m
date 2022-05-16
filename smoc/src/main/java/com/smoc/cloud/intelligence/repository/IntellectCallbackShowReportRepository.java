package com.smoc.cloud.intelligence.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackShowReportValidator;
import com.smoc.cloud.intelligence.entity.IntellectCallbackShowReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntellectCallbackShowReportRepository extends JpaRepository<IntellectCallbackShowReport, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<IntellectCallbackShowReportValidator> page(PageParams<IntellectCallbackShowReportValidator> pageParams);
}