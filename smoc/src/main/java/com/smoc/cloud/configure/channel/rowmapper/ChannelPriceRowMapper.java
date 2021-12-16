package com.smoc.cloud.configure.channel.rowmapper;

import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class ChannelPriceRowMapper implements RowMapper<ChannelPriceValidator> {

    @Override
    public ChannelPriceValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        ChannelPriceValidator qo = new ChannelPriceValidator();
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setPriceStyle(resultSet.getString("PRICE_STYLE"));
        qo.setAreaCode(resultSet.getString("AREA_CODE"));
        qo.setChannelPrice(resultSet.getBigDecimal("CHANNEL_PRICE"));
        qo.setLasttimeHistory(resultSet.getString("LASTTIME_HISTORY"));

        return qo;
    }
}
