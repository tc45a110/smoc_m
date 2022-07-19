package com.smoc.cloud.saler.rowmapper;

import com.smoc.cloud.common.smoc.saler.qo.ChannelStatisticSendData;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 **/
public class ChannelStatisticSendRowMapper implements RowMapper<ChannelStatisticSendData> {

    @Override
    public ChannelStatisticSendData mapRow(ResultSet resultSet, int i) throws SQLException {

        ChannelStatisticSendData qo = new ChannelStatisticSendData();
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
