package com.smoc.cloud.configure.number.rowmapper;

import com.smoc.cloud.common.smoc.configuate.validator.ConfigNumberInfoValidator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 * 2020/5/9 18:59
 **/
public class ConfigNumberInfoRowMapper implements RowMapper<ConfigNumberInfoValidator> {

    @Override
    public ConfigNumberInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        ConfigNumberInfoValidator qo = new ConfigNumberInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setNumberCode(resultSet.getString("NUMBER_CODE"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setProvince(resultSet.getString("PROVINCE"));
        qo.setNumberCodeType(resultSet.getString("NUMBER_CODE_TYPE"));
        qo.setStatus(resultSet.getString("STATUS"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));

        return qo;
    }
}
