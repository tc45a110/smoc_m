package com.smoc.cloud.spss.rowmapper;

import com.smoc.cloud.common.smoc.spss.qo.ManagerStatisticQo;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class ManagerStatisticModelRowMapper implements RowMapper<ManagerStatisticQo> {

    @Override
    public ManagerStatisticQo mapRow(ResultSet resultSet, int i) throws SQLException {

        ManagerStatisticQo qo = new ManagerStatisticQo();
        qo.setMessageDate(resultSet.getString("MESSAGE_DATE"));
        qo.setSendAmount(resultSet.getBigDecimal("MESSAGE_SUCCESS_NUM").divide(new BigDecimal(1000000)).setScale(2, BigDecimal.ROUND_HALF_UP));
        qo.setIncomeAmount(resultSet.getBigDecimal("ACCOUNT_PRICE_SUM").divide(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));
        BigDecimal channelPriceSum = resultSet.getBigDecimal("CHANNEL_PRICE_SUM").divide(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP);
        qo.setProfitAmount(qo.getIncomeAmount().subtract(channelPriceSum));

        return qo;
    }
}
