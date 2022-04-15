package com.smoc.cloud.configure.channel.rowmapper;

import com.smoc.cloud.common.smoc.configuate.qo.ChannelAccountInfoQo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class ChannelAccountInfoRowMapper implements RowMapper<ChannelAccountInfoQo> {

    @Override
    public ChannelAccountInfoQo mapRow(ResultSet resultSet, int i) throws SQLException {

        ChannelAccountInfoQo qo = new ChannelAccountInfoQo();
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setAccountStatus(resultSet.getString("ACCOUNT_STATUS"));
        return qo;
    }
}
