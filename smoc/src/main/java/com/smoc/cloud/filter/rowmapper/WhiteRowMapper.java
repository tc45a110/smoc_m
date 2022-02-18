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

        FilterWhiteListValidator meipGroupPersonValidator = new FilterWhiteListValidator();
        meipGroupPersonValidator.setId(resultSet.getString("ID"));
        meipGroupPersonValidator.setGroupId(resultSet.getString("GROUP_ID"));
        String userName = resultSet.getString("NAME");
        meipGroupPersonValidator.setName(userName);
        String mobile = resultSet.getString("MOBILE");
        meipGroupPersonValidator.setMobile(mobile);
        meipGroupPersonValidator.setStatus(resultSet.getString("STATUS"));
        meipGroupPersonValidator.setCreatedTimeStr(resultSet.getString("CREATED_TIME"));
        meipGroupPersonValidator.setCreatedBy(resultSet.getString("CREATED_BY"));
        return meipGroupPersonValidator;
    }
}
