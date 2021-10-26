package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.rowmapper.DictRowMapper;
import com.smoc.cloud.common.auth.qo.Dict;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;

public class BaseCommDictRepositoryImpl {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public List<Dict> findDictByTypeId(String typeId) {

        String  sql = "select ID, DICT_NAME, DICT_CODE from base_comm_dict where ACTIVE =1 and TYPE_ID=? order by sort ASC ";

        Object[] params = new Object[1];
        params[0] = typeId;

        List<Dict> dicts = jdbcTemplate.query(sql,params, new DictRowMapper());

        return dicts;

    }
}
