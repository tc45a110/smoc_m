package com.smoc.cloud.intelligence.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.intelligence.IntellectTemplateInfoValidator;
import com.smoc.cloud.intelligence.entity.IntellectTemplateInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntellectTemplateInfoRepository extends JpaRepository<IntellectTemplateInfo, String> {

    PageList<IntellectTemplateInfoValidator> page(PageParams<IntellectTemplateInfoValidator> pageParams);
}