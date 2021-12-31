package com.smoc.cloud.configure.channel.group.rowmapper;

import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class CenterConfigChannelInfoRowMapper implements RowMapper<ChannelBasicInfoQo> {

    @Override
    public ChannelBasicInfoQo mapRow(ResultSet resultSet, int i) throws SQLException {

        ChannelBasicInfoQo qo = new ChannelBasicInfoQo();
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setChannelName(resultSet.getString("CHANNEL_NAME"));
        qo.setChannelProvder(resultSet.getString("CHANNEL_PROVDER"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setMaxComplaintRate(resultSet.getBigDecimal("MAX_COMPLAINT_RATE"));
        qo.setSrcId(resultSet.getString("SRC_ID"));
        qo.setProtocol(resultSet.getString("PROTOCOL"));
        qo.setChannelAccessAccount(resultSet.getString("CHANNEL_ACCESS_ACCOUNT"));
        qo.setPriceStyle(resultSet.getString("PRICE_STYLE"));
        qo.setChannelPrice(resultSet.getString("CHANNEL_PRICE"));
        qo.setBusinessAreaType(resultSet.getString("BUSINESS_AREA_TYPE"));
        qo.setMaskProvince(resultSet.getString("MASK_PROVINCE"));
        qo.setSupportAreaCodes(resultSet.getString("SUPPORT_AREA_CODES"));
        qo.setChannelRunStatus(resultSet.getString("CHANNEL_RUN_STATUS"));
        qo.setChannelStatus(resultSet.getString("CHANNEL_STATUS"));
        qo.setMaxSendSecond(resultSet.getInt("MAX_SEND_SECOND"));
        qo.setChannelIntroduce(resultSet.getString("CHANNEL_INTRODUCE"));
        qo.setChannelAccessSales(resultSet.getString("CHANNEL_ACCESS_SALES"));
        qo.setChannelRestrictContent(resultSet.getString("CHANNEL_RESTRICT_CONTENT"));
        qo.setChannelPriority(resultSet.getInt("CHANNEL_PRIORITY"));
        qo.setChannelWeight(resultSet.getInt("CHANNEL_WEIGHT"));

        return qo;
    }
}
