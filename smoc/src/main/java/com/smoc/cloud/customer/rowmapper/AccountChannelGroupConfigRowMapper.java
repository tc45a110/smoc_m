package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class AccountChannelGroupConfigRowMapper implements RowMapper<AccountChannelInfoQo> {

    @Override
    public AccountChannelInfoQo mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountChannelInfoQo qo = new AccountChannelInfoQo();
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setChannelGroupId(resultSet.getString("CHANNEL_GROUP_ID"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setChannelName(resultSet.getString("CHANNEL_GROUP_NAME"));
        qo.setChannelIntroduce(resultSet.getString("CHANNEL_GROUP_INTRODUCE"));
        qo.setChannelCarrier(resultSet.getString("CHANNEL_GROUP_CARRIER"));
        qo.setChannelInfoType(resultSet.getString("CHANNEL_GROUP_INFO_TYPE"));
        return qo;
    }
}
