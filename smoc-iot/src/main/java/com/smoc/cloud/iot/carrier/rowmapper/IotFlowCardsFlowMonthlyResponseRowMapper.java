package com.smoc.cloud.iot.carrier.rowmapper;

import com.smoc.cloud.api.response.flow.SimFlowUsedMonthlyResponse;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IotFlowCardsFlowMonthlyResponseRowMapper implements RowMapper<SimFlowUsedMonthlyResponse> {
    @Override
    public SimFlowUsedMonthlyResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        SimFlowUsedMonthlyResponse qo = new SimFlowUsedMonthlyResponse();
        qo.setIccid(rs.getString("ICCID"));
        qo.setPackageId(rs.getString("PACKAGE_ID"));
        qo.setPackageName(rs.getString("PACKAGE_NAME"));
        qo.setChargingType(rs.getString("CHARGING_TYPE"));
        qo.setOpenCardFee(new BigDecimal(rs.getBigDecimal("OPEN_CARD_FEE").stripTrailingZeros().toPlainString()));
        qo.setCycleFunctionFee(new BigDecimal(rs.getBigDecimal("CYCLE_FUNCTION_FEE").stripTrailingZeros().toPlainString()));
        qo.setCycleQuota(new BigDecimal(rs.getBigDecimal("CYCLE_QUOTA").stripTrailingZeros().toPlainString()));
        qo.setUsedAmount(new BigDecimal(rs.getBigDecimal("USED_AMOUNT").stripTrailingZeros().toPlainString()));
        qo.setTotalAmount(new BigDecimal(rs.getBigDecimal("TOTAL_AMOUNT").stripTrailingZeros().toPlainString()));
        qo.setRemainAmount(new BigDecimal(rs.getBigDecimal("REMAIN_AMOUNT").stripTrailingZeros().toPlainString()));
        qo.setSettlementFee(new BigDecimal(rs.getBigDecimal("SETTLEMENT_FEE").stripTrailingZeros().toPlainString()));
        qo.setUsedMonth(rs.getString("USED_MONTH"));
        qo.setSettlementStatus(rs.getString("SETTLEMENT_STATUS"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
