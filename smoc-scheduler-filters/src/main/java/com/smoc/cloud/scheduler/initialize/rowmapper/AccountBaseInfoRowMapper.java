package com.smoc.cloud.scheduler.initialize.rowmapper;

import com.smoc.cloud.scheduler.initialize.entity.AccountBaseInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountBaseInfoRowMapper implements RowMapper<AccountBaseInfo> {
    @Override
    public AccountBaseInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccountBaseInfo qo = new AccountBaseInfo();
        qo.setAccountId(rs.getString("ACCOUNT_ID"));
        qo.setEnterpriseFlag(rs.getString("ENTERPRISE_FLAG"));
        qo.setAccountName(rs.getString("ACCOUNT_NAME"));
        qo.setAccountPriority(rs.getString("ACCOUNT_PRIORITY"));
        qo.setTransferType(rs.getString("TRANSFER_TYPE"));
        qo.setBusinessType(rs.getString("BUSINESS_TYPE"));
        qo.setBusinessType(rs.getString("INFO_TYPE"));
        qo.setIndustryTypes(rs.getString("INDUSTRY_TYPE"));
        qo.setBusinessCarrier(rs.getString("CARRIER"));
        qo.setAccountExtendCode(rs.getString("EXTEND_CODE"));
        qo.setConsumeType(rs.getString("CONSUME_TYPE"));
        return qo;
    }
}
