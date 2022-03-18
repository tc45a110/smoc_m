package com.smoc.cloud.template.repository;

import com.smoc.cloud.common.BasePageRepository;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.template.AccountResourceInfoValidator;
import com.smoc.cloud.template.rowmapper.AccountResourceInfoRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AccountResourceInfoRepositoryImpl extends BasePageRepository {


    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    public PageList<AccountResourceInfoValidator> page(PageParams<AccountResourceInfoValidator> pageParams) {
        //查询条件
        AccountResourceInfoValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append(" t.ID,");
        sqlBuffer.append(" t.RESOURCE_TYPE,");
        sqlBuffer.append(" t.ENTERPRISE_ID,");
        sqlBuffer.append(" t.BUSINESS_TYPE,");
        sqlBuffer.append(" t.RESOURCE_TITLE,");
        sqlBuffer.append(" t.RESOURCE_ATTCHMENT,");
        sqlBuffer.append(" t.RESOURCE_ATTCHMENT_SIZE,");
        sqlBuffer.append(" t.RESOURCE_ATTCHMENT_TYPE,");
        sqlBuffer.append(" t.RESOURCE_HEIGHT,");
        sqlBuffer.append(" t.RESOURCE_WIDTH,");
        sqlBuffer.append(" t.RESOURCE_STATUS");
        sqlBuffer.append(" from account_resource_info t ");
        sqlBuffer.append(" where t.RESOURCE_STATUS!=0 ");

        List<Object> paramsList = new ArrayList<Object>();

        //企业ID
        if (!StringUtils.isEmpty(qo.getEnterpriseId())) {
            sqlBuffer.append(" and t.ENTERPRISE_ID = ? ");
            paramsList.add(qo.getEnterpriseId().trim());
        }

        //业务类型
        if (!StringUtils.isEmpty(qo.getBusinessType())) {
            sqlBuffer.append(" and t.BUSINESS_TYPE =?");
            paramsList.add(qo.getBusinessType().trim());
        }

        //资源类型
        if (!StringUtils.isEmpty(qo.getResourceType())) {
            sqlBuffer.append(" and t.RESOURCE_TYPE =?");
            paramsList.add(qo.getResourceType().trim());
        }

        //模板内容
        if (!StringUtils.isEmpty(qo.getResourceTitle())) {
            sqlBuffer.append(" and t.RESOURCE_TITLE like ? ");
            paramsList.add("%" + qo.getResourceTitle().trim() + "%");
        }

        if(!StringUtils.isEmpty(qo.getResourceAttchmentType())) {
            sqlBuffer.append(" and (");
            boolean isFirst = true;
            for (String suffixName : qo.getResourceAttchmentType().split(",")) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sqlBuffer.append(" or ");
                }
                sqlBuffer.append("RESOURCE_ATTCHMENT_TYPE = ?");
                paramsList.add(suffixName);
            }
            sqlBuffer.append(")");
        }

        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<AccountResourceInfoValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new AccountResourceInfoRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
}
