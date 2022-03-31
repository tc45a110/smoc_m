package com.smoc.cloud.reconciliation.rowmapper;

import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationEnterpriseModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReconciliationEnterpriseModelRowMapper  implements RowMapper<ReconciliationEnterpriseModel> {
    @Override
    public ReconciliationEnterpriseModel mapRow(ResultSet resultSet, int i) throws SQLException {

        ReconciliationEnterpriseModel qo = new ReconciliationEnterpriseModel();
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setAccountingPeriod(resultSet.getString("ACCOUNT_PERIOD"));
        qo.setAccountingStatus(resultSet.getString("ACCOUNT_PERIOD_STATUS"));
        return qo;
    }
}
