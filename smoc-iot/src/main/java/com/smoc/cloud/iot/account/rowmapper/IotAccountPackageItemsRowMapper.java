package com.smoc.cloud.iot.account.rowmapper;

import com.smoc.cloud.common.iot.validator.IotAccountPackageItemsValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IotAccountPackageItemsRowMapper implements RowMapper<IotAccountPackageItemsValidator> {
    @Override
    public IotAccountPackageItemsValidator mapRow(ResultSet rs, int rowNum) throws SQLException {
        IotAccountPackageItemsValidator qo = new IotAccountPackageItemsValidator();
        qo.setId(rs.getString("ID"));
        qo.setAccountId(rs.getString("ACCOUNT_ID"));
        qo.setCreatedBy(rs.getString("CREATED_BY"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
