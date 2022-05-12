package com.smoc.cloud.intelligence.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.intelligence.IntellectShortUrlValidator;
import com.smoc.cloud.common.smoc.intelligence.IntellectTemplateInfoValidator;
import com.smoc.cloud.intelligence.entity.IntellectShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntellectShortUrlRepository extends JpaRepository<IntellectShortUrl, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<IntellectShortUrlValidator> page(PageParams<IntellectShortUrlValidator> pageParams);
}