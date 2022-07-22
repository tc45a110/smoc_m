package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.common.smoc.query.model.AccountSendStatisticModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class AccountSendStatisticsRowMapper implements RowMapper<AccountSendStatisticModel> {

    @Override
    public AccountSendStatisticModel mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountSendStatisticModel qo = new AccountSendStatisticModel();
        qo.setMessageDate(resultSet.getString("MESSAGE_DATE"));
        qo.setBusinessAccount(resultSet.getString("BUSINESS_ACCOUNT"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setTotalSuccessSubmitNum(resultSet.getInt("SUCCESS_SUBMIT_NUM"));
        qo.setTotalMessageSuccessNum(resultSet.getInt("MESSAGE_SUCCESS_NUM"));
        qo.setTotalMessageFailureNum(resultSet.getInt("MESSAGE_FAILURE_NUM"));
        qo.setTotalMessageNoReportNum(qo.getTotalSuccessSubmitNum()-qo.getTotalMessageSuccessNum()-qo.getTotalMessageFailureNum());
        return qo;
    }
}
