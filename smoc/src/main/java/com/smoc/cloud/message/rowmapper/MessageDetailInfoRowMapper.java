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
        qo.setBusinessAccount(resultSet.getString("ACCOUNT_ID"));
        qo.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setArea(resultSet.getString("AREA_NAME"));
        qo.setReportTime(resultSet.getString("REPORT_TIME"));
        qo.setSubmitTime(resultSet.getString("SUBMIT_TIME"));
        qo.setCustomerStatus(resultSet.getString("STATUS_CODE"));
        qo.setMessageContent(resultSet.getString("MESSAGE_CONTENT"));
        qo.setSign(resultSet.getString("SIGN"));
        qo.setMessageNumber(resultSet.getInt("MESSAGE_TOTAL"));

        return qo;
    }
}
