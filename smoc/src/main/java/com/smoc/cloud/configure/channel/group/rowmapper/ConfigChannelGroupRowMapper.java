package com.smoc.cloud.configure.channel.group.rowmapper;

import com.smoc.cloud.common.smoc.configuate.qo.ConfigChannelGroupQo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class ConfigChannelGroupRowMapper implements RowMapper<ConfigChannelGroupQo> {

    @Override
    public ConfigChannelGroupQo mapRow(ResultSet resultSet, int i) throws SQLException {

        ConfigChannelGroupQo qo = new ConfigChannelGroupQo();
        qo.setId(resultSet.getString("ID"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setChannelName(resultSet.getString("CHANNEL_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setProtocol(resultSet.getString("PROTOCOL"));
        qo.setChannelPriority(resultSet.getInt("CHANNEL_PRIORITY"));
        qo.setChannelWeight(resultSet.getInt("CHANNEL_WEIGHT"));

        return qo;
    }
}
