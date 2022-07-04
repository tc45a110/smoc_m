package com.smoc.cloud.scheduler.channel.price.service.rowmapper;

import com.smoc.cloud.scheduler.channel.price.service.model.ChannelFutruePriceModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 通道未来价格 批处理
 */
public class ChannelFutruePriceRowMapper implements RowMapper<ChannelFutruePriceModel> {

    @Override
    public ChannelFutruePriceModel mapRow(ResultSet resultSet, int i) throws SQLException {

        ChannelFutruePriceModel qo = new ChannelFutruePriceModel();
        //ID
        qo.setId(resultSet.getString("ID"));
        qo.setSourceId(resultSet.getString("DATA_ID"));
        //业务账号
        qo.setAccountId(resultSet.getString("BUSINESS_ID"));
        //运营商
        qo.setCarrier(resultSet.getString("PRICE_AREA"));
        //运营商 价格
        qo.setCarrierPrice(resultSet.getBigDecimal("CHANGE_PRICE"));
        //计价方式
        qo.setPriceStyle(resultSet.getString("PRICE_STYLE"));

        //价格日期
        qo.setPriceDate(resultSet.getString("START_DATE"));

        return qo;
    }
}
