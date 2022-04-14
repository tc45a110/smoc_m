package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class AccountStatisticComplaintRowMapper implements RowMapper<AccountStatisticComplaintData> {

    @Override
    public AccountStatisticComplaintData mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountStatisticComplaintData qo = new AccountStatisticComplaintData();
        qo.setMonth(resultSet.getString("MONTH_DAY"));
        qo.setComplaint(resultSet.getString("COMPLAINT_NUM"));

        return qo;
    }
}
