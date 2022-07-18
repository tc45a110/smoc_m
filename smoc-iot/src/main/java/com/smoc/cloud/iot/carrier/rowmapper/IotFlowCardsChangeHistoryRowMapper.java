package com.smoc.cloud.iot.carrier.rowmapper;

import com.smoc.cloud.common.iot.validator.IotFlowCardsChangeHistoryValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IotFlowCardsChangeHistoryRowMapper implements RowMapper<IotFlowCardsChangeHistoryValidator> {
    @Override
    public IotFlowCardsChangeHistoryValidator mapRow(ResultSet rs, int rowNum) throws SQLException {
        IotFlowCardsChangeHistoryValidator qo = new IotFlowCardsChangeHistoryValidator();
        qo.setId(rs.getString("ID"));
        qo.setIccid(rs.getString("ICCID"));
        qo.setImsi(rs.getString("IMSI"));
        qo.setMsisdn(rs.getString("MSISDN"));
        qo.setOriginalStatus(rs.getString("ORIGINAL_STATUS"));
        qo.setTargetStatus(rs.getString("TARGET_STATUS"));
        qo.setChangeTime(rs.getString("CHANGE_TIME"));
        return qo;
    }
}
