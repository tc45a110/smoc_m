package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.qo.AccountContentRepairQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountInfoQo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class AccountContentRepairQoRowMapper implements RowMapper<AccountContentRepairQo> {

    @Override
    public AccountContentRepairQo mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountContentRepairQo qo = new AccountContentRepairQo();
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));

        return qo;
    }
}
