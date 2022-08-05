package com.smoc.cloud.spss.rowmapper;

import com.smoc.cloud.common.smoc.spss.qo.ManagerCarrierStatisticQo;
import com.smoc.cloud.common.smoc.spss.qo.ManagerStatisticQo;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class ManagerCarrierStatisticModelRowMapper implements RowMapper<ManagerCarrierStatisticQo> {

    @Override
    public ManagerCarrierStatisticQo mapRow(ResultSet resultSet, int i) throws SQLException {

        ManagerCarrierStatisticQo qo = new ManagerCarrierStatisticQo();
        qo.setMessageDate(resultSet.getString("MESSAGE_DATE"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setCarrierData(resultSet.getBigDecimal("SUCCESS_SUM").divide(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP));

        return qo;
    }
}
