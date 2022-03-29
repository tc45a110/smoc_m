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
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setMobile(resultSet.getString("MOBILE"));
        qo.setMessageContent(resultSet.getString("MESSAGE_CONTENT"));
        qo.setMoDate(resultSet.getString("MO_DATE"));

        return qo;
    }
}
