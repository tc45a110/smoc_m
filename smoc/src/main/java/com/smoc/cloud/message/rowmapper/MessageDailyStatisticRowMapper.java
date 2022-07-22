package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageDailyStatisticRowMapper implements RowMapper<MessageDailyStatisticValidator> {
    @Override
    public MessageDailyStatisticValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageDailyStatisticValidator qo = new MessageDailyStatisticValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setBusinessAccount(resultSet.getString("BUSINESS_ACCOUNT"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setAreaCode(resultSet.getString("AREA_CODE"));
        qo.setPriceAreaCode(resultSet.getString("PRICE_AREA_CODE"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setCustomerSubmitNum(resultSet.getInt("CUSTOMER_SUBMIT_NUM"));
        qo.setSuccessSubmitNum(resultSet.getInt("SUCCESS_SUBMIT_NUM"));
        qo.setFailureSubmitNum(resultSet.getInt("FAILURE_SUBMIT_NUM"));
        qo.setMessageSuccessNum(resultSet.getInt("MESSAGE_SUCCESS_NUM"));
        qo.setMessageFailureNum(resultSet.getInt("MESSAGE_FAILURE_NUM"));
        qo.setMessageNoReportNum(qo.getSuccessSubmitNum()-qo.getMessageSuccessNum()-qo.getMessageFailureNum());
        qo.setMessageSign(resultSet.getString("MESSAGE_SIGN"));
        qo.setMessageDate(resultSet.getString("MESSAGE_DATE"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
