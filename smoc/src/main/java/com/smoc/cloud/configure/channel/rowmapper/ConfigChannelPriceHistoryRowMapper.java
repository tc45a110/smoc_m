package com.smoc.cloud.configure.channel.rowmapper;

import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelPriceHistoryValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigChannelPriceHistoryRowMapper implements RowMapper<ConfigChannelPriceHistoryValidator> {
    @Override
    public ConfigChannelPriceHistoryValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        ConfigChannelPriceHistoryValidator qo = new ConfigChannelPriceHistoryValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setSourceId(resultSet.getString("SOURCE_ID"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setPriceStyle(resultSet.getString("PRICE_STYLE"));
        qo.setAreaCode(resultSet.getString("AREA_CODE"));
        qo.setChannelPrice(resultSet.getBigDecimal("CHANNEL_PRICE"));
        qo.setPriceDate(resultSet.getString("PRICE_DATE"));
        qo.setCreateTime(resultSet.getString("CREATE_TIME"));
        qo.setUpdatedTime(resultSet.getString("UPDATED_TIME"));

        return qo;
    }
}
