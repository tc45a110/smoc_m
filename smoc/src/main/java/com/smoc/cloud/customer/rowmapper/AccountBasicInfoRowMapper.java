package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class AccountBasicInfoRowMapper implements RowMapper<AccountBasicInfoValidator> {

    @Override
    public AccountBasicInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountBasicInfoValidator qo = new AccountBasicInfoValidator();
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setExtendCode(resultSet.getString("EXTEND_CODE"));
        qo.setAccountProcess(resultSet.getString("ACCOUNT_PROCESS"));
        qo.setAccountStauts(resultSet.getString("ACCOUNT_STATUS"));

        return qo;
    }
}
