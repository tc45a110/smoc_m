package com.smoc.cloud.iot.carrier.rowmapper;

import com.smoc.cloud.common.iot.validator.IotCarrierFlowPoolValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IotCarrierFlowPoolRowMapper implements RowMapper<IotCarrierFlowPoolValidator> {
    @Override
    public IotCarrierFlowPoolValidator mapRow(ResultSet rs, int rowNum) throws SQLException {
        IotCarrierFlowPoolValidator qo = new IotCarrierFlowPoolValidator();
        qo.setId(rs.getString("ID"));
        qo.setCarrierId(rs.getString("CARRIER_ID"));
        qo.setPoolName(rs.getString("POOL_NAME"));
        qo.setPoolCardNumber(rs.getInt("POOL_CARD_NUMBER"));
        qo.setPoolSize(rs.getBigDecimal("POOL_SIZE"));
        qo.setSyncDate(rs.getString("SYNC_DATE"));
        qo.setContinueType(rs.getString("CONTINUE_TYPE"));
        qo.setPoolStatus(rs.getString("POOL_STATUS"));
        qo.setCreatedBy(rs.getString("CREATED_BY"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
