package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class MessageAccountRowMapper implements RowMapper<MessageAccountValidator> {

    @Override
    public MessageAccountValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageAccountValidator qo = new MessageAccountValidator();
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));
        BigDecimal accountUsableSum = resultSet.getBigDecimal("ACCOUNT_USABLE_SUM");
        if(!StringUtils.isEmpty(accountUsableSum)){
            qo.setAccountUsableSum(new BigDecimal(accountUsableSum.stripTrailingZeros().toPlainString()));
        }else{
            qo.setAccountUsableSum(accountUsableSum);
        }
        return qo;
    }
}
