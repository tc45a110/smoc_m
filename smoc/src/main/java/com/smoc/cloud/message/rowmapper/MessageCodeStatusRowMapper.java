package com.smoc.cloud.message.rowmapper;

import com.smoc.cloud.common.smoc.message.MessageCodeValidator;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageCodeStatusRowMapper implements RowMapper<MessageCodeValidator> {
    @Override
    public MessageCodeValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageCodeValidator qo = new MessageCodeValidator();
        qo.setBusinessAccount(resultSet.getString("ACCOUNT_ID"));
        String codeStatus = resultSet.getString("STATUS_CODE");
        if(!StringUtils.isEmpty(codeStatus)){
            qo.setCodeStatus(resultSet.getString("STATUS_CODE"));
        }else{
            qo.setCodeStatus("无状态码");
        }
        qo.setCodeNumber(resultSet.getInt("CODE_NUM"));

        return qo;
    }
}
