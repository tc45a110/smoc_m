package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "enterprise_sign_certify")
public class EnterpriseSignCertify {
    private String id;
    private String registerEnterpriseId;
    private String registerEnterpriseName;
    private String socialCreditCode;
    private String businessLicense;
    private String personLiableName;
    private String personLiableCertificateType;
    private String personLiableCertificateNumber;
    private String personLiableCertificateUrl;
    private String personHandledName;
    private String personHandledCertificateType;
    private String personHandledCertificateNumber;
    private String personHandledCertificateUrl;
    private String authorizeCertificate;
    private String authorizeStartDate;
    private String authorizeExpireDate;
    private String position;
    private String officePhotos;
    private String certifyStatus;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "REGISTER_ENTERPRISE_ID")
    public String getRegisterEnterpriseId() {
        return registerEnterpriseId;
    }

    public void setRegisterEnterpriseId(String registerEnterpriseId) {
        this.registerEnterpriseId = registerEnterpriseId;
    }

    @Basic
    @Column(name = "REGISTER_ENTERPRISE_NAME")
    public String getRegisterEnterpriseName() {
        return registerEnterpriseName;
    }

    public void setRegisterEnterpriseName(String registerEnterpriseName) {
        this.registerEnterpriseName = registerEnterpriseName;
    }

    @Basic
    @Column(name = "SOCIAL_CREDIT_CODE")
    public String getSocialCreditCode() {
        return socialCreditCode;
    }

    public void setSocialCreditCode(String socialCreditCode) {
        this.socialCreditCode = socialCreditCode;
    }

    @Basic
    @Column(name = "BUSINESS_LICENSE")
    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    @Basic
    @Column(name = "PERSON_LIABLE_NAME")
    public String getPersonLiableName() {
        return personLiableName;
    }

    public void setPersonLiableName(String personLiableName) {
        this.personLiableName = personLiableName;
    }

    @Basic
    @Column(name = "PERSON_LIABLE_CERTIFICATE_TYPE")
    public String getPersonLiableCertificateType() {
        return personLiableCertificateType;
    }

    public void setPersonLiableCertificateType(String personLiableCertificateType) {
        this.personLiableCertificateType = personLiableCertificateType;
    }

    @Basic
    @Column(name = "PERSON_LIABLE_CERTIFICATE_NUMBER")
    public String getPersonLiableCertificateNumber() {
        return personLiableCertificateNumber;
    }

    public void setPersonLiableCertificateNumber(String personLiableCertificateNumber) {
        this.personLiableCertificateNumber = personLiableCertificateNumber;
    }

    @Basic
    @Column(name = "PERSON_LIABLE_CERTIFICATE_URL")
    public String getPersonLiableCertificateUrl() {
        return personLiableCertificateUrl;
    }

    public void setPersonLiableCertificateUrl(String personLiableCertificateUrl) {
        this.personLiableCertificateUrl = personLiableCertificateUrl;
    }

    @Basic
    @Column(name = "PERSON_HANDLED_NAME")
    public String getPersonHandledName() {
        return personHandledName;
    }

    public void setPersonHandledName(String personHandledName) {
        this.personHandledName = personHandledName;
    }

    @Basic
    @Column(name = "PERSON_HANDLED_CERTIFICATE_TYPE")
    public String getPersonHandledCertificateType() {
        return personHandledCertificateType;
    }

    public void setPersonHandledCertificateType(String personHandledCertificateType) {
        this.personHandledCertificateType = personHandledCertificateType;
    }

    @Basic
    @Column(name = "PERSON_HANDLED_CERTIFICATE_NUMBER")
    public String getPersonHandledCertificateNumber() {
        return personHandledCertificateNumber;
    }

    public void setPersonHandledCertificateNumber(String personHandledCertificateNumber) {
        this.personHandledCertificateNumber = personHandledCertificateNumber;
    }

    @Basic
    @Column(name = "PERSON_HANDLED_CERTIFICATE_URL")
    public String getPersonHandledCertificateUrl() {
        return personHandledCertificateUrl;
    }

    public void setPersonHandledCertificateUrl(String personHandledCertificateUrl) {
        this.personHandledCertificateUrl = personHandledCertificateUrl;
    }

    @Basic
    @Column(name = "AUTHORIZE_CERTIFICATE")
    public String getAuthorizeCertificate() {
        return authorizeCertificate;
    }

    public void setAuthorizeCertificate(String authorizeCertificate) {
        this.authorizeCertificate = authorizeCertificate;
    }

    @Basic
    @Column(name = "AUTHORIZE_START_DATE")
    public String getAuthorizeStartDate() {
        return authorizeStartDate;
    }

    public void setAuthorizeStartDate(String authorizeStartDate) {
        this.authorizeStartDate = authorizeStartDate;
    }

    @Basic
    @Column(name = "AUTHORIZE_EXPIRE_DATE")
    public String getAuthorizeExpireDate() {
        return authorizeExpireDate;
    }

    public void setAuthorizeExpireDate(String authorizeExpireDate) {
        this.authorizeExpireDate = authorizeExpireDate;
    }

    @Basic
    @Column(name = "POSITION")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Basic
    @Column(name = "OFFICE_PHOTOS")
    public String getOfficePhotos() {
        return officePhotos;
    }

    public void setOfficePhotos(String officePhotos) {
        this.officePhotos = officePhotos;
    }

    @Basic
    @Column(name = "CERTIFY_STATUS")
    public String getCertifyStatus() {
        return certifyStatus;
    }

    public void setCertifyStatus(String certifyStatus) {
        this.certifyStatus = certifyStatus;
    }

    @Basic
    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "CREATED_TIME")
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Basic
    @Column(name = "UPDATED_BY")
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Basic
    @Column(name = "UPDATED_TIME")
    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnterpriseSignCertify that = (EnterpriseSignCertify) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(registerEnterpriseId, that.registerEnterpriseId) &&
                Objects.equals(registerEnterpriseName, that.registerEnterpriseName) &&
                Objects.equals(socialCreditCode, that.socialCreditCode) &&
                Objects.equals(businessLicense, that.businessLicense) &&
                Objects.equals(personLiableName, that.personLiableName) &&
                Objects.equals(personLiableCertificateType, that.personLiableCertificateType) &&
                Objects.equals(personLiableCertificateNumber, that.personLiableCertificateNumber) &&
                Objects.equals(personLiableCertificateUrl, that.personLiableCertificateUrl) &&
                Objects.equals(personHandledName, that.personHandledName) &&
                Objects.equals(personHandledCertificateType, that.personHandledCertificateType) &&
                Objects.equals(personHandledCertificateNumber, that.personHandledCertificateNumber) &&
                Objects.equals(personHandledCertificateUrl, that.personHandledCertificateUrl) &&
                Objects.equals(authorizeCertificate, that.authorizeCertificate) &&
                Objects.equals(authorizeStartDate, that.authorizeStartDate) &&
                Objects.equals(authorizeExpireDate, that.authorizeExpireDate) &&
                Objects.equals(position, that.position) &&
                Objects.equals(officePhotos, that.officePhotos) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, registerEnterpriseId, registerEnterpriseName, socialCreditCode, businessLicense, personLiableName, personLiableCertificateType, personLiableCertificateNumber, personLiableCertificateUrl, personHandledName, personHandledCertificateType, personHandledCertificateNumber, personHandledCertificateUrl, authorizeCertificate, authorizeStartDate, authorizeExpireDate, position, officePhotos, createdBy, createdTime, updatedBy, updatedTime);
    }
}
