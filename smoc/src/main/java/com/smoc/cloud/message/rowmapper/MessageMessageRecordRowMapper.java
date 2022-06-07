package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMessageRecordRowMapper implements RowMapper<MessageDetailInfoValidator> {
    @Override
    public MessageDetailInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageDetailInfoValidator qo = new MessageDetailInfoValidator();
        qo.setBusinessAccount(resultSet.getString("ACCOUNT_ID"));
        qo.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
        qo.setTaskId(resultSet.getString("MESSAGE_ID"));
        qo.setProtocol(resultSet.getString("PROTOCOL"));
        qo.setMessageContent(resultSet.getString("MESSAGE_CONTENT"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setArea(resultSet.getString("AREA_NAME"));
        qo.setCustomerStatus(resultSet.getString("STATUS_CODE"));
        qo.setSubmitTime(resultSet.getString("SUBMIT_TIME"));
        qo.setSendTime(resultSet.getString("REPORT_TIME"));

        return qo;
    }
}
