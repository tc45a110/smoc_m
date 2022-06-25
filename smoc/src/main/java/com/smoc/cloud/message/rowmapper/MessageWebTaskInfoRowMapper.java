package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * web任务单
 */
public class MessageWebTaskInfoRowMapper implements RowMapper<MessageWebTaskInfoValidator> {
    @Override
    public MessageWebTaskInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageWebTaskInfoValidator qo = new MessageWebTaskInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setProtocolType(resultSet.getString("PROTOCOL_TYPE"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setTemplateId(resultSet.getString("TEMPLATE_ID"));
        qo.setBusinessAccount(resultSet.getString("BUSINESS_ACCOUNT"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setSendType(resultSet.getString("SEND_TYPE"));
        qo.setTimingTime(resultSet.getString("TIMING_TIME"));
        qo.setExpandNumber(resultSet.getString("EXPAND_NUMBER"));
        qo.setSubmitNumber(resultSet.getInt("SUBMIT_NUMBER"));
        qo.setSuccessNumber(resultSet.getInt("SUCCESS_NUMBER"));
        qo.setSuccessSendNumber(resultSet.getInt("SUCCESS_SEND_NUMBER"));
        qo.setFailureNumber(resultSet.getInt("FAILURE_NUMBER"));
        qo.setNoReportNumber(resultSet.getInt("NO_REPORT_NUMBER"));
        qo.setSendTime(resultSet.getString("SEND_TIME"));
        qo.setSendStatus(resultSet.getString("SEND_STATUS"));
        qo.setMessageContent(resultSet.getString("MESSAGE_CONTENT"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setSplitNumber(resultSet.getInt("SPLIT_NUMBER"));
        qo.setMessageType(resultSet.getString("MESSAGE_TYPE"));
        qo.setSubject(resultSet.getString("SUBJECT"));

        return qo;
    }
}
