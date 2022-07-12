package com.smoc.cloud.reconciliation.rowmapper;

import com.smoc.cloud.common.smoc.reconciliation.ReconciliationCarrierItemsValidator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReconciliationCarrierRowMapper implements RowMapper<ReconciliationCarrierItemsValidator> {
    @Override
    public ReconciliationCarrierItemsValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        ReconciliationCarrierItemsValidator qo = new ReconciliationCarrierItemsValidator();
        qo.setChannelPeriod(resultSet.getString("MESSAGE_DATE"));
        qo.setChannelProvder(resultSet.getString("CHANNEL_PROVDER"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setSrcId(resultSet.getString("SRC_ID"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setTotalSendQuantity(resultSet.getLong("MESSAGE_SUCCESS_NUM"));
        qo.setPrice(resultSet.getBigDecimal("CHANNEL_PRICE"));
        qo.setChannelPeriodStatus(resultSet.getString("CHANNEL_PERIOD_STATUS"));
        qo.setCarrierTotalAmount(resultSet.getBigDecimal("CARRIER_TOTAL_AMOUNT"));
        qo.setCarrierTotalSendQuantity(resultSet.getLong("CARRIER_TOTAL_SEND_QUANTITY"));

        if(!StringUtils.isEmpty(qo.getPrice())){
            qo.setPrice(new BigDecimal(qo.getPrice().stripTrailingZeros().toPlainString()));
            qo.setTotalAmount(new BigDecimal(new BigDecimal(qo.getTotalSendQuantity()).multiply(qo.getPrice()).stripTrailingZeros().toPlainString()));
        }else{
            qo.setTotalAmount(new BigDecimal(0));
        }

        //运营商总额
        if(!StringUtils.isEmpty(resultSet.getBigDecimal("CARRIER_TOTAL_AMOUNT"))){
            qo.setCarrierTotalAmount(new BigDecimal(qo.getCarrierTotalAmount().stripTrailingZeros().toPlainString()));
        }

        //金额差额
        if(!StringUtils.isEmpty(qo.getCarrierTotalAmount())){
            qo.setAmountDifference(new BigDecimal(qo.getTotalAmount().subtract(qo.getCarrierTotalAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()));

            qo.setQuantityDifference(qo.getTotalSendQuantity() - qo.getCarrierTotalSendQuantity());
            qo.setQuantityDifferenceRate( new BigDecimal(qo.getQuantityDifference()).divide(new BigDecimal(qo.getCarrierTotalSendQuantity()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());
        }

        return qo;
    }
}
