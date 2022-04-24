package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class AccountStatisticSendRowMapper implements RowMapper<AccountStatisticSendData> {

    @Override
    public AccountStatisticSendData mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountStatisticSendData qo = new AccountStatisticSendData();
        qo.setMonth(resultSet.getString("MONTH_DAY"));
        BigDecimal sendNumber = resultSet.getBigDecimal("SEND_NUMBER");
        if(!StringUtils.isEmpty(sendNumber)){
            qo.setSendNumber(new BigDecimal(sendNumber.stripTrailingZeros().toPlainString()));
        }else{
            qo.setSendNumber(new BigDecimal(0));
        }

        return qo;
    }
}
