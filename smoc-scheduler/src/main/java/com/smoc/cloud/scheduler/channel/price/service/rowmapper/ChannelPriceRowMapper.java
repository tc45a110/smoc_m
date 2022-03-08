package com.smoc.cloud.scheduler.channel.price.service.rowmapper;

import com.smoc.cloud.scheduler.channel.price.service.model.ChannelPriceModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class ChannelPriceRowMapper implements RowMapper<ChannelPriceModel> {

    @Override
    public ChannelPriceModel mapRow(ResultSet resultSet, int i) throws SQLException {

        ChannelPriceModel qo = new ChannelPriceModel();
        qo.setId(resultSet.getString("ID"));
        //通道id
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        //价格区域编码
        qo.setAreaCode(resultSet.getString("AREA_CODE"));
        //价格日期
        qo.setPriceData(resultSet.getString("PRICE_DATE"));
        //通道价格
        qo.setChannelPrice(resultSet.getBigDecimal("CHANNEL_PRICE"));
        //上次更新时间
        qo.setBatchDate(resultSet.getString("BATCH_DATE"));
        //间隔天数
        qo.setDays(resultSet.getInt("DAYS"));

        qo.setCreateTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
