package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.common.page.BasePageRepository;
import com.smoc.cloud.auth.data.provider.rowmapper.SystemUserLogRowMapper;
import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SystemUserLogRepositoryImpl extends BasePageRepository {

    public PageList<SystemUserLogValidator> page(PageParams<SystemUserLogValidator> pageParams) {

        //查询条件
        SystemUserLogValidator qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  t.ID");
        sqlBuffer.append(", t.USER_ID");
        sqlBuffer.append(", t.MODULE");
        sqlBuffer.append(", t.MODULE_ID");
        sqlBuffer.append(", t.OPERATION_TYPE");
        sqlBuffer.append(", t.SIMPLE_INTRODUCE");
        sqlBuffer.append(", t.CREATED_TIME");
        sqlBuffer.append("  from system_user_logs t where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getUserId())) {
            sqlBuffer.append(" and t.USER_ID = ?");
            paramsList.add(qo.getUserId().trim());
        }

        if (!StringUtils.isEmpty(qo.getModule())) {
            sqlBuffer.append(" and t.MODULE = ?");
            paramsList.add(qo.getModule().trim());
        }
        if (!StringUtils.isEmpty(qo.getModuleId())) {
            sqlBuffer.append(" and t.MODULE_ID = ?");
            paramsList.add(qo.getModuleId().trim());
        }

        //排序
        sqlBuffer.append(" order by t.CREATED_TIME desc");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<SystemUserLogValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new SystemUserLogRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;
    }
}
