package com.smoc.cloud.template.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.template.entity.AccountTemplateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountTemplateInfoRepository extends JpaRepository<AccountTemplateInfo, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<AccountTemplateInfoValidator> page(PageParams<AccountTemplateInfoValidator> pageParams);

    /**
     * 注销模板
     * @param templateId
     */
    @Modifying
    @Query(value = "update account_template_info set TEMPLATE_STATUS = '0' where TEMPLATE_ID=:templateId ",nativeQuery = true)
    void cancelTemplate(@Param("templateId") String templateId);

}