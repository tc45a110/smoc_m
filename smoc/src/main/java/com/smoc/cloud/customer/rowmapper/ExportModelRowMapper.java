package com.smoc.cloud.customer.rowmapper;

import com.smoc.cloud.common.smoc.customer.qo.ExportModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExportModelRowMapper implements RowMapper<ExportModel> {
    @Override
    public ExportModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExportModel qo = new ExportModel();
        qo.setId(rs.getString("ID"));
        qo.setCertifyId(rs.getString("CERTIFY_ID"));
        qo.setRegCodeNum(rs.getString("REGISTER_CODE_NUMBER"));
        qo.setAccessProvince(rs.getString("ACCESS_PROVINCE"));
        qo.setAccessCity(rs.getString("ACCESS_CITY"));
        qo.setNumberSegment(rs.getString("NUMBER_SEGMENT"));
        qo.setRegisterSign(rs.getString("REGISTER_SIGN"));
        qo.setRegisterCarrier(rs.getString("REGISTER_CARRIER"));
        qo.setAppName(rs.getString("APP_NAME"));
        qo.setServiceType(rs.getString("SERVICE_TYPE"));
        qo.setMainApplication(rs.getString("MAIN_APPLICATION"));
        qo.setRegisterEnterprise(rs.getString("REGISTER_ENTERPRISE_ID"));
        qo.setEnterpriseName(rs.getString("REGISTER_ENTERPRISE_NAME"));
        qo.setSocialCreditCode(rs.getString("SOCIAL_CREDIT_CODE"));
        qo.setBusinessLicense(rs.getString("BUSINESS_LICENSE"));
        qo.setLiableName(rs.getString("PERSON_LIABLE_NAME"));
        qo.setLiableCertType(rs.getString("PERSON_LIABLE_CERTIFICATE_TYPE"));
        qo.setLiableCertNum(rs.getString("PERSON_LIABLE_CERTIFICATE_NUMBER"));
        qo.setLiableCertUrl(rs.getString("PERSON_LIABLE_CERTIFICATE_URL"));
        qo.setHandledName(rs.getString("PERSON_HANDLED_NAME"));
        qo.setHandledCertType(rs.getString("PERSON_HANDLED_CERTIFICATE_TYPE"));
        qo.setHandledCertNum(rs.getString("PERSON_HANDLED_CERTIFICATE_NUMBER"));
        qo.setHandledCertUrl(rs.getString("PERSON_HANDLED_CERTIFICATE_URL"));
        qo.setAuthorizeCert(rs.getString("AUTHORIZE_CERTIFICATE"));
        qo.setAuthorizeStart(rs.getString("AUTHORIZE_START_DATE"));
        qo.setAuthorizeEnd(rs.getString("AUTHORIZE_EXPIRE_DATE"));
        qo.setPosition(rs.getString("POSITION"));
        qo.setOfficePhotos(rs.getString("OFFICE_PHOTOS"));
        return qo;
    }
}
