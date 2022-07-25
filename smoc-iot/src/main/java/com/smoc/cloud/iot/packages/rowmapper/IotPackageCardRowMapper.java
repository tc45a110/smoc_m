package com.smoc.cloud.iot.packages.rowmapper;

import com.smoc.cloud.common.iot.validator.IotPackageCardValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IotPackageCardRowMapper implements RowMapper<IotPackageCardValidator> {
    @Override
    public IotPackageCardValidator mapRow(ResultSet rs, int rowNum) throws SQLException {
        IotPackageCardValidator qo = new IotPackageCardValidator();
        qo.setId(rs.getString("ID"));
        qo.setPackageId(rs.getString("PACKAGE_ID"));
        qo.setCardMsisdn(rs.getString("CARD_MSISDN"));
        qo.setCardImsi(rs.getString("CARD_IMSI"));
        qo.setCardIccid(rs.getString("CARD_ICCID"));
        qo.setStatus(rs.getString("STATUS"));
        qo.setCreatedBy(rs.getString("CREATED_BY"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
