package com.smoc.cloud.scheduler.channel.price.service.rowmapper;

import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class ChannelPriceRowMapper implements RowMapper<ChannelPriceValidator> {

    @Override
    public ChannelPriceValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        ChannelPriceValidator qo = new ChannelPriceValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setPriceStyle(resultSet.getString("PRICE_STYLE"));
        qo.setAreaCode(resultSet.getString("AREA_CODE"));
        qo.setLasttimeHistory(resultSet.getString("LASTTIME_HISTORY"));
        //间隔天数
        qo.setCreatedBy(resultSet.getInt("DAYS")+"");
        //当前日期
        qo.setUpdatedBy(resultSet.getString("TODAY"));
        BigDecimal channelPrice = resultSet.getBigDecimal("CHANNEL_PRICE");
        if(!StringUtils.isEmpty(channelPrice)){
            qo.setChannelPrice(new BigDecimal(channelPrice.stripTrailingZeros().toPlainString()));
        }else{
            qo.setChannelPrice(channelPrice);
        }
        return qo;
    }
}
