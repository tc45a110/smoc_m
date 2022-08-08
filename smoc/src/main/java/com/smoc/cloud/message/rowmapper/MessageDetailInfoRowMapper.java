package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class MessageDetailInfoRowMapper implements RowMapper<MessageDetailInfoValidator> {
    @Override
    public MessageDetailInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageDetailInfoValidator qo = new MessageDetailInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setBusinessAccount(resultSet.getString("ACCOUNT_ID"));
        qo.setPhoneNumber(resultSet.getString("PHONE_NUMBER"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setArea(resultSet.getString("AREA_NAME"));
        qo.setCustomerStatus(resultSet.getString("STATUS_CODE"));
        qo.setMessageContent(resultSet.getString("MESSAGE_CONTENT"));
        qo.setSign(resultSet.getString("SIGN"));
        qo.setSubmitStyle(resultSet.getString("SUBMIT_STYLE"));
        qo.setMessageNumber(resultSet.getInt("MESSAGE_TOTAL"));

        String reportTime = resultSet.getString("REPORT_TIME");
        String submitTime = resultSet.getString("SUBMIT_TIME");
        if(!StringUtils.isEmpty(reportTime) && !StringUtils.isEmpty(submitTime)){
            Date reportDate = DateTimeUtils.getDateTimeSSSFormat(reportTime);
            Date submitDate = DateTimeUtils.getDateTimeSSSFormat(submitTime);
            qo.setTimeElapsed(DateTimeUtils.getTimeInMillis(reportDate,submitDate));
        }

        qo.setReportTime(reportTime);
        qo.setSubmitTime(submitTime);


        return qo;
    }
}
