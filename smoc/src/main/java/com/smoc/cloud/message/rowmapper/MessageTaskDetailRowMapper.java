package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.model.MessageTaskDetail;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageTaskDetailRowMapper implements RowMapper<MessageTaskDetail> {
    @Override
    public MessageTaskDetail mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageTaskDetail qo = new MessageTaskDetail();
        qo.setMobile(resultSet.getString("SEND_NUMBER"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setArea(resultSet.getString("AREA"));
        qo.setSendTime(resultSet.getString("SEND_TIME"));
        qo.setCustomerStatus(resultSet.getString("CUSTOMER_STATUS"));
        qo.setMessageContent(resultSet.getString("MESSAGE_CONTENT"));
        qo.setChargeNumber(resultSet.getInt("CHARGE_NUMBER"));

        return qo;
    }
}
