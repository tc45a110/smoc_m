package com.smoc.cloud.scheduler.message.service.rowmapper;

import com.smoc.cloud.scheduler.message.service.model.MessageTimingModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 定时任务 批处理
 */
public class MessageTimingRowMapper implements RowMapper<MessageTimingModel> {

    @Override
    public MessageTimingModel mapRow(ResultSet resultSet, int i) throws SQLException {

        MessageTimingModel qo = new MessageTimingModel();
        qo.setId(resultSet.getString("ID"));
        qo.setUpType(resultSet.getString("UP_TYPE"));
        qo.setInputMobile(resultSet.getString("INPUT_NUMBER"));
        qo.setMobileFile(resultSet.getString("NUMBER_FILES"));
        qo.setGroupId(resultSet.getString("GROUP_ID"));
        qo.setTemplateId(resultSet.getString("TEMPLATE_ID"));
        qo.setAccountId(resultSet.getString("BUSINESS_ACCOUNT"));
        qo.setMessageContent(resultSet.getString("MESSAGE_CONTENT"));
        qo.setMessageType(resultSet.getString("MESSAGE_TYPE"));
        qo.setBusinessType(resultSet.getString("BUSINESS_TYPE"));

        return qo;
    }
}
