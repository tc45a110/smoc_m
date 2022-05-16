package com.smoc.cloud.intelligence.rowmapper;

import com.smoc.cloud.common.smoc.intelligence.IntellectCallbackShowReportValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntellectCallbackShowReportRowMapper implements RowMapper<IntellectCallbackShowReportValidator> {
    @Override
    public IntellectCallbackShowReportValidator mapRow(ResultSet resultSet, int i) throws SQLException {
        IntellectCallbackShowReportValidator qo = new IntellectCallbackShowReportValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setOrderNo(resultSet.getString("ORDER_NO"));
        qo.setCustFlag(resultSet.getString("CUST_FLAG"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setCustId(resultSet.getString("CUST_ID"));
        qo.setTplId(resultSet.getString("TPL_ID"));
        qo.setAimUrl(resultSet.getString("AIM_URL"));
        qo.setAimCode(resultSet.getString("AIM_CODE"));
        qo.setExtData(resultSet.getString("EXT_DATA"));
        qo.setStatus(resultSet.getInt("STATUS"));
        qo.setDescribe(resultSet.getString("DESCRIBE"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
