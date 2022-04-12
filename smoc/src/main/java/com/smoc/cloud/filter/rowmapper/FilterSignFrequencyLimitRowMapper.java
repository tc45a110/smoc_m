package com.smoc.cloud.filter.rowmapper;

import com.smoc.cloud.common.smoc.filter.FilterSignFrequencyLimitValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilterSignFrequencyLimitRowMapper implements RowMapper<FilterSignFrequencyLimitValidator> {
    @Override
    public FilterSignFrequencyLimitValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        FilterSignFrequencyLimitValidator qo = new FilterSignFrequencyLimitValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setLimitType(resultSet.getString("LIMIT_TYPE"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setSigns(resultSet.getString("SIGNS"));
        qo.setAccounts(resultSet.getString("ACCOUNTS"));
        qo.setFrequency(resultSet.getString("FREQUENCY"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
