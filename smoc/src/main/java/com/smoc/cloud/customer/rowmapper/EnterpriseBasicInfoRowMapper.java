package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 * 2020/5/9 18:59
 **/
public class EnterpriseBasicInfoRowMapper implements RowMapper<EnterpriseBasicInfoValidator> {

    @Override
    public EnterpriseBasicInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        EnterpriseBasicInfoValidator qo = new EnterpriseBasicInfoValidator();
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setEnterpriseParentId(resultSet.getString("ENTERPRISE_PARENT_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setEnterpriseType(resultSet.getString("ENTERPRISE_TYPE"));
        qo.setAccessCorporation(resultSet.getString("ACCESS_CORPORATION"));
        qo.setEnterpriseContacts(resultSet.getString("ENTERPRISE_CONTACTS"));
        qo.setEnterpriseContactsPhone(resultSet.getString("ENTERPRISE_CONTACTS_PHONE"));
        qo.setSaler(resultSet.getString("SALER"));
        qo.setEnterpriseProcess(resultSet.getString("ENTERPRISE_PROCESS"));
        qo.setEnterpriseStatus(resultSet.getString("ENTERPRISE_STATUS"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));

        return qo;
    }
}
