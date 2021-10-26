package com.smoc.cloud.auth.params.rowmapper;

import com.smoc.cloud.common.auth.qo.ConfiguationParams;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 2020/5/30 14:45
 **/
public class BaseSystemParamConfiguationRowMapper implements RowMapper<ConfiguationParams> {
    @Override
    public ConfiguationParams mapRow(ResultSet resultSet, int i) throws SQLException {

        ConfiguationParams qo = new ConfiguationParams();
        qo.setId(resultSet.getString("ID"));
        qo.setUserId(resultSet.getString("USER_ID"));
        qo.setParamCode(resultSet.getString("PARAM_CODE"));
        qo.setParamValue(resultSet.getString("PARAM_VALUE"));
        qo.setParamValueDesc(resultSet.getString("PARAM_VALUE_DESC"));
        qo.setStatus(resultSet.getString("STATUS"));
        qo.setDataDate(resultSet.getDate("DATA_DATE"));
        return qo;
    }
}
