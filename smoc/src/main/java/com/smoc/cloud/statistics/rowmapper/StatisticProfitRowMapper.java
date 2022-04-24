package com.smoc.cloud.statistics.rowmapper;

import com.smoc.cloud.common.smoc.customer.qo.StatisticProfitData;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class StatisticProfitRowMapper implements RowMapper<StatisticProfitData> {

    @Override
    public StatisticProfitData mapRow(ResultSet resultSet, int i) throws SQLException {

        StatisticProfitData qo = new StatisticProfitData();
        qo.setMonth(resultSet.getString("MONTH_DAY"));
        BigDecimal sendNumber = resultSet.getBigDecimal("PROFIT_NUM");
        if(!StringUtils.isEmpty(sendNumber)){
            qo.setProfit(new BigDecimal(sendNumber.stripTrailingZeros().toPlainString()));
        }else{
            qo.setProfit(new BigDecimal(0));
        }

        return qo;
    }
}
