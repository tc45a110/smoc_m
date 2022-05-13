package com.smoc.cloud.saler.rowmapper;

import com.smoc.cloud.common.smoc.saler.qo.CustomerAccountInfoQo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class CustomerAccountInfoRowMapper implements RowMapper<CustomerAccountInfoQo> {

    @Override
    public CustomerAccountInfoQo mapRow(ResultSet resultSet, int i) throws SQLException {

        CustomerAccountInfoQo qo = new CustomerAccountInfoQo();
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setIndustryType(resultSet.getString("INDUSTRY_TYPE"));
        qo.setPayType(resultSet.getString("PAY_TYPE"));
        qo.setEnterpriseType(resultSet.getString("ENTERPRISE_TYPE"));
        qo.setEnterpriseContacts(resultSet.getString("ENTERPRISE_CONTACTS"));
        qo.setEnterpriseContactsPhone(resultSet.getString("ENTERPRISE_CONTACTS_PHONE"));

        return qo;
    }
}
