package com.smoc.cloud.saler.rowmapper;

import com.smoc.cloud.common.smoc.saler.qo.CustomerComplaintQo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class SalerCustomerComplaintRowMapper implements RowMapper<CustomerComplaintQo> {

    @Override
    public CustomerComplaintQo mapRow(ResultSet resultSet, int i) throws SQLException {

        CustomerComplaintQo qo = new CustomerComplaintQo();
        qo.setMonth(resultSet.getString("MONTH_DAY"));
        qo.setComplaint(resultSet.getString("COMPLAINT_NUM"));

        return qo;
    }
}
