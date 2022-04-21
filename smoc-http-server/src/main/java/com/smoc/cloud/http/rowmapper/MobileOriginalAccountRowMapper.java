package com.smoc.cloud.http.rowmapper;

import com.smoc.cloud.http.entity.MobileOriginalAccount;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MobileOriginalAccountRowMapper implements RowMapper<MobileOriginalAccount> {
    @Override
    public MobileOriginalAccount mapRow(ResultSet resultSet, int i) throws SQLException {

        MobileOriginalAccount account = new MobileOriginalAccount();
        account.setAccount(resultSet.getString("ACCOUNT_ID"));
        account.setMoUrl(resultSet.getString("MO_URL"));
        account.setReportUrl(resultSet.getString("STATUS_REPORT_URL"));
        return account;
    }
}
