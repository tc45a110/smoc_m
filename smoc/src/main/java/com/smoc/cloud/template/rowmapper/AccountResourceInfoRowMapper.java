package com.smoc.cloud.template.rowmapper;

import com.smoc.cloud.common.smoc.template.AccountResourceInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountResourceInfoRowMapper implements RowMapper<AccountResourceInfoValidator> {
    @Override
    public AccountResourceInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountResourceInfoValidator qo = new AccountResourceInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setResourceType(resultSet.getString("RESOURCE_TYPE"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setResourceTitle(resultSet.getString("RESOURCE_TITLE"));
        qo.setResourceAttchment(resultSet.getString("RESOURCE_ATTCHMENT"));
        qo.setResourceAttchmentSize(resultSet.getInt("RESOURCE_ATTCHMENT_SIZE"));
        qo.setResourceAttchmentType(resultSet.getString("RESOURCE_ATTCHMENT_TYPE"));
        qo.setResourceHeight(resultSet.getInt("RESOURCE_HEIGHT"));
        qo.setResourceWidth(resultSet.getInt("RESOURCE_WIDTH"));
        qo.setResourceStatus(resultSet.getString("RESOURCE_STATUS"));
        return qo;
    }
}
