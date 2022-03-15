package com.smoc.cloud.template.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.template.rowmapper.AccountTemplateInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AccountTemplateInfoRepositoryImpl extends BasePageRepository {


    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<AccountTemplateInfoValidator> page(PageParams<AccountTemplateInfoValidator> pageParams) {
        //查询条件
        AccountTemplateInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");

        sqlBuffer.append(" t.TEMPLATE_ID,");
        sqlBuffer.append(" e.ENTERPRISE_NAME,");
        sqlBuffer.append(" a.ACCOUNT_NAME,");
        sqlBuffer.append(" a.BUSINESS_TYPE,");
        sqlBuffer.append(" t.BUSINESS_ACCOUNT,");
        sqlBuffer.append(" t.TEMPLATE_TYPE,");
        sqlBuffer.append(" t.TEMPLATE_FLAG,");
        sqlBuffer.append(" t.SIGN_NAME,");
        sqlBuffer.append(" t.TEMPLATE_CONTENT,");
        sqlBuffer.append(" DATE_FORMAT(t.CHECK_DATE, '%Y-%m-%d %H:%i:%S')CHECK_DATE, ");
        sqlBuffer.append(" t.CHECK_BY,");
        sqlBuffer.append(" t.CHECK_OPINIONS,");
        sqlBuffer.append(" t.CHECK_STATUS,");
        sqlBuffer.append(" t.TEMPLATE_STATUS,");
        sqlBuffer.append(" t.TEMPLATE_AGREEMENT_TYPE,");
        sqlBuffer.append(" t.CREATED_BY,");
        sqlBuffer.append(" DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append(" from account_template_info t,account_base_info a,enterprise_basic_info e ");
        sqlBuffer.append(" where t.BUSINESS_ACCOUNT = a.ACCOUNT_ID and a.ENTERPRISE_ID = e.ENTERPRISE_ID ");

        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ? ");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }

        //企业ID
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and e.ENTERPRISE_ID = ? ");
            paramsList.add(qo.getEnterpriseId().trim());
        }

        //业务账号
        if (!StringUtils.isEmpty(qo.getBusinessAccount())) {
            sqlBuffer.append(" and t.BUSINESS_ACCOUNT like ?");
            paramsList.add("%" + qo.getBusinessAccount().trim() + "%");
        }

        //业务类型
        if (!StringUtils.isEmpty(qo.getTemplateType())) {
            sqlBuffer.append(" and a.BUSINESS_TYPE =?");
            paramsList.add(qo.getTemplateType().trim());
        }

        //业务账号名称
        if (!StringUtils.isEmpty(qo.getAccountName())) {
            sqlBuffer.append(" and a.ACCOUNT_NAME like ? ");
            paramsList.add("%" + qo.getAccountName().trim() + "%");
        }

        //业务类型
        if (!StringUtils.isEmpty(qo.getTemplateStatus())) {
            sqlBuffer.append(" and t.TEMPLATE_STATUS =?");
            paramsList.add(qo.getTemplateStatus().trim());
        }

        //业务类型
        if (!StringUtils.isEmpty(qo.getTemplateAgreementType())) {
            sqlBuffer.append(" and t.TEMPLATE_AGREEMENT_TYPE =?");
            paramsList.add(qo.getTemplateAgreementType().trim());
        }

        //模板标识
        if (!StringUtils.isEmpty(qo.getTemplateFlag())) {
            sqlBuffer.append(" and t.TEMPLATE_FLAG = ? ");
            paramsList.add( qo.getTemplateFlag().trim() );
        }

        //模板内容
        if (!StringUtils.isEmpty(qo.getTemplateContent())) {
            sqlBuffer.append(" and t.TEMPLATE_CONTENT like ? ");
            paramsList.add("%" + qo.getTemplateContent().trim() + "%");
        }

        //签名
        if (!StringUtils.isEmpty(qo.getSignName())) {
            sqlBuffer.append(" and t.SIGN_NAME like ? ");
            paramsList.add("%" + qo.getSignName().trim() + "%");
        }

        //时间起
        if (!StringUtils.isEmpty(qo.getStartDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MESSAGE_DATE,'%Y-%m-%d') >=? ");
            paramsList.add(qo.getStartDate().trim());
        }
        //时间止
        if (!StringUtils.isEmpty(qo.getEndDate())) {
            sqlBuffer.append(" and DATE_FORMAT(t.MESSAGE_DATE,'%Y-%m-%d') <=? ");
            paramsList.add(qo.getEndDate().trim());
        }

        sqlBuffer.append(" order by t.TEMPLATE_STATUS desc,t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<AccountTemplateInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new AccountTemplateInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
}
