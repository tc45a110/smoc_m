package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.EnterpriseContractInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 * 2020/5/9 18:59
 **/
public class EnterpriseContractInfoRowMapper implements RowMapper<EnterpriseContractInfoValidator> {

    @Override
    public EnterpriseContractInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        EnterpriseContractInfoValidator qo = new EnterpriseContractInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setEnterpriseType(resultSet.getString("ENTERPRISE_TYPE"));
        qo.setContractNo(resultSet.getString("CONTRACT_NO"));
        qo.setContractKey(resultSet.getString("CONTRACT_KEY"));
        qo.setContractDate(resultSet.getString("CONTRACT_DATE"));
        qo.setContractExpireDate(resultSet.getString("CONTRACT_EXPIRE_DATE"));
        qo.setContractStatus(resultSet.getString("CONTRACT_STATUS"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));

        return qo;
    }
}
