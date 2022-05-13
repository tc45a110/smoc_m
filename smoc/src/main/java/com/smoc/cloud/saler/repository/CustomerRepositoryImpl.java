package com.smoc.cloud.saler.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.saler.qo.CustomerAccountInfoQo;
import com.smoc.cloud.saler.rowmapper.CustomerAccountInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/28 15:44
 **/
public class CustomerRepositoryImpl extends BasePageRepository {


    public PageList<CustomerAccountInfoQo> page(PageParams<CustomerAccountInfoQo> pageParams){

        //查询条件
        CustomerAccountInfoQo qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.ACCOUNT_NAME");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.INDUSTRY_TYPE");
        sqlBuffer.append(", a.PAY_TYPE");
        sqlBuffer.append(", e.ENTERPRISE_NAME");
        sqlBuffer.append(", e.ENTERPRISE_TYPE");
        sqlBuffer.append(", e.ENTERPRISE_CONTACTS");
        sqlBuffer.append(", e.ENTERPRISE_CONTACTS_PHONE");
        sqlBuffer.append("  from account_base_info t left join (select PAY_TYPE,ACCOUNT_ID from account_finance_info group by ACCOUNT_ID)a on t.ACCOUNT_ID=a.ACCOUNT_ID ");
        sqlBuffer.append("  left join enterprise_basic_info e on t.ENTERPRISE_ID = e.ENTERPRISE_ID");
        sqlBuffer.append("  where e.SALER = ? ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(qo.getSalerId().trim());

        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ?");
            paramsList.add( "%"+qo.getEnterpriseName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getAccountName())) {
            sqlBuffer.append(" and t.ACCOUNT_NAME like ?");
            paramsList.add( "%"+qo.getAccountName().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getAccountId())) {
            sqlBuffer.append(" and t.ACCOUNT_ID like ?");
            paramsList.add( "%"+qo.getAccountId().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getCarrier())) {
            sqlBuffer.append(" and t.CARRIER like ?");
            paramsList.add( "%"+qo.getCarrier().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ?");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE like ?");
            paramsList.add( "%"+qo.getInfoType().trim()+"%");
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<CustomerAccountInfoQo> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new CustomerAccountInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }

}
