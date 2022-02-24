package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountChannelInfoDetailRowMapper implements RowMapper<AccountChannelInfoValidator> {
    @Override
    public AccountChannelInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {
        AccountChannelInfoValidator qo = new AccountChannelInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setConfigType(resultSet.getString("CONFIG_TYPE"));
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setChannelPriority(resultSet.getString("CHANNEL_PRIORITY"));
        qo.setChannelWeight(resultSet.getInt("CHANNEL_WEIGHT"));
        qo.setChannelSource(resultSet.getString("CHANNEL_SOURCE"));
        qo.setChangeSource(resultSet.getString("CHANGE_SOURCE"));
        qo.setChannelStatus(resultSet.getString("CHANNEL_STATUS"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTimeStr(resultSet.getString("CREATED_TIME"));

        return qo;
    }
}
