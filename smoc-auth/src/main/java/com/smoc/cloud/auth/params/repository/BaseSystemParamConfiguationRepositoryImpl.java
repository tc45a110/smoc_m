package com.smoc.cloud.auth.params.repository;

import com.smoc.cloud.auth.common.page.BasePageRepository;
import com.smoc.cloud.auth.params.rowmapper.BaseSystemParamConfiguationRowMapper;
import com.smoc.cloud.common.auth.qo.ConfiguationParams;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020/5/30 14:47
 **/
@Slf4j
public class BaseSystemParamConfiguationRepositoryImpl extends BasePageRepository {


    public List<ConfiguationParams> findConfiguationParams(ConfiguationParams param) {

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  ID");
        sqlBuffer.append(", USER_ID");
        sqlBuffer.append(", PARAM_CODE");
        sqlBuffer.append(", PARAM_VALUE");
        sqlBuffer.append(", PARAM_VALUE_DESC");
        sqlBuffer.append(", STATUS");
        sqlBuffer.append(", DATA_DATE");
        sqlBuffer.append("  from base_system_param_configuation  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();
        if (!StringUtils.isEmpty(param.getUserId())) {
            sqlBuffer.append(" and USER_ID = ?");
            paramsList.add(param.getUserId().trim());
        }

        if (!StringUtils.isEmpty(param.getParamCode())) {
            sqlBuffer.append(" and PARAM_CODE = ?");
            paramsList.add(param.getParamCode().trim());
        }

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        List<ConfiguationParams> list = jdbcTemplate.query(sqlBuffer.toString(), params, new BaseSystemParamConfiguationRowMapper());

        return list;
    }


    //分页查询
    public PageList<ConfiguationParams> page(PageParams<ConfiguationParams> pageParams) {

        ConfiguationParams qo = pageParams.getParams();

        //查询sql
        StringBuilder sqlBuffer = new StringBuilder("select ");
        sqlBuffer.append("  ID");
        sqlBuffer.append(", USER_ID");
        sqlBuffer.append(", PARAM_CODE");
        sqlBuffer.append(", PARAM_VALUE");
        sqlBuffer.append(", PARAM_VALUE_DESC");
        sqlBuffer.append(", STATUS");
        sqlBuffer.append(", DATA_DATE");
        sqlBuffer.append("  from base_system_param_configuation  where 1=1 ");

        List<Object> paramsList = new ArrayList<Object>();
        if (!StringUtils.isEmpty(qo.getUserId())) {
            sqlBuffer.append(" and USER_ID = ?");
            paramsList.add(qo.getUserId());
        }

        if (!StringUtils.isEmpty(qo.getParamCode())) {
            sqlBuffer.append(" and PARAM_CODE = ?");
            paramsList.add(qo.getParamCode());
        }

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);
        PageList<ConfiguationParams> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new BaseSystemParamConfiguationRowMapper());
        pageList.getPageParams().setParams(qo);
        return pageList;

    }
}
