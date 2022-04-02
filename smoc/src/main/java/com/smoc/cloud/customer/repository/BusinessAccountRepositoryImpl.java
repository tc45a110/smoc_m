package com.smoc.cloud.customer.repository;


import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.customer.rowmapper.AccountBasicInfoRowMapper;
import com.smoc.cloud.customer.rowmapper.AccountCenterInfoRowMapper;
import com.smoc.cloud.customer.rowmapper.MessageAccountInfoRowMapper;
import com.smoc.cloud.customer.rowmapper.MessageAccountRowMapper;
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

    public  List<AccountBasicInfoValidator> findBusinessAccountByEnterpriseId(String enterpriseId) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.ACCOUNT_NAME");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.EXTEND_CODE");
        sqlBuffer.append(", t.ACCOUNT_STATUS");
        sqlBuffer.append(", e.PROTOCOL");
        sqlBuffer.append("  from account_base_info t left join account_interface_info e on t.ACCOUNT_ID = e.ACCOUNT_ID");
        sqlBuffer.append("  where t.ENTERPRISE_ID = ? ");

        List<Object> paramsList = new ArrayList<Object>();
        paramsList.add(enterpriseId);

        sqlBuffer.append("  order by t.BUSINESS_TYPE,t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<AccountBasicInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new AccountCenterInfoRowMapper());
        return list;
    }

    public  List<AccountBasicInfoValidator> findBusinessAccount(AccountBasicInfoValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.ACCOUNT_NAME");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.EXTEND_CODE");
        sqlBuffer.append(", t.ACCOUNT_STATUS");
        sqlBuffer.append(", ''PROTOCOL");
        sqlBuffer.append("  from account_base_info t LEFT JOIN account_interface_info a on t.ACCOUNT_ID=a.ACCOUNT_ID ");
        sqlBuffer.append("  where a.PROTOCOL='WEB' ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID = ?");
            paramsList.add(qo.getEnterpriseId().trim());
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ?");
            paramsList.add(qo.getBusinessType().trim());
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE like ?");
            paramsList.add( "%"+qo.getInfoType().trim()+"%");
        }

        if (!StringUtils.isEmpty(qo.getAccountStatus())) {
            sqlBuffer.append(" and t.ACCOUNT_STATUS = ?");
            paramsList.add(qo.getAccountStatus().trim());
        }

        sqlBuffer.append("  order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<AccountBasicInfoValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new AccountCenterInfoRowMapper());
        return list;
    }

    public  List<MessageAccountValidator> messageAccountList(MessageAccountValidator qo) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.ENTERPRISE_ID");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", a.ACCOUNT_USABLE_SUM");
        sqlBuffer.append("  from account_base_info t left join finance_account a on t.ACCOUNT_ID=a.ACCOUNT_ID ");
        sqlBuffer.append("  where 1 = 1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID = ?");
            paramsList.add(qo.getEnterpriseId().trim());
        }

        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE = ?");
            paramsList.add(qo.getBusinessType().trim());
        }

        sqlBuffer.append("  order by t.CREATED_TIME ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        List<MessageAccountValidator> list = this.queryForObjectList(sqlBuffer.toString(), params, new MessageAccountRowMapper());
        return list;
    }

    public PageList<MessageAccountValidator> messageAccountInfoList(PageParams<MessageAccountValidator> pageParams) {

        //查询条件
        MessageAccountValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ACCOUNT_ID");
        sqlBuffer.append(", t.ENTERPRISE_ID");
        sqlBuffer.append(", t.BUSINESS_TYPE");
        sqlBuffer.append(", t.CARRIER");
        sqlBuffer.append(", t.INFO_TYPE");
        sqlBuffer.append(", t.ACCOUNT_STATUS");
        sqlBuffer.append(", a.PAY_TYPE");
        sqlBuffer.append(", i.PROTOCOL");
        sqlBuffer.append(", DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME");
        sqlBuffer.append(", f.PARAM_VALUE AS SEND_LIMIT");
        sqlBuffer.append(", f1.PARAM_VALUE AS DAY_LIMIT");
        sqlBuffer.append(", f2.PARAM_VALUE AS MASK_AREA");
        sqlBuffer.append("  from account_base_info t left join (select PAY_TYPE,ACCOUNT_ID from account_finance_info group by ACCOUNT_ID)a on t.ACCOUNT_ID=a.ACCOUNT_ID ");
        sqlBuffer.append("  left join account_interface_info i on t.ACCOUNT_ID=i.ACCOUNT_ID");
        sqlBuffer.append("  left join parameter_extend_filters_value f on t.ACCOUNT_ID=f.BUSINESS_ID and f.BUSINESS_TYPE='BUSINESS_ACCOUNT_FILTER' and f.PARAM_KEY='SEND_RATE_LIMIT_SECOND'");
        sqlBuffer.append("  left join parameter_extend_filters_value f1 on t.ACCOUNT_ID=f1.BUSINESS_ID and f1.BUSINESS_TYPE='BUSINESS_ACCOUNT_FILTER' and f1.PARAM_KEY='SEND_LIMIT_NUMBER_DAILY'");
        sqlBuffer.append("  left join parameter_extend_filters_value f2 on t.ACCOUNT_ID=f2.BUSINESS_ID and f2.BUSINESS_TYPE='BUSINESS_ACCOUNT_FILTER' and f2.PARAM_KEY='MASK_PROVINCE'");
        sqlBuffer.append("  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID = ?");
            paramsList.add(qo.getEnterpriseId().trim());
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

        if (!StringUtils.isEmpty(qo.getProtocol())) {
            sqlBuffer.append(" and i.PROTOCOL = ?");
            paramsList.add(qo.getProtocol().trim());
        }

        if (!StringUtils.isEmpty(qo.getInfoType())) {
            sqlBuffer.append(" and t.INFO_TYPE like ?");
            paramsList.add( "%"+qo.getInfoType().trim()+"%");
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<MessageAccountValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new MessageAccountInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
}
