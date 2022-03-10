package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.AccountPriceHistoryValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountPriceHistoryRowMapper implements RowMapper<AccountPriceHistoryValidator> {
    @Override
    public AccountPriceHistoryValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountPriceHistoryValidator qo = new AccountPriceHistoryValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setSourceId(resultSet.getString("SOURCE_ID"));
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setCarrierType(resultSet.getString("CARRIER_TYPE"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setCarrierPrice(resultSet.getBigDecimal("CARRIER_PRICE"));
        qo.setPriceDate(resultSet.getString("PRICE_DATE"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        qo.setUpdatedTime(resultSet.getString("UPDATED_TIME"));
        return qo;
    }
}
