package com.smoc.cloud.configure.channel.rowmapper;

import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelSpareChannelValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class SpareChannelInfoRowMapper implements RowMapper<ConfigChannelSpareChannelValidator> {

    @Override
    public ConfigChannelSpareChannelValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        ConfigChannelSpareChannelValidator qo = new ConfigChannelSpareChannelValidator();
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setSpareChannelName(resultSet.getString("CHANNEL_NAME"));

        return qo;
    }
}
