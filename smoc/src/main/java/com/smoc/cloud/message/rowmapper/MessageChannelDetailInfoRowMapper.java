package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageChannelDetailInfoRowMapper implements RowMapper<MessageDetailInfoValidator> {
    @Override
    public MessageDetailInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageDetailInfoValidator qo = new MessageDetailInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setBusinessAccount(resultSet.getString("ACCOUNT_ID"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setArea(resultSet.getString("AREA_NAME"));
        qo.setCustomerStatus(resultSet.getString("STATUS_CODE"));
        qo.setMessageContent(resultSet.getString("MESSAGE_CONTENT"));
        qo.setMessageNumber(resultSet.getInt("MESSAGE_TOTAL"));
        qo.setTimeElapsed(resultSet.getString("TIME_ELAPSED"));
        qo.setReportTime(resultSet.getString("CHANNEL_REPORT_TIME"));
        qo.setSubmitTime(resultSet.getString("CHANNEL_SUBMIT_TIME"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));

        return qo;
    }
}
