package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import com.smoc.cloud.customer.rowmapper.EnterpriseWebAccountInfoRowMapper;
import org.springframework.util.StringUtils;

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
        sqlBuffer.append(", t.WEB_LOGIN_NAME");
        sqlBuffer.append(", t.ACCOUNT_STATUS");
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

}
