package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class AccountChannelInfoRowMapper implements RowMapper<ChannelBasicInfoQo> {

    @Override
    public ChannelBasicInfoQo mapRow(ResultSet resultSet, int i) throws SQLException {

        ChannelBasicInfoQo qo = new ChannelBasicInfoQo();
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setChannelName(resultSet.getString("CHANNEL_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setSrcId(resultSet.getString("SRC_ID"));
        qo.setProtocol(resultSet.getString("PROTOCOL"));
        qo.setChannelIntroduce(resultSet.getString("CHANNEL_INTRODUCE"));

        return qo;
    }
}
