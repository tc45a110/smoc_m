package com.smoc.cloud.parameter.errorcode.rowmapper;

import com.smoc.cloud.common.smoc.parameter.SystemErrorCodeValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询映射
 * 2019/4/23 15:21
 **/
public class SystemErrorCodeRowMapper implements RowMapper<SystemErrorCodeValidator> {

    @Override
    public SystemErrorCodeValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        SystemErrorCodeValidator data = new SystemErrorCodeValidator();
        data.setId(resultSet.getString("ID"));
        data.setCodeType(resultSet.getString("CODE_TYPE"));
        data.setErrorCode(resultSet.getString("ERROR_CODE"));
        data.setErrorContent(resultSet.getString("ERROR_CONTENT"));
        data.setHandleRemark(resultSet.getString("HANDLE_REMARK"));
        data.setStatus(resultSet.getString("STATUS"));
        return data;
    }
}
