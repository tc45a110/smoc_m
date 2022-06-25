package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.common.page.BasePageRepository;
import com.smoc.cloud.auth.data.provider.rowmapper.DictInfoRowMapper;
import com.smoc.cloud.auth.data.provider.rowmapper.DictRowMapper;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.validator.DictValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class BaseCommDictRepositoryImpl extends BasePageRepository {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public List<Dict> findDictByTypeId(String typeId) {

        String  sql = "select ID, DICT_NAME, DICT_CODE from base_comm_dict where ACTIVE =1 and TYPE_ID=? order by sort ASC ";

        Object[] params = new Object[1];
        params[0] = typeId;

        List<Dict> dicts = jdbcTemplate.query(sql,params, new DictRowMapper());

        return dicts;

    }

    public PageList<DictValidator> page(PageParams<DictValidator> pageParams) {

        DictValidator qo = pageParams.getParams();

        StringBuilder sqlBuffer = new StringBuilder("select ID, DICT_NAME, DICT_CODE ,SORT from base_comm_dict where ACTIVE =1 ");

        List<Object> paramsList = new ArrayList<Object>();

        if (!StringUtils.isEmpty(qo.getDictType())) {
            sqlBuffer.append(" and DICT_TYPE = ?");
            paramsList.add(qo.getDictType().trim());
        }

        if (!StringUtils.isEmpty(qo.getDictName())) {
            sqlBuffer.append(" and DICT_NAME = ?");
            paramsList.add(qo.getDictName().trim());
        }

        if (!StringUtils.isEmpty(qo.getDictCode())) {
            sqlBuffer.append(" and DICT_CODE = ?");
            paramsList.add(qo.getDictCode().trim());
        }

        sqlBuffer.append(" order by sort ASC ");

        //根据参数个数，组织参数值
        Object[] params = new Object[paramsList.size()];
        paramsList.toArray(params);

        PageList<DictValidator> pageList = this.queryByPageForMySQL(sqlBuffer.toString(), params, pageParams.getCurrentPage(), pageParams.getPageSize(), new DictInfoRowMapper());
        pageList.getPageParams().setParams(qo);

        return pageList;

    }
}
