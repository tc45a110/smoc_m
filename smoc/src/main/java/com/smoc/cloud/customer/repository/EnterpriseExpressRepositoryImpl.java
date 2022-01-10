package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseExpressInfoValidator;
import com.smoc.cloud.customer.rowmapper.EnterpriseExpressInfoRowMapper;
import com.smoc.cloud.customer.rowmapper.EnterpriseWebAccountInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/28 15:44
 **/
public class EnterpriseExpressRepositoryImpl extends BasePageRepository {


    public List<EnterpriseExpressInfoValidator> page(EnterpriseExpressInfoValidator qo) {
        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.ENTERPRISE_ID");
        sqlBuffer.append(", t.POST_REMARK");
        sqlBuffer.append(", t.POST_CONTACTS");
        sqlBuffer.append(", t.POST_PHONE");
        sqlBuffer.append(", t.POST_ADDRESS");
        sqlBuffer.append(", t.POST_STATUS");
        sqlBuffer.append("  from enterprise_express_info t  ");
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

        List<EnterpriseExpressInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new EnterpriseExpressInfoRowMapper());
        return list;
    }

}
