package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.validator.EnterpriseSignCertifyValidator;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EnterpriseSignCertifyRowMapper implements RowMapper<EnterpriseSignCertifyValidator> {
    @Override
    public EnterpriseSignCertifyValidator mapRow(ResultSet rs, int rowNum) throws SQLException {
        EnterpriseSignCertifyValidator qo = new EnterpriseSignCertifyValidator();
        qo.setId(rs.getString("ID"));
        qo.setRegisterEnterpriseId(rs.getString("REGISTER_ENTERPRISE_ID"));
        qo.setRegisterEnterpriseName(rs.getString("REGISTER_ENTERPRISE_NAME"));
        qo.setSocialCreditCode(rs.getString("SOCIAL_CREDIT_CODE"));
        qo.setBusinessLicense(rs.getString("BUSINESS_LICENSE"));
        qo.setPersonLiableName(rs.getString("PERSON_LIABLE_NAME"));
        qo.setPersonLiableCertificateType(rs.getString("PERSON_LIABLE_CERTIFICATE_TYPE"));
        qo.setPersonLiableCertificateNumber(rs.getString("PERSON_LIABLE_CERTIFICATE_NUMBER"));
        qo.setPersonLiableCertificateUrl(rs.getString("PERSON_LIABLE_CERTIFICATE_URL"));
        qo.setPersonHandledName(rs.getString("PERSON_HANDLED_NAME"));
        qo.setPersonHandledCertificateType(rs.getString("PERSON_HANDLED_CERTIFICATE_TYPE"));
        qo.setPersonHandledCertificateNumber(rs.getString("PERSON_HANDLED_CERTIFICATE_NUMBER"));
        qo.setPersonHandledCertificateUrl(rs.getString("PERSON_HANDLED_CERTIFICATE_URL"));
        qo.setAuthorizeCertificate(rs.getString("AUTHORIZE_CERTIFICATE"));
        qo.setAuthorizeStartDate(rs.getString("AUTHORIZE_START_DATE"));
        qo.setAuthorizeExpireDate(rs.getString("AUTHORIZE_EXPIRE_DATE"));
        qo.setPosition(rs.getString("POSITION"));
        qo.setOfficePhotos(rs.getString("OFFICE_PHOTOS"));
        qo.setCreatedBy(rs.getString("CREATED_BY"));
        qo.setCreatedTime(rs.getString("CREATED_TIME"));
        return qo;
    }
}
