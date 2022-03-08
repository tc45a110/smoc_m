package com.smoc.cloud.scheduler.account.price.service.rowmapper;

import com.smoc.cloud.scheduler.account.price.service.model.AccountPriceModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 业务账号价格历史 批处理
 */
public class AccountPriceRowMapper implements RowMapper<AccountPriceModel> {

    @Override
    public AccountPriceModel mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountPriceModel qo = new AccountPriceModel();
        qo.setId(resultSet.getString("ID"));
        //业务账号
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        //运营商
        qo.setCarrier(resultSet.getString("CARRIER"));
        //运营商 价格
        qo.setCarrierPrice(resultSet.getBigDecimal("CARRIER_PRICE"));
        //价格日期
        qo.setPriceData(resultSet.getString("PRICE_DATE"));
        //上次更新时间
        qo.setBatchDate(resultSet.getString("BATCH_DATE"));
        //间隔天数
        qo.setDays(resultSet.getInt("DAYS"));

        qo.setCreateTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
