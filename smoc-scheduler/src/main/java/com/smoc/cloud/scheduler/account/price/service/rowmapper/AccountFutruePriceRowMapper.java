package com.smoc.cloud.scheduler.account.price.service.rowmapper;

import com.smoc.cloud.scheduler.account.price.service.model.AccountFutruePriceModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 业务账号未来价格 批处理
 */
public class AccountFutruePriceRowMapper implements RowMapper<AccountFutruePriceModel> {

    @Override
    public AccountFutruePriceModel mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountFutruePriceModel qo = new AccountFutruePriceModel();
        //ID
        qo.setId(resultSet.getString("ID"));
        qo.setSourceId(resultSet.getString("DATA_ID"));
        //业务账号
        qo.setAccountId(resultSet.getString("BUSINESS_ID"));
        //运营商
        qo.setCarrier(resultSet.getString("PRICE_AREA"));
        //运营商 价格
        qo.setCarrierPrice(resultSet.getBigDecimal("CHANGE_PRICE"));
        //运营商类型
        String type = resultSet.getString("CARRIER_TYPE");
        if("INTL".equals(type)){
            qo.setCarrierType("2");
        }else{
            qo.setCarrierType("1");
        }

        //价格日期
        qo.setPriceDate(resultSet.getString("START_DATE"));

        return qo;
    }
}
