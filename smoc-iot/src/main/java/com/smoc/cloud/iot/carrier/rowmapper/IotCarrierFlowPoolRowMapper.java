package com.smoc.cloud.iot.carrier.rowmapper;

import com.smoc.cloud.common.iot.validator.IotCarrierFlowPoolValidator;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IotCarrierFlowPoolRowMapper implements RowMapper<IotCarrierFlowPoolValidator> {
    @Override
    public IotCarrierFlowPoolValidator mapRow(ResultSet rs, int rowNum) throws SQLException {
        IotCarrierFlowPoolValidator qo = new IotCarrierFlowPoolValidator();
        qo.setId(rs.getString("ID"));
        qo.setCarrierId(rs.getString("CARRIER_ID"));
        qo.setPoolName(rs.getString("POOL_NAME"));
        qo.setCarrierName(rs.getString("CARRIER_NAME"));
        qo.setPoolType(rs.getString("POOL_TYPE"));
        qo.setPoolCardNumber(rs.getInt("POOL_CARD_NUMBER"));
        qo.setPoolSize(new BigDecimal(rs.getBigDecimal("POOL_SIZE").stripTrailingZeros().toPlainString()));
        qo.setUsedAmount(new BigDecimal(rs.getBigDecimal("USED_AMOUNT").stripTrailingZeros().toPlainString()));
        qo.setSyncDate(rs.getString("SYNC_DATE"));
        qo.setWarningLevel(rs.getInt("WARNING_LEVEL"));
        qo.setContinueType(rs.getString("CONTINUE_TYPE"));
        qo.setPoolStatus(rs.getString("POOL_STATUS"));
        qo.setCreatedBy(rs.getString("CREATED_BY"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
