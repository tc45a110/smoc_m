package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountSignRegisterRowMapper implements RowMapper<AccountSignRegisterValidator> {
    @Override
    public AccountSignRegisterValidator mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccountSignRegisterValidator qo = new AccountSignRegisterValidator();
        qo.setId(rs.getString("ID"));
        qo.setAccount(rs.getString("ACCOUNT"));
        qo.setSign(rs.getString("SIGN"));
        qo.setSignExtendNumber(rs.getString("SIGN_EXTEND_NUMBER"));
        qo.setExtendType(rs.getString("EXTEND_TYPE"));
        qo.setExtendData(rs.getString("EXTEND_DATA"));
        qo.setEnterpriseId(rs.getString("ENTERPRISE_ID"));
        qo.setAppName(rs.getString("APP_NAME"));
        qo.setServiceType(rs.getString("SERVICE_TYPE"));
        qo.setMainApplication(rs.getString("MAIN_APPLICATION"));
        qo.setRegisterStatus(rs.getString("REGISTER_STATUS"));
        qo.setCreatedBy(rs.getString("CREATED_BY"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
