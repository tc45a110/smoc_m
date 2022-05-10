package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import com.smoc.cloud.customer.rowmapper.EnterpriseWebAccountInfoRowMapper;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.util.StringUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/28 15:44
 **/
public class EnterpriseWebRepositoryImpl extends BasePageRepository {


    public List<EnterpriseWebAccountInfoValidator> page(EnterpriseWebAccountInfoValidator qo) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.ENTERPRISE_ID");
        sqlBuffer.append(", '' ENTERPRISE_NAME");
        sqlBuffer.append(", t.WEB_LOGIN_NAME");
        sqlBuffer.append(", t.AES_PASSWORD");
        sqlBuffer.append(", t.ACCOUNT_STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from enterprise_web_account_info t  ");
        sqlBuffer.append("  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID =?");
            paramsList.add( qo.getEnterpriseId().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<EnterpriseWebAccountInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new EnterpriseWebAccountInfoRowMapper());
        return list;
    }

    public PageList<EnterpriseWebAccountInfoValidator> webAll(PageParams<EnterpriseWebAccountInfoValidator> pageParams) {

        EnterpriseWebAccountInfoValidator qo = pageParams.getParams();
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.ENTERPRISE_ID");
        sqlBuffer.append(", a.ENTERPRISE_NAME");
        sqlBuffer.append(", ''AES_PASSWORD");
        sqlBuffer.append(", t.ACCOUNT_STATUS");
        sqlBuffer.append(", t.ACCOUNT_STATUS");
        sqlBuffer.append(", t.CREATED_BY");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append("  from enterprise_web_account_info t left join enterprise_basic_info a on t.ENTERPRISE_ID = a.ENTERPRISE_ID ");
        sqlBuffer.append("  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID =?");
            paramsList.add( qo.getEnterpriseId().trim());
        }

        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and a.ENTERPRISE_NAME like ?");
            paramsList.add("%"+ qo.getEnterpriseName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getWebLoginName())) {
            sqlBuffer.append(" and t.WEB_LOGIN_NAME like ?");
            paramsList.add("%"+ qo.getWebLoginName().trim()+"%");
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<EnterpriseWebAccountInfoValidator> list = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new EnterpriseWebAccountInfoRowMapper());
        return list;
    }
}
