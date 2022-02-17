package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageDetailInfoRowMapper implements RowMapper<MessageDetailInfoValidator> {
    @Override
    public MessageDetailInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageDetailInfoValidator qo = new MessageDetailInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setBusinessAccount(resultSet.getString("BUSINESS_ACCOUNT"));
        qo.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
        qo.setSendNumber(resultSet.getString("SEND_NUMBER"));
        qo.setSubmitTime(resultSet.getString("SUBMIT_TIME"));
        qo.setSubmitStyle(resultSet.getString("SUBMIT_STYLE"));
        qo.setSign(resultSet.getString("SIGN"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setArea(resultSet.getString("AREA"));
        qo.setCustomerSubmitStatus(resultSet.getString("CUSTOMER_SUBMIT_STATUS"));
        qo.setSendTime(resultSet.getString("SEND_TIME"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setReportTime(resultSet.getString("REPORT_TIME"));
        qo.setReportStatus(resultSet.getString("REPORT_STATUS"));
        qo.setDelayTimes(resultSet.getString("DELAY_TIMES"));
        qo.setTotalDelayTimes(resultSet.getString("TOTAL_DELAY_TIMES"));
        qo.setCustomerStatus(resultSet.getString("CUSTOMER_STATUS"));
        qo.setMessageContent(resultSet.getString("MESSAGE_CONTENT"));

        return qo;
    }
}
