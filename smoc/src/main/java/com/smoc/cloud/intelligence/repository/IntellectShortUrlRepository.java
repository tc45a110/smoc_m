package com.smoc.cloud.intelligence.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.intelligence.IntellectShortUrlValidator;
import com.smoc.cloud.common.smoc.intelligence.IntellectTemplateInfoValidator;
import com.smoc.cloud.intelligence.entity.IntellectMaterial;
import com.smoc.cloud.intelligence.entity.IntellectShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IntellectShortUrlRepository extends JpaRepository<IntellectShortUrl, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<IntellectShortUrlValidator> page(PageParams<IntellectShortUrlValidator> pageParams);

    List<IntellectShortUrl> findIntellectShortUrlByTplIdAndCustIdAndAimCodeAndAimUrl(String tplId,String custId,String aimCode,String aimUrl);

    @Modifying
    @Query(value = "update intellect_short_url set SUCCESS_ANALYSIS = SUCCESS_ANALYSIS+:successAnalysis where ID = :id ", nativeQuery = true)
    void addSuccessAnalysis(@Param("id") String id, @Param("successAnalysis") Integer successAnalysis);
}