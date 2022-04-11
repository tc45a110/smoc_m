package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * web任务单
 */
public class MessageHttpTaskInfoRowMapper implements RowMapper<MessageWebTaskInfoValidator> {
    @Override
    public MessageWebTaskInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageWebTaskInfoValidator qo = new MessageWebTaskInfoValidator();
        qo.setBusinessAccount(resultSet.getString("BUSINESS_ACCOUNT"));
        qo.setId(resultSet.getString("ID"));
        qo.setTemplateId(resultSet.getString("TEMPLATE_ID"));
        qo.setMessageContent(resultSet.getString("MESSAGE_CONTENT"));
        qo.setSuccessNumber(resultSet.getInt("SUCCESS_NUMBER"));
        qo.setSuccessSendNumber(resultSet.getInt("SUCCESS_SEND_NUMBER"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));

        return qo;
    }
}
