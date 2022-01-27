package com.smoc.cloud.identification.rowmapper;

import com.smoc.cloud.common.smoc.identification.validator.IdentificationOrdersInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IdentificationOrdersInfoRowMapper implements RowMapper<IdentificationOrdersInfoValidator> {
    @Override
    public IdentificationOrdersInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {
        IdentificationOrdersInfoValidator qo = new IdentificationOrdersInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setIdentificationAccount(resultSet.getString("IDENTIFICATION_ACCOUNT"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setOrderNo(resultSet.getString("ORDER_NO"));
        qo.setOrderType(resultSet.getString("ORDER_TYPE"));
        qo.setIdentificationPrice(resultSet.getBigDecimal("IDENTIFICATION_PRICE"));
        qo.setIdentificationPriceStatus(resultSet.getString("IDENTIFICATION_PRICE_STATUS"));
        qo.setIdentificationOrderNo(resultSet.getString("IDENTIFICATION_ORDER_NO"));
        qo.setIdentificationStatus(resultSet.getString("IDENTIFICATION_STATUS"));
        qo.setIdentificationMessage(resultSet.getString("IDENTIFICATION_MESSAGE"));
        qo.setCostPrice(resultSet.getBigDecimal("COST_PRICE"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
