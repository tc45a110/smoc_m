package com.smoc.cloud.identification.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationAccountInfoValidator;
import com.smoc.cloud.identification.rowmapper.IdentificationAccountInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class IdentificationAccountInfoRepositoryImpl extends BasePageRepository {

    public PageList<IdentificationAccountInfoValidator> page(PageParams<IdentificationAccountInfoValidator> pageParams) {

        //查询条件
        IdentificationAccountInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID,");
        sqlBuffer.append("  t.ENTERPRISE_ID,");
        sqlBuffer.append("  e.ENTERPRISE_NAME,");
        sqlBuffer.append("  t.IDENTIFICATION_ACCOUNT,");
        sqlBuffer.append("  t.MD5_HMAC_KEY,");
        sqlBuffer.append("  t.AES_KEY,");
        sqlBuffer.append("  t.AES_IV,");
        sqlBuffer.append("  t.IDENTIFICATION_PRICE,");
        sqlBuffer.append("  t.IDENTIFICATION_FACE_PRICE,");
        sqlBuffer.append("  t.ACCOUNT_TYPE,");
        sqlBuffer.append("  t.ACCOUNT_STATUS,");
        sqlBuffer.append("  t.CREATED_BY,");
        sqlBuffer.append("  DATE_FORMAT(t.CREATED_TIME, '%Y-%m-%d %H:%i:%S')CREATED_TIME ");
        sqlBuffer.append("  from identification_account_info t,enterprise_basic_info e ");
        sqlBuffer.append("  where t.ENTERPRISE_ID = e.ENTERPRISE_ID ");

        List<Object> paramsList = new ArrayList<Object>();

        //企业名称
        if (!StringUtils.isEmpty(qo.getEnterpriseName())) {
            sqlBuffer.append(" and e.ENTERPRISE_NAME like ?");
            paramsList.add("%" + qo.getEnterpriseName().trim() + "%");
        }
        //认证账号
        if (!StringUtils.isEmpty(qo.getIdentificationAccount())) {
            sqlBuffer.append(" and e.IDENTIFICATION_ACCOUNT =?");
            paramsList.add(qo.getIdentificationAccount().trim());
        }
        //账号状态
        if (!StringUtils.isEmpty(qo.getAccountStatus())) {
            sqlBuffer.append(" and e.ACCOUNT_STATUS =?");
            paramsList.add(qo.getAccountStatus().trim());
        }
        //账号类型
        if (!StringUtils.isEmpty(qo.getAccountStatus())) {
            sqlBuffer.append(" and e.ACCOUNT_TYPE =?");
            paramsList.add(qo.getAccountType().trim());
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<IdentificationAccountInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new IdentificationAccountInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
}
