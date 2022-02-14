package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class AccountFinanceInfoRowMapper implements RowMapper<AccountFinanceInfoValidator> {

    @Override
    public AccountFinanceInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountFinanceInfoValidator qo = new AccountFinanceInfoValidator();
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setPayType(resultSet.getString("PAY_TYPE"));
        qo.setChargeType(resultSet.getString("CHARGE_TYPE"));
        qo.setFrozenReturnDate(resultSet.getString("FROZEN_RETURN_DATE"));
        qo.setAccountCreditSum(resultSet.getBigDecimal("ACCOUNT_CREDIT_SUM"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setCarrierType(resultSet.getString("CARRIER_TYPE"));
        BigDecimal price = resultSet.getBigDecimal("CARRIER_PRICE");
        if(!StringUtils.isEmpty(price)){
            qo.setCarrierPrice(new BigDecimal(price.stripTrailingZeros().toPlainString()));
        }else{
            qo.setCarrierPrice(price);
        }

        return qo;
    }
}
