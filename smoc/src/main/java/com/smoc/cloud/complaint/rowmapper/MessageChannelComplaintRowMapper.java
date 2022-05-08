package com.smoc.cloud.complaint.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageChannelComplaintValidator;
import com.smoc.cloud.common.smoc.message.MessageComplaintInfoValidator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 复杂查询对象封装
 * 2020/5/9 18:59
 **/
public class MessageChannelComplaintRowMapper implements RowMapper<MessageChannelComplaintValidator> {

    @Override
    public MessageChannelComplaintValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageChannelComplaintValidator qo = new MessageChannelComplaintValidator();

        qo.setCarrier(resultSet.getString("CARRIER"));
        qo.setChannelId(resultSet.getString("CHANNEL_ID"));
        qo.setChannelName(resultSet.getString("CHANNEL_NAME"));
        qo.setComplaintNum(resultSet.getString("COMPLAINT_NUM"));
        qo.setMessageNum(resultSet.getString("MESSAGE_SUCCESS_NUM"));
        BigDecimal complaintRate = resultSet.getBigDecimal("COMPLAINT_RATE");
        if(!StringUtils.isEmpty(complaintRate)){
            qo.setComplaintRate(complaintRate.stripTrailingZeros().toPlainString());
        }else{
            qo.setComplaintRate("");
        }
        BigDecimal maxComplaintRate = resultSet.getBigDecimal("MAX_COMPLAINT_RATE");
        if(!StringUtils.isEmpty(maxComplaintRate)){
            qo.setMaxComplaintRate(maxComplaintRate.stripTrailingZeros().toPlainString());
        }else{
            qo.setMaxComplaintRate("");
        }

        return qo;
    }
}
