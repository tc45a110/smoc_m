package com.smoc.cloud.template.rowmapper;

import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountTemplateInfoRowMapper  implements RowMapper<AccountTemplateInfoValidator> {
    @Override
    public AccountTemplateInfoValidator mapRow(ResultSet resultSet, int i) throws SQLException {

        AccountTemplateInfoValidator qo = new AccountTemplateInfoValidator();
        qo.setTemplateId(resultSet.getString("TEMPLATE_ID"));
        qo.setEnterpriseName(resultSet.getString("ENTERPRISE_NAME"));
        qo.setBusinessAccount(resultSet.getString("BUSINESS_ACCOUNT"));
        qo.setAccountName(resultSet.getString("ACCOUNT_NAME"));
        qo.setTemplateType(resultSet.getString("BUSINESS_TYPE"));
        qo.setSignName(resultSet.getString("SIGN_NAME"));
        qo.setTemplateContent(resultSet.getString("TEMPLATE_CONTENT"));
        qo.setForbiddenContent(resultSet.getString("FORBIDDEN_CONTENT"));
        qo.setCheckDate(resultSet.getString("CHECK_DATE"));
        qo.setCheckBy(resultSet.getString("CHECK_BY"));
        qo.setCheckOpinions(resultSet.getString("CHECK_OPINIONS"));
        qo.setCheckStatus(resultSet.getString("CHECK_STATUS"));
        qo.setTemplateStatus(resultSet.getString("TEMPLATE_STATUS"));
        qo.setTemplateAgreementType(resultSet.getString("TEMPLATE_AGREEMENT_TYPE"));
        qo.setCreatedBy(resultSet.getString("CREATED_BY"));
        qo.setCreatedTime(resultSet.getString("CREATED_TIME"));
        qo.setTemplateFlag(resultSet.getString("TEMPLATE_FLAG"));
        qo.setInfoType(resultSet.getString("INFO_TYPE"));
        qo.setTemplateTitle(resultSet.getString("TEMPLATE_TITLE"));
        qo.setTemplateClassify(resultSet.getString("TEMPLATE_CLASSIFY"));

        return qo;
    }
}
