package com.smoc.cloud.complaint.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageComplaintInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 * 2020/5/9 18:59
 **/
public class MessageComplaintInfoRowMapper implements RowMapper<MessageComplaintInfoValidator> {

    @Override
    public MessageComplaintInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageComplaintInfoValidator qo = new MessageComplaintInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setBusinessAccount(resultSet.getString("BUSINESS_ACCOUNT"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setReportNumber(resultSet.getString("REPORT_NUMBER"));
        qo.setNumberCode(resultSet.getString("NUMBER_CODE"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setSendDate(resultSet.getString("SEND_DATE"));
        qo.setSendRate(resultSet.getString("SEND_RATE"));
        qo.setReportContent(resultSet.getString("REPORT_CONTENT"));
        qo.setContentType(resultSet.getString("CONTENT_TYPE"));
        qo.setIs12321(resultSet.getString("IS_12321"));
        qo.setReportSource(resultSet.getString("REPORT_SOURCE"));
        qo.setReportDate(resultSet.getString("REPORT_DATE"));
        qo.setReportedNumber(resultSet.getString("REPORTED_NUMBER"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        qo.setCarrierSource(resultSet.getString("CARRIER_SOURCE"));
        qo.setHandleCarrierId(resultSet.getString("HANDLE_CARRIER_ID"));
        qo.setSendType(resultSet.getString("SEND_TYPE"));
        qo.setReportedProvince(resultSet.getString("REPORTED_PROVINCE"));
        qo.setReportedCity(resultSet.getString("REPORTED_CITY"));
        qo.setReportProvince(resultSet.getString("REPORT_PROVINCE"));
        qo.setReportCity(resultSet.getString("REPORT_CITY"));
        qo.setReportChann(resultSet.getString("REPORT_CHANN"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));


        return qo;
    }
}
