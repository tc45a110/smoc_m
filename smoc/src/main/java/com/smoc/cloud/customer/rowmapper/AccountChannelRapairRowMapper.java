package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.qo.AccountChannelRepairQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class AccountChannelRapairRowMapper implements RowMapper<AccountChannelRepairQo> {

    @Override
    public AccountChannelRepairQo mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountChannelRepairQo qo = new AccountChannelRepairQo();
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setRepairCode(resultSet.getString("REPAIR_CODE"));
        qo.setRepairDate(resultSet.getString("REPAIR_DATE"));

        return qo;
    }
}
