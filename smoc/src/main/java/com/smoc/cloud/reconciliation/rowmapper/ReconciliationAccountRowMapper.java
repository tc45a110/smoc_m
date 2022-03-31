package com.smoc.cloud.reconciliation.rowmapper;

import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationAccountModel;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReconciliationAccountRowMapper implements RowMapper<ReconciliationAccountModel> {
    @Override
    public ReconciliationAccountModel mapRow(ResultSet resultSet, int i) throws SQLException {

        ReconciliationAccountModel qo = new ReconciliationAccountModel();
        qo.setAccountingPeriod(resultSet.getString("ACCOUNT_PERIOD"));
        qo.setAccount(resultSet.getString("ACCOUNT_ID"));
//        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setSendSum(resultSet.getLong("TOTAL_SEND_QUANTITY"));
        qo.setPrice(resultSet.getBigDecimal("PRICE"));
        qo.setPayType(resultSet.getString("CHARGE_TYPE"));
        if(!StringUtils.isEmpty(qo.getPrice())){
            qo.setPrice(new BigDecimal(qo.getPrice().stripTrailingZeros().toPlainString()));
        }
        qo.setTotalSum(new BigDecimal(qo.getSendSum()).multiply(qo.getPrice()));
        return qo;
    }
}
