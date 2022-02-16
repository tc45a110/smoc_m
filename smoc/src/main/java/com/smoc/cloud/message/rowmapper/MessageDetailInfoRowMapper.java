package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageDetailInfoRowMapper implements RowMapper<MessageDetailInfoValidator> {
    @Override
    public MessageDetailInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageDetailInfoValidator qo = new MessageDetailInfoValidator();
        return qo;
    }
}
