package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMessageRecordRowMapper implements RowMapper<MessageDetailInfoValidator> {
    @Override
    public MessageDetailInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageDetailInfoValidator qo = new MessageDetailInfoValidator();
        qo.setTaskId(resultSet.getString("TASK_ID"));
        qo.setProtocol(resultSet.getString("PROTOCOL"));
        qo.setBusinessAccount(resultSet.getString("BUSINESS_ACCOUNT"));
        qo.setMessageContent(resultSet.getString("MESSAGE_CONTENT"));
        qo.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setArea(resultSet.getString("AREA"));
        qo.setReportStatus(resultSet.getString("REPORT_STATUS"));
        qo.setCustomerStatus(resultSet.getString("CUSTOMER_STATUS"));
        qo.setSubmitTime(resultSet.getString("SUBMIT_TIME"));
        qo.setSendTime(resultSet.getString("SEND_TIME"));

        return qo;
    }
}
