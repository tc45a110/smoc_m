package com.smoc.cloud.configure.channel.rowmapper;

import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelChangeValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigChannelChangeRowMapper implements RowMapper<ConfigChannelChangeValidator> {
    @Override
    public ConfigChannelChangeValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        ConfigChannelChangeValidator qo = new ConfigChannelChangeValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setChannelName(resultSet.getString("CHANNEL_NAME"));
        qo.setAccountNum(resultSet.getInt("ACCOUNT_NUM"));
        qo.setChangeType(resultSet.getString("CHANGE_TYPE"));
        qo.setChangeReason(resultSet.getString("CHANGE_REASON"));
        qo.setChangeStatus(resultSet.getString("CHANGE_STATUS"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
