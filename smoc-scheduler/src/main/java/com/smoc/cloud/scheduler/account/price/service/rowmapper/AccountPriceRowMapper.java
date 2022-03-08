package com.smoc.cloud.scheduler.account.price.service.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 业务账号价格历史 批处理
 */
public class AccountPriceRowMapper implements RowMapper<AccountFinanceInfoValidator> {

    @Override
    public AccountFinanceInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountFinanceInfoValidator qo = new AccountFinanceInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        //间隔天数
        qo.setCreatedBy(resultSet.getInt("DAYS")+"");
        //当前日期
        qo.setUpdatedBy(resultSet.getString("TODAY"));

        return qo;
    }
}
