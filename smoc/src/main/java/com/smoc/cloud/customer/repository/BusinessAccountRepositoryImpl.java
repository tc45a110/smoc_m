package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.customer.rowmapper.AccountBasicInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/28 15:44
 **/
public class BusinessAccountRepositoryImpl extends BasePageRepository {


    public PageList<AccountBasicInfoValidator> page(PageParams<AccountBasicInfoValidator> pageParams) {

        //查询条件
        AccountBasicInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.ENTERPRISE_ID");
        sqlBuffer.append(", t.ACCOUNT_NAME");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.EXTEND_CODE");
        sqlBuffer.append(", t.ACCOUNT_PROCESS");
        sqlBuffer.append(", t.ACCOUNT_STATUS");
        sqlBuffer.append(", t.ACCOUNT_CHANNEL_TYPE");
        sqlBuffer.append(", e.ENTERPRISE_NAME");
        sqlBuffer.append("  from account_base_info t left join enterprise_basic_info e on t.ENTERPRISE_ID = e.ENTERPRISE_ID");
        sqlBuffer.append("  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

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

        PageList<AccountBasicInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new AccountBasicInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }


}
