package com.smoc.cloud.finance.rowmapper;

import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRechargeValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FinanceAccountRechargeRowMapper implements RowMapper<FinanceAccountRechargeValidator> {
    @Override
    public FinanceAccountRechargeValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        FinanceAccountRechargeValidator qo = new FinanceAccountRechargeValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setRechargeFlowNo(resultSet.getString("RECHARGE_FLOW_NO"));
        qo.setRechargeSource(resultSet.getString("RECHARGE_SOURCE"));
        qo.setRechargeSum(resultSet.getBigDecimal("RECHARGE_SUM"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
