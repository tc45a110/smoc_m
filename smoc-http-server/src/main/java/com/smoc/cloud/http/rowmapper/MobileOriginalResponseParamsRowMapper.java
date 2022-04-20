package com.smoc.cloud.http.rowmapper;

import com.smoc.cloud.common.http.server.message.response.MobileOriginalResponseParams;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MobileOriginalResponseParamsRowMapper implements RowMapper<MobileOriginalResponseParams> {
    @Override
    public MobileOriginalResponseParams mapRow(ResultSet resultSet, int i) throws SQLException {

        MobileOriginalResponseParams mo = new MobileOriginalResponseParams();
        mo.setId(resultSet.getString("ID"));
        mo.setAccount(resultSet.getString("ACCOUNT_ID"));
        mo.setOrderNo(resultSet.getString("MESSAGE_ID"));
        mo.setMobile(resultSet.getString("PHONE_NUMBER"));
        mo.setMoTime(resultSet.getString("MO_TIME"));
        mo.setContent(resultSet.getString("MESSAGE_CONTENT"));
        mo.setExtNumber(resultSet.getString("ACCOUNT_SRC_ID"));
        mo.setBusiness(resultSet.getString("ACCOUNT_BUSINESS_CODE"));
        return mo;
    }
}
