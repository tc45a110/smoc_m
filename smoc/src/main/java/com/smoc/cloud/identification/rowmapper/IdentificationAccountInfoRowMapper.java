package com.smoc.cloud.identification.rowmapper;

import com.smoc.cloud.common.smoc.identification.validator.IdentificationAccountInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IdentificationAccountInfoRowMapper implements RowMapper<IdentificationAccountInfoValidator> {
    @Override
    public IdentificationAccountInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {
        IdentificationAccountInfoValidator qo = new IdentificationAccountInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setIdentificationAccount(resultSet.getString("IDENTIFICATION_ACCOUNT"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setMd5HmacKey(resultSet.getString("MD5_HMAC_KEY"));
        qo.setAesKey(resultSet.getString("AES_KEY"));
        qo.setAesIv(resultSet.getString("AES_IV"));
        qo.setIdentificationPrice(resultSet.getBigDecimal("IDENTIFICATION_PRICE"));
        qo.setIdentificationFacePrice(resultSet.getBigDecimal("IDENTIFICATION_FACE_PRICE"));
        qo.setGrantingCredit(resultSet.getBigDecimal("GRANTING_CREDIT"));
        qo.setAccountType(resultSet.getString("ACCOUNT_TYPE"));
        qo.setAccountStatus(resultSet.getString("ACCOUNT_STATUS"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
