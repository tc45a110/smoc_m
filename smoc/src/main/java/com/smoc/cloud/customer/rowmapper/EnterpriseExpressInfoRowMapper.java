package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.EnterpriseExpressInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 * 2020/5/9 18:59
 **/
public class EnterpriseExpressInfoRowMapper implements RowMapper<EnterpriseExpressInfoValidator> {

    @Override
    public EnterpriseExpressInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        EnterpriseExpressInfoValidator qo = new EnterpriseExpressInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setPostRemark(resultSet.getString("POST_REMARK"));
        qo.setPostContacts(resultSet.getString("POST_CONTACTS"));
        qo.setPostPhone(resultSet.getString("POST_PHONE"));
        qo.setPostAddress(resultSet.getString("POST_ADDRESS"));
        qo.setPostStatus(resultSet.getString("POST_STATUS"));

        return qo;
    }
}
