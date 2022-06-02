package com.smoc.cloud.statistics.rowmapper;

import com.smoc.cloud.common.smoc.index.CheckRemindModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class StatisticCheckRemindRowMapper implements RowMapper<CheckRemindModel> {

    @Override
    public CheckRemindModel mapRow(ResultSet resultSet, int i) throws SQLException {

        CheckRemindModel qo = new CheckRemindModel();
        qo.setTotalNum(resultSet.getInt("TOTAL_NUM"));

        return qo;
    }
}
