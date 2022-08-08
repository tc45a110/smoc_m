package com.smoc.cloud.spss.rowmapper;

import com.smoc.cloud.common.smoc.spss.qo.StatisticIncomeQo;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class StatisticIncomeRowMapper implements RowMapper<StatisticIncomeQo> {

    @Override
    public StatisticIncomeQo mapRow(ResultSet resultSet, int i) throws SQLException {

        StatisticIncomeQo qo = new StatisticIncomeQo();
        qo.setMonth(resultSet.getString("MESSAGE_DATE"));
        qo.setIncome(resultSet.getBigDecimal("PROFIT_SUM").setScale(2, BigDecimal.ROUND_HALF_UP));

        return qo;
    }
}
