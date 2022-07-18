package com.smoc.cloud.iot.account.rowmapper;

import com.smoc.cloud.common.iot.validator.IotUserProductInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IotUserProductInfoRowMapper implements RowMapper<IotUserProductInfoValidator> {
    @Override
    public IotUserProductInfoValidator mapRow(ResultSet rs, int rowNum) throws SQLException {
        IotUserProductInfoValidator qo = new IotUserProductInfoValidator();
        qo.setId(rs.getString("ID"));
        qo.setUserId(rs.getString("USER_ID"));
        qo.setCreatedBy(rs.getString("CREATED_BY"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
