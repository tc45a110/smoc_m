package com.smoc.cloud.finance.rowmapper;

import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FinanceAccountRowMapper implements RowMapper<FinanceAccountValidator> {
    @Override
    public FinanceAccountValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        FinanceAccountValidator qo = new FinanceAccountValidator();
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setBusinessAccount("ACCOUNT");
        qo.setAccountType(resultSet.getString("ACCOUNT_TYPE"));
        qo.setAccountTotalSum(resultSet.getBigDecimal("ACCOUNT_TOTAL_SUM"));
        qo.setAccountUsableSum(resultSet.getBigDecimal("ACCOUNT_USABLE_SUM"));
        qo.setAccountFrozenSum(resultSet.getBigDecimal("ACCOUNT_FROZEN_SUM"));
        qo.setAccountConsumeSum(resultSet.getBigDecimal("ACCOUNT_CONSUME_SUM"));
        qo.setAccountRechargeSum(resultSet.getBigDecimal("ACCOUNT_RECHARGE_SUM"));
        qo.setAccountCreditSum(resultSet.getBigDecimal("ACCOUNT_CREDIT_SUM"));
        qo.setAccountStatus(resultSet.getString("ACCOUNT_STATUS"));
        qo.setIsShare(resultSet.getString("IS_SHARE"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
