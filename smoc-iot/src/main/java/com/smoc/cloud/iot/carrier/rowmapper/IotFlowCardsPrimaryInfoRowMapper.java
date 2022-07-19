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
        qo.setCardType(rs.getString("CARD_TYPE"));
        qo.setOrderNum(rs.getString("ORDER_NUM"));
        qo.setMsisdn(rs.getString("MSISDN"));
        qo.setImsi(rs.getString("IMSI"));
        qo.setIccid(rs.getString("ICCID"));
        qo.setFlowPoolId(rs.getString("FLOW_POOL_ID"));
        qo.setChangingType(rs.getString("CHANGING_TYPE"));
        qo.setCycleQuota(new BigDecimal(rs.getBigDecimal("CYCLE_QUOTA").stripTrailingZeros().toPlainString()));
        qo.setActiveDate(rs.getString("ACTIVE_DATE"));
        qo.setOpenDate(rs.getString("OPEN_DATE"));
        qo.setUseStatus(rs.getString("USE_STATUS"));
        qo.setCardStatus(rs.getString("CARD_STATUS"));
        qo.setCreatedBy(rs.getString("CREATED_BY"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
