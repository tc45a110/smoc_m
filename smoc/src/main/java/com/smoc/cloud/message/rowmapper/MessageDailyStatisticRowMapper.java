package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageDailyStatisticRowMapper implements RowMapper<MessageDailyStatisticValidator> {
    @Override
    public MessageDailyStatisticValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageDailyStatisticValidator qo = new MessageDailyStatisticValidator();
        return qo;
    }
}
