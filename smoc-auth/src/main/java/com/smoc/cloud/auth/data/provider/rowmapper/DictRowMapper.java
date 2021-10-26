package com.smoc.cloud.auth.data.provider.rowmapper;

import com.smoc.cloud.common.auth.qo.Dict;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询映射
 * 2019/4/23 15:21
 **/
public class DictRowMapper implements RowMapper<Dict> {

    @Override
    public Dict mapRow(ResultSet resultSet, int i) throws SQLException {

        String id = resultSet.getString("ID");
        String name = resultSet.getString("DICT_NAME");
        String code = resultSet.getString("DICT_CODE");
        Dict dict = new Dict();
        dict.setId(id);
        dict.setFieldName(name);
        dict.setFieldCode(code);

        return dict;
    }
}
