package com.smoc.cloud.iot.carrier.rowmapper;

import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IotFlowCardsPrimaryInfoRowMapper implements RowMapper<IotFlowCardsPrimaryInfoValidator> {
    @Override
    public IotFlowCardsPrimaryInfoValidator mapRow(ResultSet rs, int rowNum) throws SQLException {
        IotFlowCardsPrimaryInfoValidator qo = new IotFlowCardsPrimaryInfoValidator();
        qo.setId(rs.getString("ID"));
        qo.setCarrier(rs.getString("CARRIER"));
        qo.setCarrierName(rs.getString("CARRIER_NAME"));
        qo.setCardType(rs.getString("CARD_TYPE"));
        qo.setOrderNum(rs.getString("ORDER_NUM"));
        qo.setIccid(rs.getString("ICCID"));
        qo.setMsisdn(rs.getString("MSISDN"));
        qo.setImsi(rs.getString("IMSI"));
        qo.setImei(rs.getString("IMEI"));
        qo.setChargeCycle(rs.getString("CHARGE_CYCLE"));
        qo.setChargeType(rs.getString("CHARGE_TYPE"));
        qo.setFlowPoolId(rs.getString("FLOW_POOL_ID"));
        qo.setCycleQuota(new BigDecimal(rs.getBigDecimal("CYCLE_QUOTA").stripTrailingZeros().toPlainString()));
        BigDecimal openCardFee = rs.getBigDecimal("OPEN_CARD_FEE");
        qo.setOpenCardFee(new BigDecimal(openCardFee.stripTrailingZeros().toPlainString()));
        qo.setCycleFunctionFee(new BigDecimal(rs.getBigDecimal("CYCLE_FUNCTION_FEE").stripTrailingZeros().toPlainString()));
        qo.setActiveDate(rs.getString("ACTIVE_DATE"));
        qo.setOpenDate(rs.getString("OPEN_DATE"));
        qo.setUseStatus(rs.getString("USE_STATUS"));
        qo.setCardStatus(rs.getString("CARD_STATUS"));
        qo.setCreatedBy(rs.getString("CREATED_BY"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
