package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class MessageAccountInfoRowMapper implements RowMapper<MessageAccountValidator> {

    @Override
    public MessageAccountValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageAccountValidator qo = new MessageAccountValidator();
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setAccountStatus(resultSet.getString("ACCOUNT_STATUS"));
        qo.setPayType(resultSet.getString("PAY_TYPE"));
        qo.setProtocol(resultSet.getString("PROTOCOL"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        qo.setDayLimit(resultSet.getString("DAY_LIMIT"));
        qo.setSendLimit(resultSet.getString("SEND_LIMIT"));
        qo.setMaskArea(resultSet.getString("MASK_AREA"));

        return qo;
    }
}
