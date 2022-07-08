package com.smoc.cloud.reconciliation.rowmapper;

import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationChannelCarrierModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReconciliationChannelRowMapper implements RowMapper<ReconciliationChannelCarrierModel> {
    @Override
    public ReconciliationChannelCarrierModel mapRow(ResultSet resultSet, int i) throws SQLException {

        ReconciliationChannelCarrierModel qo = new ReconciliationChannelCarrierModel();
        qo.setChannelPeriod(resultSet.getString("MESSAGE_DATE"));
        qo.setChannelProvder(resultSet.getString("CHANNEL_PROVDER"));
        qo.setChannelPeriodStatus(resultSet.getString("CHANNEL_PERIOD_STATUS"));
        return qo;
    }
}
