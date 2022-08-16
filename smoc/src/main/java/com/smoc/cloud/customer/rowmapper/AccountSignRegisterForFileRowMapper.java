package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterForFileValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountSignRegisterForFileRowMapper implements RowMapper<AccountSignRegisterForFileValidator> {
    @Override
    public AccountSignRegisterForFileValidator mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccountSignRegisterForFileValidator qo = new AccountSignRegisterForFileValidator();
        qo.setId(rs.getString("ID"));
        qo.setRegisterType(rs.getString("REGISTER_TYPE"));
        qo.setRegisterSignId(rs.getString("REGISTER_SIGN_ID"));
        qo.setAccount(rs.getString("ACCOUNT"));
        qo.setChannelId(rs.getString("CHANNEL_ID"));
        qo.setChannelName(rs.getString("CHANNEL_NAME"));
        qo.setAccessProvince(rs.getString("ACCESS_PROVINCE"));
        qo.setRegisterCarrier(rs.getString("REGISTER_CARRIER"));
        qo.setRegisterCodeNumber(rs.getString("REGISTER_CODE_NUMBER"));
        qo.setRegisterExtendNumber(rs.getString("REGISTER_EXTEND_NUMBER"));
        qo.setRegisterSign(rs.getString("REGISTER_SIGN"));
        qo.setNumberSegment(rs.getString("NUMBER_SEGMENT"));
        qo.setRegisterExpireDate(rs.getString("REGISTER_EXPIRE_DATE"));
        qo.setRegisterStatus(rs.getString("REGISTER_STATUS"));
        qo.setUpdatedBy(rs.getString("UPDATED_BY"));
        qo.setUpdatedTime(rs.getString("UPDATED_TIME"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
