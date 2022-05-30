package com.smoc.cloud.template.rowmapper;

import com.smoc.cloud.template.entity.AccountTemplateContent;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountTemplateContentRowMapper implements RowMapper<AccountTemplateContent> {
    @Override
    public AccountTemplateContent mapRow(ResultSet resultSet, int i) throws SQLException {
        AccountTemplateContent qo = new AccountTemplateContent();
        qo.setAccount(resultSet.getString("BUSINESS_ACCOUNT"));
        qo.setContent(resultSet.getString("TEMPLATE_CONTENT"));
        qo.setIsFilter(resultSet.getString("IS_FILTER"));
        qo.setTemplateId(resultSet.getString("TEMPLATE_ID"));
        return qo;
    }
}
