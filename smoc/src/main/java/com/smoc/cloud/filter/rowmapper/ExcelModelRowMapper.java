package com.smoc.cloud.filter.rowmapper;

import com.smoc.cloud.common.smoc.filter.ExcelModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询映射
 * 2019/4/23 15:21
 **/
public class ExcelModelRowMapper implements RowMapper<ExcelModel> {

    @Override
    public ExcelModel mapRow(ResultSet resultSet, int i) throws SQLException {

        ExcelModel data = new ExcelModel();
        String userName = resultSet.getString("NAME");
        data.setColumn2(userName);
        String mobile = resultSet.getString("MOBILE");
        data.setColumn1(mobile);
        return data;
    }
}
