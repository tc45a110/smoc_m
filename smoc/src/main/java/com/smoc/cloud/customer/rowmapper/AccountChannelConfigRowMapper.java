package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class AccountChannelConfigRowMapper implements RowMapper<AccountChannelInfoQo> {

    @Override
    public AccountChannelInfoQo mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountChannelInfoQo qo = new AccountChannelInfoQo();
        qo.setId(resultSet.getString("ID"));
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setChannelName(resultSet.getString("CHANNEL_NAME"));
        qo.setProtocol(resultSet.getString("PROTOCOL"));
        qo.setChannelIntroduce(resultSet.getString("CHANNEL_INTRODUCE"));
        qo.setChannelCarrier(resultSet.getString("CHANNEL_CARRIER"));
        qo.setChannelInfoType(resultSet.getString("CHANNEL_INFO_TYPE"));

        return qo;
    }
}
