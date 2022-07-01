package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.qo.AccountInfoQo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class AccountInfoQoRowMapper implements RowMapper<AccountInfoQo> {

    @Override
    public AccountInfoQo mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountInfoQo qo = new AccountInfoQo();
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setExtendCode(resultSet.getString("EXTEND_CODE"));
        qo.setRandomExtendCodeLength(resultSet.getInt("RANDOM_EXTEND_CODE_LENGTH"));
        qo.setAccountStatus(resultSet.getString("ACCOUNT_STATUS"));
        qo.setPayType(resultSet.getString("PAY_TYPE"));
        qo.setChargeType(resultSet.getString("CHARGE_TYPE"));
        qo.setProtocol(resultSet.getString("PROTOCOL"));
        qo.setSrcId(resultSet.getString("SRC_ID"));

        return qo;
    }
}
