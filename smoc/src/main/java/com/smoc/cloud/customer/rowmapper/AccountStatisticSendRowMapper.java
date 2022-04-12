package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class AccountStatisticSendRowMapper implements RowMapper<AccountStatisticSendData> {

    @Override
    public AccountStatisticSendData mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountStatisticSendData qo = new AccountStatisticSendData();
        qo.setData1(resultSet.getString("MONTH_DAY"));
        qo.setData2(resultSet.getString("SEND_NUMBER"));
        qo.setIndex(resultSet.getInt("INDEX"));
        return qo;
    }
}
