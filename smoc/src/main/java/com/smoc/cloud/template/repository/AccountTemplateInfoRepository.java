package com.smoc.cloud.template.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.template.entity.AccountTemplateContent;
import com.smoc.cloud.template.entity.AccountTemplateInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface AccountTemplateInfoRepository extends JpaRepository<AccountTemplateInfo, String> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<AccountTemplateInfoValidator> page(PageParams<AccountTemplateInfoValidator> pageParams);

    /**
     * 注销模板
     *
     * @param templateId
     */
    @Modifying
    @Query(value = "update account_template_info set TEMPLATE_STATUS = :templateStatus where TEMPLATE_ID=:templateId ", nativeQuery = true)
    void cancelTemplate(@Param("templateId") String templateId, @Param("templateStatus") String templateStatus);

    /**
     * 查询绑定账号的固定模版，包括CMPP类型
     *
     * @return
     */
    Map<String, String> findFixedTemplate();

    /**
     * 查询绑定账号的固定模版，包括CMPP类型
     *
     * @return
     */
    List<AccountTemplateContent> findFixedTemplate(String account);

    /**
     * 查询绑定账号的固定模版，包括HTTP、WEB类型
     *
     * @return
     */
    Map<String, String> findHttpFixedTemplate();

    /**
     * 查询绑定的变量模版，包括HTTP、WEB类型
     *
     * @return
     */
    Map<String, String> findHttpVariableTemplate();

    /**
     * 查询绑定账号的变量模版，而且该变量模版 匹配后，不在进行后续过滤
     *
     * @return
     */
    Map<String, String> findNoFilterVariableTemplate();

    /**
     * 查询绑定账号的变量模版，而且该变量模版 匹配后，不在进行后续过滤
     *
     * @return
     */
     List<AccountTemplateContent> findNoFilterVariableTemplate(String account);

    /**
     * 查询绑定账号的CMPP变量模版，该部分只查询匹配上模版后，需要继续过滤的模版
     *
     * @return
     */
    Map<String, String> findCMPPVariableTemplate();

    /**
     * 查询绑定账号的CMPP变量模版，该部分只查询匹配上模版后，需要继续过滤的模版
     *
     * @return
     */
    List<AccountTemplateContent> findCMPPVariableTemplate(String account);

    /**
     * 查询绑定账号的CMPP签名模版
     *
     * @return
     */
    Map<String, String> findCMPPSignTemplate();

    /**
     * 查询绑定账号的CMPP签名模版
     *
     * @return
     */
    List<AccountTemplateContent> findCMPPSignTemplate(String account);

}