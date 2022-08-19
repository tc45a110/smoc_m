package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterExportRecordValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountSignRegisterExportRecordRowMapper implements RowMapper<AccountSignRegisterExportRecordValidator> {
    @Override
    public AccountSignRegisterExportRecordValidator mapRow(ResultSet rs, int rowNum) throws SQLException {

        AccountSignRegisterExportRecordValidator qo = new AccountSignRegisterExportRecordValidator();
        qo.setId(rs.getString("ID"));
        qo.setRegisterOrderNo(rs.getString("REGISTER_ORDER_NO"));
        qo.setCarrier(rs.getString("CARRIER"));
        qo.setRegisterNumber(rs.getInt("REGISTER_NUMBER"));
        qo.setRegisterStatus(rs.getString("REGISTER_STATUS"));
        qo.setCreatedBy(rs.getString("CREATED_BY"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
