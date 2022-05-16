package com.smoc.cloud.system.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.system.SystemAccountInfoValidator;
import com.smoc.cloud.system.rowmapper.SystemAccountInfoValidatorRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SystemAccountInfoRepositoryImpl extends BasePageRepository {

    public PageList<SystemAccountInfoValidator> page(PageParams<SystemAccountInfoValidator> pageParams) {

        //查询条件
        SystemAccountInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID,");
        sqlBuffer.append("  t.ENTERPRISE_ID,");
        sqlBuffer.append("  e.ENTERPRISE_NAME,");
        sqlBuffer.append("  t.ACCOUNT,");
        sqlBuffer.append("  t.MD5_HMAC_KEY,");
        sqlBuffer.append("  t.AES_KEY,");
        sqlBuffer.append("  t.AES_IV,");
        sqlBuffer.append("  t.PRICE,");
        sqlBuffer.append("  t.SECOND_PRICE,");
        sqlBuffer.append("  t.THIRD_PRICE,");
        sqlBuffer.append("  t.GRANTING_CREDIT,");
        sqlBuffer.append("  t.ACCOUNT_TYPE,");
        sqlBuffer.append("  t.BUSINESS_TYPE,");
        sqlBuffer.append("  t.IS_GATEWAY,");
        sqlBuffer.append("  t.ACCOUNT_STATUS,");
        sqlBuffer.append("  t.CREATED_BY,");
        sqlBuffer.append("  DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append("  from system_account_info t,enterprise_basic_info e ");
        sqlBuffer.append("  where t.ENTERPRISE_ID = e.ENTERPRISE_ID ");

        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ?");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }
        //企业ID
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID =?");
            paramsList.add(qo.getEnterpriseId().trim());
        }
        //账号
        if (!StringUtils.isEmpty(qo.getAccount())) {
            sqlBuffer.append(" and t.ACCOUNT =?");
            paramsList.add(qo.getAccount().trim());
        }
        //账号状态
        if (!StringUtils.isEmpty(qo.getAccountStatus())) {
            sqlBuffer.append(" and t.ACCOUNT_STATUS =?");
            paramsList.add(qo.getAccountStatus().trim());
        }
        //账号类型
        if (!StringUtils.isEmpty(qo.getAccountType())) {
            sqlBuffer.append(" and t.ACCOUNT_TYPE =?");
            paramsList.add(qo.getAccountType().trim());
        }
        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE =?");
            paramsList.add(qo.getBusinessType().trim());
        }
        sqlBuffer.append(" order by ACCOUNT_STATUS desc, t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<SystemAccountInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new SystemAccountInfoValidatorRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
}
