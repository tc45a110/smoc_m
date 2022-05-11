package com.smoc.cloud.intelligence.rowmapper;

import com.smoc.cloud.common.smoc.intelligence.IntellectTemplateInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntellectTemplateInfoRwoMapper implements RowMapper<IntellectTemplateInfoValidator> {
    @Override
    public IntellectTemplateInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        IntellectTemplateInfoValidator qo = new IntellectTemplateInfoValidator();
        qo.setId(resultSet.getString("ID"));
        qo.setEnterpriseId(resultSet.getString("ENTERPRISE_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setAccountId(resultSet.getString("ACCOUNT_ID"));
        qo.setTemplateId(resultSet.getString("TEMPLATE_ID"));
        qo.setCardId(resultSet.getString("CARD_ID"));
        qo.setTplName(resultSet.getString("TPL_NAME"));
        qo.setScene(resultSet.getString("SCENE"));
        //qo.setPages(resultSet.getString("PAGES"));
        qo.setMessageType(resultSet.getString("MESSAGE_TYPE"));
        qo.setBizId(resultSet.getString("BIZ_ID"));
        qo.setBizFlag(resultSet.getString("BIZ_FLAG"));
        qo.setSmsExample(resultSet.getString("SMS_EXAMPLE"));
        qo.setTemplateCheckStatus(resultSet.getInt("TEMPLATE_CHECK_STATUS"));
        qo.setTemplateStatus(resultSet.getInt("TEMPLATE_STATUS"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        return qo;
    }
}
