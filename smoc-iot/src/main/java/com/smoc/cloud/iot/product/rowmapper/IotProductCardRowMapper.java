package com.smoc.cloud.iot.product.rowmapper;

import com.smoc.cloud.common.iot.validator.IotProductCardValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IotProductCardRowMapper implements RowMapper<IotProductCardValidator> {
    @Override
    public IotProductCardValidator mapRow(ResultSet rs, int rowNum) throws SQLException {
        IotProductCardValidator qo = new IotProductCardValidator();
        qo.setId(rs.getString("ID"));
        qo.setProductId(rs.getString("PRODUCT_ID"));
        qo.setCardMsisdn(rs.getString("CARD_MSISDN"));
        qo.setCardImsi(rs.getString("CARD_IMSI"));
        qo.setCardIccid(rs.getString("CARD_ICCID"));
        qo.setStatus(rs.getString("STATUS"));
        qo.setCreatedBy(rs.getString("CREATED_BY"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
