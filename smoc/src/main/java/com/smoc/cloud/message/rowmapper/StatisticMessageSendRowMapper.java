package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.model.StatisticMessageSend;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * web任务单
 */
public class StatisticMessageSendRowMapper implements RowMapper<StatisticMessageSend> {
    @Override
    public StatisticMessageSend mapRow(ResultSet resultSet, int i) throws SQLException {

        StatisticMessageSend qo = new StatisticMessageSend();
        qo.setSubmitNumber(resultSet.getInt("SUBMIT_NUMBER"));
        qo.setSuccessNumber(resultSet.getInt("SUCCESS_NUMBER"));
        qo.setSuccessSendNumber(resultSet.getInt("SUCCESS_SEND_NUMBER"));
        qo.setFailureNumber(resultSet.getInt("FAILURE_NUMBER"));
        qo.setNoReportNumber(qo.getSuccessNumber()-qo.getSuccessSendNumber()-qo.getFailureNumber());

        return qo;
    }
}
