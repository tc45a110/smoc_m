package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageHttpsTaskInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * web任务单
 */
public class MessageHttpTaskInfoRowMapper implements RowMapper<MessageHttpsTaskInfoValidator> {
    @Override
    public MessageHttpsTaskInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageHttpsTaskInfoValidator qo = new MessageHttpsTaskInfoValidator();
        qo.setBusinessAccount(resultSet.getString("BUSINESS_ACCOUNT"));
        qo.setId(resultSet.getString("ID"));
        qo.setTemplateId(resultSet.getString("TEMPLATE_ID"));
        qo.setMessageContent(resultSet.getString("MESSAGE_CONTENT"));
        qo.setSuccessNumber(resultSet.getInt("SUCCESS_NUMBER"));
        qo.setSuccessSendNumber(resultSet.getInt("SUCCESS_SEND_NUMBER"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        qo.setSplitNumber(resultSet.getInt("SPLIT_NUMBER"));
        qo.setSubmitNumber(resultSet.getInt("SUBMIT_NUMBER"));

        return qo;
    }
}
