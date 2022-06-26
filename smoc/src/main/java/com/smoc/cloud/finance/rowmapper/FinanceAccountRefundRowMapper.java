package com.smoc.cloud.finance.rowmapper;

import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountRefundValidator;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FinanceAccountRefundRowMapper implements RowMapper<FinanceAccountRefundValidator> {
    @Override
    public FinanceAccountRefundValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        FinanceAccountRefundValidator qo = new FinanceAccountRefundValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setRefundFlowNo(resultSet.getString("REFUND_FLOW_NO"));
        qo.setRefundSource(resultSet.getString("REFUND_SOURCE"));
        qo.setRefundSum(new BigDecimal(resultSet.getBigDecimal("REFUND_SUM").stripTrailingZeros().toPlainString()));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        qo.setRemark(resultSet.getString("REMARK"));
        return qo;
    }
}
