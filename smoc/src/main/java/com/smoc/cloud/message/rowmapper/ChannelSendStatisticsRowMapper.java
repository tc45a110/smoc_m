package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.query.model.AccountSendStatisticModel;
import com.smoc.cloud.common.smoc.query.model.ChannelSendStatisticModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class ChannelSendStatisticsRowMapper implements RowMapper<ChannelSendStatisticModel> {

    @Override
    public ChannelSendStatisticModel mapRow(ResultSet resultSet, int i) throws SQLException {

        ChannelSendStatisticModel qo = new ChannelSendStatisticModel();
        qo.setMessageDate(resultSet.getString("MESSAGE_DATE"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setChannelName(resultSet.getString("CHANNEL_NAME"));
        qo.setCustomerSubmitNum(resultSet.getInt("CUSTOMER_SUBMIT_NUM"));
        qo.setSuccessSubmitNum(resultSet.getInt("SUCCESS_SUBMIT_NUM"));
        qo.setFailureSubmitNum(resultSet.getInt("FAILURE_SUBMIT_NUM"));
        qo.setMessageSuccessNum(resultSet.getInt("MESSAGE_SUCCESS_NUM"));
        qo.setMessageFailureNum(resultSet.getInt("MESSAGE_FAILURE_NUM"));
        qo.setMessageNoReportNum(resultSet.getInt("MESSAGE_NO_REPORT_NUM"));
        return qo;
    }
}
