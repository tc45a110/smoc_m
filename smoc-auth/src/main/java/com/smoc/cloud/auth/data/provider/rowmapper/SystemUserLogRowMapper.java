package com.smoc.cloud.auth.data.provider.rowmapper;

import com.smoc.cloud.auth.data.provider.entity.SystemUserLog;
import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SystemUserLogRowMapper implements RowMapper<SystemUserLogValidator> {

    @Override
    public SystemUserLogValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        SystemUserLogValidator data = new SystemUserLogValidator();
        return data;
    }
}
