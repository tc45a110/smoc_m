package com.smoc.cloud.iot.carrier.rowmapper;

import com.smoc.cloud.common.iot.validator.IotCarrierInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IotCarrierInfoRowMapper implements RowMapper<IotCarrierInfoValidator> {
    @Override
    public IotCarrierInfoValidator mapRow(ResultSet rs, int rowNum) throws SQLException {
        IotCarrierInfoValidator qo = new IotCarrierInfoValidator();
        qo.setId(rs.getString("ID"));
        qo.setCarrier(rs.getString("CARRIER"));
        qo.setCarrierName(rs.getString("CARRIER_NAME"));
        qo.setCarrierIdentifying(rs.getString("CARRIER_IDENTIFYING"));
        qo.setCarrierUsername(rs.getString("CARRIER_USERNAME"));
        qo.setCarrierServerUrl(rs.getString("CARRIER_SERVER_URL"));
        qo.setApiType(rs.getString("API_TYPE"));
        qo.setCarrierStatus(rs.getString("CARRIER_STATUS"));
        qo.setCreatedBy(rs.getString("CREATED_BY"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
