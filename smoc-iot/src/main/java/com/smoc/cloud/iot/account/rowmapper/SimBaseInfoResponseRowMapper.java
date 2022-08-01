package com.smoc.cloud.iot.account.rowmapper;

import com.smoc.cloud.api.response.info.SimBaseInfoResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SimBaseInfoResponseRowMapper implements RowMapper<SimBaseInfoResponse> {
    @Override
    public SimBaseInfoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        SimBaseInfoResponse qo = new SimBaseInfoResponse();
        qo.setIccid(rs.getString("ICCID"));
        qo.setMsisdn(rs.getString("MSISDN"));
        qo.setImsi(rs.getString("IMSI"));
        qo.setImsi(rs.getString("IMEI"));
        qo.setChargingType(rs.getString("CARD_TYPE"));
        qo.setOpenCardFee(rs.getBigDecimal("OPEN_CARD_FEE").stripTrailingZeros().toPlainString());
        qo.setCycleFunctionFee(rs.getBigDecimal("CYCLE_FUNCTION_FEE").stripTrailingZeros().toPlainString());
        qo.setDateActivated(rs.getString("ACTIVE_DATE"));
        qo.setDateOpen(rs.getString("OPEN_DATE"));
        qo.setCardStatus(rs.getString("CARD_STATUS"));
        return qo;
    }
}
