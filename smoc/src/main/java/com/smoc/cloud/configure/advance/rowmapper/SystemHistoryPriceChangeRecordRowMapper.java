package com.smoc.cloud.configure.advance.rowmapper;

import com.smoc.cloud.common.smoc.configuate.validator.SystemHistoryPriceChangeRecordValidator;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SystemHistoryPriceChangeRecordRowMapper implements RowMapper<SystemHistoryPriceChangeRecordValidator> {
    @Override
    public SystemHistoryPriceChangeRecordValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        SystemHistoryPriceChangeRecordValidator qo = new SystemHistoryPriceChangeRecordValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setChangeType(resultSet.getString("CHANGE_TYPE"));
        qo.setBusinessId(resultSet.getString("BUSINESS_ID"));
        qo.setPriceArea(resultSet.getString("PRICE_AREA"));
        qo.setAreaType(resultSet.getString("AREA_TYPE"));
        qo.setStartDate(resultSet.getString("START_DATE"));
        qo.setChangePrice(new BigDecimal(resultSet.getBigDecimal("CHANGE_PRICE").stripTrailingZeros().toPlainString()));
        qo.setTaskType(resultSet.getString("TASK_TYPE"));
        qo.setTaskStatus(resultSet.getString("TASK_STATUS"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
