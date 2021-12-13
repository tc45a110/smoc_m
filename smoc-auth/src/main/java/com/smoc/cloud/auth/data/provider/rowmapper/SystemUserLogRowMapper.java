package com.smoc.cloud.auth.data.provider.rowmapper;

import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class SystemUserLogRowMapper implements RowMapper<SystemUserLogValidator> {

    @Override
    public SystemUserLogValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        SystemUserLogValidator data = new SystemUserLogValidator();
        data.setId(resultSet.getString("ID"));
        data.setUserId(resultSet.getString("USER_ID"));
        data.setModule(resultSet.getString("MODULE"));
        data.setModuleId(resultSet.getString("MODULE_ID"));
        data.setOperationType(resultSet.getString("OPERATION_TYPE"));
        data.setSimpleIntroduce(resultSet.getString("SIMPLE_INTRODUCE"));
        data.setCreatedTime(DateTimeUtils.getDateTimeFormat(resultSet.getString("CREATED_TIME")));
        return data;
    }
}
