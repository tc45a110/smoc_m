package com.smoc.cloud.auth.data.provider.rowmapper;

import com.smoc.cloud.common.auth.validator.SystemUserPersonalParamValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SystemUserPersonalParamRowMapper implements RowMapper<SystemUserPersonalParamValidator> {
    @Override
    public SystemUserPersonalParamValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        SystemUserPersonalParamValidator data = new SystemUserPersonalParamValidator();
        return data;
    }
}
