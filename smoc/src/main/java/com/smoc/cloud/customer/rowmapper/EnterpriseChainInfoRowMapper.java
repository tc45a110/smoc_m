package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.EnterpriseChainInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 * 2020/5/9 18:59
 **/
public class EnterpriseChainInfoRowMapper implements RowMapper<EnterpriseChainInfoValidator> {

    @Override
    public EnterpriseChainInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        EnterpriseChainInfoValidator qo = new EnterpriseChainInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setDocumentId(resultSet.getString("DOCUMENT_ID"));
        qo.setSignChain(resultSet.getString("SIGN_CHAIN"));
        qo.setSignDate(resultSet.getString("SIGN_DATE"));
        qo.setSignExpireDate(resultSet.getString("SIGN_EXPIRE_DATE"));
        qo.setSignChainStatus(resultSet.getString("SIGN_CHAIN_STATUS"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setSort(resultSet.getInt("SORT"));

        return qo;
    }
}
