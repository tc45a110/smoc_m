package com.smoc.cloud.finance.rowmapper;

import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountShareDetailValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FinanceAccountShareDetailRowMapper implements RowMapper<FinanceAccountShareDetailValidator> {
    @Override
    public FinanceAccountShareDetailValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        FinanceAccountShareDetailValidator qo = new FinanceAccountShareDetailValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setShareAccountId(resultSet.getString("SHARE_ACCOUNT_ID"));
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setIsFreezeSumPool(resultSet.getString("IS_FREEZE_SUM_POOL"));
        qo.setIsUsableSumPool(resultSet.getString("IS_USABLE_SUM_POOL"));
        qo.setUsableSumPool(resultSet.getBigDecimal("USABLE_SUM_POOL"));
        qo.setFreezeSumPool(resultSet.getBigDecimal("FREEZE_SUM_POOL"));
        qo.setShareStatus(resultSet.getString("SHARE_STATUS"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));

        return qo;
    }
}
