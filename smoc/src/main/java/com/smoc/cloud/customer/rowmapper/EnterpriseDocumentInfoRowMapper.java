package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.EnterpriseDocumentInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 * 2020/5/9 18:59
 **/
public class EnterpriseDocumentInfoRowMapper implements RowMapper<EnterpriseDocumentInfoValidator> {

    @Override
    public EnterpriseDocumentInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        EnterpriseDocumentInfoValidator qo = new EnterpriseDocumentInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setEnterpriseType(resultSet.getString("ENTERPRISE_TYPE"));
        qo.setSignName(resultSet.getString("SIGN_NAME"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setShortLink(resultSet.getString("SHORT_LINK"));
        qo.setDocKey(resultSet.getString("DOC_KEY"));
        qo.setDocStatus(resultSet.getString("DOC_STATUS"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));

        return qo;
    }
}
