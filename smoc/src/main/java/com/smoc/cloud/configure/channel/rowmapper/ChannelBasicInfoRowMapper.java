package com.smoc.cloud.configure.channel.rowmapper;

import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class ChannelBasicInfoRowMapper implements RowMapper<ChannelBasicInfoValidator> {

    @Override
    public ChannelBasicInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        ChannelBasicInfoValidator qo = new ChannelBasicInfoValidator();
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));

        return qo;
    }
}
