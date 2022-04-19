package com.smoc.cloud.http.repository;

import com.smoc.cloud.http.entity.AccountTemplateInfo;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

public class AccountTemplateInfoRepositoryImpl {

    @Resource
    public JdbcTemplate jdbcTemplate;

    /**
     * 保存模板信息
     *
     * @param template
     */
    public void saveHandle(AccountTemplateInfo template) {
        StringBuffer sql = new StringBuffer("insert into account_template_info(TEMPLATE_ID,TEMPLATE_TYPE,ENTERPRISE_ID,INFO_TYPE,BUSINESS_ACCOUNT,TEMPLATE_TITLE,TEMPLATE_CONTENT,TEMPLATE_FLAG,MM_ATTCHMENT,TEMPLATE_STATUS,TEMPLATE_AGREEMENT_TYPE,CREATED_BY,CREATED_TIME)");
        sql.append(" select '" + template.getTemplateId() + "','" + template.getTemplateType() + "',a.ENTERPRISE_ID,a.INFO_TYPE,'"+template.getBusinessAccount()+"','"+template.getTemplateTitle()+"','"+template.getTemplateContent()+"','"+template.getTemplateFlag()+"','"+template.getMmAttchment()+"','"+template.getTemplateStatus()+"','"+template.getTemplateAgreementType()+"','" + template.getCreatedBy() + "',now() from account_base_info a where ACCOUNT_ID = '" + template.getBusinessAccount() + "'");

        jdbcTemplate.update(sql.toString());
    }
}
