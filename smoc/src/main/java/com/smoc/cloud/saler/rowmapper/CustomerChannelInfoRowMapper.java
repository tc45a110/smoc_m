package com.smoc.cloud.saler.rowmapper;

import com.smoc.cloud.common.smoc.saler.qo.CustomerChannelInfoQo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class CustomerChannelInfoRowMapper implements RowMapper<CustomerChannelInfoQo> {

    @Override
    public CustomerChannelInfoQo mapRow(ResultSet resultSet, int i) throws SQLException {

        CustomerChannelInfoQo qo = new CustomerChannelInfoQo();
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setChannelName(resultSet.getString("CHANNEL_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setChannelStatus(resultSet.getString("CHANNEL_STATUS"));
        qo.setChannelAccessAccount(resultSet.getString("CHANNEL_ACCESS_ACCOUNT"));
        qo.setChannelAccessPassword(resultSet.getString("CHANNEL_ACCESS_PASSWORD"));
        qo.setChannelServiceUrl(resultSet.getString("CHANNEL_SERVICE_URL"));
        qo.setSpId(resultSet.getString("SP_ID"));
        qo.setSrcId(resultSet.getString("SRC_ID"));
        qo.setBusinessCode(resultSet.getString("BUSINESS_CODE"));
        qo.setConnectNumber(resultSet.getInt("CONNECT_NUMBER"));
        qo.setMaxSendSecond(resultSet.getInt("MAX_SEND_SECOND"));
        qo.setHeartbeatInterval(resultSet.getInt("HEARTBEAT_INTERVAL"));
        qo.setProtocol(resultSet.getString("PROTOCOL"));

        return qo;
    }
}
