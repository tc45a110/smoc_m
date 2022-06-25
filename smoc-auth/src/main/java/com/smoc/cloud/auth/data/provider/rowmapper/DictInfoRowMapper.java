package com.smoc.cloud.auth.data.provider.rowmapper;

import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.validator.DictValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询映射
 * 2019/4/23 15:21
 **/
public class DictInfoRowMapper implements RowMapper<DictValidator> {

    @Override
    public DictValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        String id = resultSet.getString("ID");
        String name = resultSet.getString("DICT_NAME");
        String code = resultSet.getString("DICT_CODE");
        DictValidator dict = new DictValidator();
        dict.setId(id);
        dict.setDictName(name);
        dict.setDictCode(code);
        dict.setSort(resultSet.getInt("SORT"));
        return dict;
    }
}
