package com.smoc.cloud.scheduler.init;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InitRowMapper implements RowMapper<InitModel> {
    @Override
    public InitModel mapRow(ResultSet resultSet, int i) throws SQLException {

        InitModel qo = new InitModel();
        qo.setBusinessAccount(resultSet.getString("ACCOUNT_ID"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        return qo;
    }
}
