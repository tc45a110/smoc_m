package com.smoc.cloud.scheduler.batch.filters.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BusinessRouteValueRowMapper implements RowMapper<BusinessRouteValue> {
    @Override
    public BusinessRouteValue mapRow(ResultSet rs, int rowNum) throws SQLException {
        BusinessRouteValue qo = new BusinessRouteValue();
        qo.setId(rs.getLong("ID"));
        qo.setAccountId(rs.getString("ACCOUNT_ID"));
        qo.setPhoneNumber(rs.getString("PHONE_NUMBER"));
        qo.setAccountSubmitTime(rs.getString("SUBMIT_TIME"));
        qo.setMessageContent(rs.getString("MESSAGE_CONTENT"));
        qo.setMessageFormat(rs.getString("MESSAGE_FORMAT"));
        qo.setAccountMessageIds(rs.getString("MESSAGE_ID"));
        qo.setAccountTemplateId(rs.getString("TEMPLATE_ID"));
        qo.setProtocol(rs.getString("PROTOCOL"));
        qo.setAccountSubmitSrcId(rs.getString("ACCOUNT_SRC_ID"));
        qo.setAccountBusinessCode(rs.getString("ACCOUNT_BUSINESS_CODE"));
        qo.setTaskPhoneNumberNumber(rs.getLong("PHONE_NUMBER_NUMBER"));
        qo.setTaskMessageNumber(rs.getLong("MESSAGE_CONTENT_NUMBER"));
        qo.setAccountReportFlag(rs.getInt("REPORT_FLAG"));
        qo.setOptionParam(rs.getString("OPTION_PARAM"));
        qo.setAccountSubmitTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
