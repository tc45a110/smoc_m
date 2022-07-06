package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.model.MessageTaskDetail;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageTaskDetailRowMapper implements RowMapper<MessageTaskDetail> {
    @Override
    public MessageTaskDetail mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageTaskDetail qo = new MessageTaskDetail();
        qo.setMobile(resultSet.getString("PHONE_NUMBER"));
        qo.setMessageContent(resultSet.getString("MESSAGE_CONTENT"));
        qo.setChargeNumber(resultSet.getInt("CHARGE_NUMBER"));
        qo.setArea(resultSet.getString("AREA_NAME"));
        qo.setSendTime(resultSet.getString("REPORT_TIME"));
        qo.setCustomerStatus(resultSet.getString("STATUS_CODE"));

        String carrier = resultSet.getString("CARRIER");
        if("CMCC".equals(carrier)){
            qo.setCarrier("移动");
        }
        if("UNIC".equals(carrier)){
            qo.setCarrier("联通");
        }
        if("TELC".equals(carrier)){
            qo.setCarrier("电信");
        }
        if("INTL".equals(carrier)){
            qo.setCarrier("国际");
        }

        return qo;
    }
}
