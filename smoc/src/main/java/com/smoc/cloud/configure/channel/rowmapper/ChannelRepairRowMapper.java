package com.smoc.cloud.configure.channel.rowmapper;

import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class ChannelRepairRowMapper implements RowMapper<ConfigChannelRepairValidator> {

    @Override
    public ConfigChannelRepairValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        ConfigChannelRepairValidator qo = new ConfigChannelRepairValidator();
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setChannelName(resultSet.getString("CHANNEL_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setRepairCode(resultSet.getString("REPAIR_CODE"));
        qo.setRepairDate(resultSet.getString("REPAIR_DATE"));

        return qo;
    }
}
