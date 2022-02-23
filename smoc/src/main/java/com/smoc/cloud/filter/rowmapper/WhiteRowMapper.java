package com.smoc.cloud.filter.rowmapper;

import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询映射
 * 2019/4/23 15:21
 **/
public class WhiteRowMapper implements RowMapper<FilterWhiteListValidator> {

    @Override
    public FilterWhiteListValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        FilterWhiteListValidator data = new FilterWhiteListValidator();
        data.setId(resultSet.getString("ID"));
        data.setGroupId(resultSet.getString("GROUP_ID"));
        String userName = resultSet.getString("NAME");
        data.setName(userName);
        String mobile = resultSet.getString("MOBILE");
        data.setMobile(mobile);
        data.setStatus(resultSet.getString("STATUS"));
        data.setCreatedTimeStr(resultSet.getString("CREATED_TIME"));
        data.setCreatedBy(resultSet.getString("CREATED_BY"));
        return data;
    }
}
