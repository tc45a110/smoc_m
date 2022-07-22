package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.model.StatisticMessageSend;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSendData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 * web任务单
 */
public class StatisticMessageSendNumberRowMapper implements RowMapper<StatisticMessageSendData> {
    @Override
    public StatisticMessageSendData mapRow(ResultSet resultSet, int i) throws SQLException {
        DecimalFormat df=new DecimalFormat("0");

        StatisticMessageSendData qo = new StatisticMessageSendData();
        qo.setBusinessAccount(resultSet.getString("BUSINESS_ACCOUNT"));
        qo.setProtocol(resultSet.getString("PROTOCOL"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setSign(resultSet.getString("SIGN"));
        int sendNumber = resultSet.getInt("SUCCESS_NUMBER");
        qo.setSendNumber(sendNumber);
        int successNumber = resultSet.getInt("SUCCESS_SEND_NUMBER");
        qo.setSuccessNumber(successNumber);
        int failureNumber = resultSet.getInt("FAILURE_NUMBER");
        qo.setFailureNumber(failureNumber);
        int noReportNumber = sendNumber - successNumber - failureNumber;

        qo.setNoReportNumber(noReportNumber);

        if(sendNumber>0){
            qo.setSuccessRate(Integer.parseInt(df.format(100*(double)successNumber/sendNumber)));
            qo.setFailureRate(Integer.parseInt(df.format(100*(double)failureNumber/sendNumber)));
            qo.setNoReportRate(Integer.parseInt(df.format(100*(double)noReportNumber/sendNumber)));
        }

        qo.setCreatedTime(resultSet.getString("MESSAGE_DATE"));

        return qo;
    }
}
