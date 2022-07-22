package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import com.smoc.cloud.common.smoc.query.model.AccountSendStatisticItemsModel;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountSendStatisticItemsRowMapper implements RowMapper<AccountSendStatisticItemsModel> {
    @Override
    public AccountSendStatisticItemsModel mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountSendStatisticItemsModel qo = new AccountSendStatisticItemsModel();
        qo.setMessageDate(resultSet.getString("MESSAGE_DATE"));
        qo.setBusinessAccount(resultSet.getString("BUSINESS_ACCOUNT"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setPrice(new BigDecimal(resultSet.getBigDecimal("ACCOUNT_PRICE").stripTrailingZeros().toPlainString()));
        qo.setCustomerSubmitNum(resultSet.getInt("CUSTOMER_SUBMIT_NUM"));
        qo.setSuccessSubmitNum(resultSet.getInt("SUCCESS_SUBMIT_NUM"));
        qo.setFailureSubmitNum(resultSet.getInt("FAILURE_SUBMIT_NUM"));
        qo.setMessageSuccessNum(resultSet.getInt("MESSAGE_SUCCESS_NUM"));
        qo.setMessageFailureNum(resultSet.getInt("MESSAGE_FAILURE_NUM"));
        qo.setMessageNoReportNum(qo.getSuccessSubmitNum()-qo.getMessageSuccessNum()-qo.getMessageFailureNum());
        return qo;
    }
}
