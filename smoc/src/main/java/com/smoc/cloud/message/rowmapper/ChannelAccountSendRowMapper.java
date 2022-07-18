package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.query.model.AccountSendStatisticItemsModel;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChannelAccountSendRowMapper implements RowMapper<AccountSendStatisticItemsModel> {
    @Override
    public AccountSendStatisticItemsModel mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountSendStatisticItemsModel qo = new AccountSendStatisticItemsModel();
        qo.setMessageDate(resultSet.getString("MESSAGE_DATE"));
        qo.setBusinessAccount(resultSet.getString("BUSINESS_ACCOUNT"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setCustomerSubmitNum(resultSet.getInt("CUSTOMER_SUBMIT_NUM"));
        qo.setSuccessSubmitNum(resultSet.getInt("SUCCESS_SUBMIT_NUM"));
        qo.setFailureSubmitNum(resultSet.getInt("FAILURE_SUBMIT_NUM"));
        qo.setMessageSuccessNum(resultSet.getInt("MESSAGE_SUCCESS_NUM"));
        qo.setMessageFailureNum(resultSet.getInt("MESSAGE_FAILURE_NUM"));
        qo.setMessageNoReportNum(resultSet.getInt("MESSAGE_NO_REPORT_NUM"));
        return qo;
    }
}
