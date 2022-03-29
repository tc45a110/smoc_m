package com.smoc.cloud.reconciliation.rowmapper;

import com.smoc.cloud.common.smoc.reconciliation.ReconciliationPeriodValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReconciliationPeriodRowMapper implements RowMapper<ReconciliationPeriodValidator> {
    @Override
    public ReconciliationPeriodValidator mapRow(ResultSet resultSet, int i) throws SQLException {
        ReconciliationPeriodValidator qo = new ReconciliationPeriodValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setAccountPeriod(resultSet.getString("ACCOUNT_PERIOD"));
        qo.setAccountPeriodType(resultSet.getString("ACCOUNT_PERIOD_TYPE"));
        qo.setAccountPeriodStartDate(resultSet.getString("ACCOUNT_PERIOD_START_DATE"));
        qo.setAccountPeriodEndDate(resultSet.getString("ACCOUNT_PERIOD_END_DATE"));
        qo.setAccountPeriodStatus(resultSet.getString("ACCOUNT_PERIOD_STATUS"));
        qo.setStatus(resultSet.getString("STATUS"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
