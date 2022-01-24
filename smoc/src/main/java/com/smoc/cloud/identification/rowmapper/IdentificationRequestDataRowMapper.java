package com.smoc.cloud.identification.rowmapper;

import com.smoc.cloud.common.smoc.identification.validator.IdentificationRequestDataValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IdentificationRequestDataRowMapper implements RowMapper<IdentificationRequestDataValidator> {
    @Override
    public IdentificationRequestDataValidator mapRow(ResultSet resultSet, int i) throws SQLException {
        IdentificationRequestDataValidator qo = new IdentificationRequestDataValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setIdentificationAccount(resultSet.getString("IDENTIFICATION_ACCOUNT"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setOrderNo(resultSet.getString("ORDER_NO"));
        qo.setOrderType(resultSet.getString("ORDER_TYPE"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
