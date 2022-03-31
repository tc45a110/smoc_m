package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageMoInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 短信上行
 */
public class MessageMoInfoRowMapper implements RowMapper<MessageMoInfoValidator> {
    @Override
    public MessageMoInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageMoInfoValidator qo = new MessageMoInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setTaskId(resultSet.getString("TASK_ID"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setMobile(resultSet.getString("MOBILE"));
        qo.setMoMessageContent(resultSet.getString("MO_MESSAGE_CONTENT"));
        qo.setMoDate(resultSet.getString("MO_DATE"));
        qo.setMtMessageContent(resultSet.getString("MT_MESSAGE_CONTENT"));
        qo.setMtDate(resultSet.getString("MT_DATE"));

        return qo;
    }
}
