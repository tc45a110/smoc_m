package com.smoc.cloud.http.rowmapper;

import com.smoc.cloud.common.http.server.message.response.ReportResponseParams;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportResponseParamsRowMapper implements RowMapper<ReportResponseParams> {
    @Override
    public ReportResponseParams mapRow(ResultSet resultSet, int i) throws SQLException {

        ReportResponseParams report = new ReportResponseParams();
        report.setId(resultSet.getString("ID"));
        report.setAccount(resultSet.getString("ACCOUNT_ID"));
        report.setMobile(resultSet.getString("PHONE_NUMBER"));
        report.setReportTime(resultSet.getString("REPORT_TIME"));
        report.setSubmitTime(resultSet.getString("SUBMIT_TIME"));
        report.setOrderNo(resultSet.getString("MESSAGE_ID"));
        report.setStatusCode(resultSet.getString("STATUS_CODE"));
        report.setTemplateId(resultSet.getString("TEMPLATE_ID"));
        report.setExtNumber(resultSet.getString("ACCOUNT_SRC_ID"));
        report.setBusiness(resultSet.getString("ACCOUNT_BUSINESS_CODE"));
        report.setMessageTotal(resultSet.getInt("MESSAGE_TOTAL"));
        report.setMessageIndex(resultSet.getInt("MESSAGE_INDEX"));
        return report;
    }
}
