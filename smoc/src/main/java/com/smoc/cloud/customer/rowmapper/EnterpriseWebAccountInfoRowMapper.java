package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.EnterpriseWebAccountInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 * 2020/5/9 18:59
 **/
public class EnterpriseWebAccountInfoRowMapper implements RowMapper<EnterpriseWebAccountInfoValidator> {

    @Override
    public EnterpriseWebAccountInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        EnterpriseWebAccountInfoValidator qo = new EnterpriseWebAccountInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setWebLoginName(resultSet.getString("WEB_LOGIN_NAME"));
        qo.setAccountStatus(resultSet.getString("ACCOUNT_STATUS"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));

        return qo;
    }
}
