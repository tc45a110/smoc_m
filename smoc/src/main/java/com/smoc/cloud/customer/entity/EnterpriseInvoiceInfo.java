package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "enterprise_invoice_info")
public class EnterpriseInvoiceInfo {
    private String id;
    private String enterpriseId;
    private String invoiceType;
    private String invoiceTitle;
    private String taxPayerNumber;
    private String openBank;
    private String openAccount;
    private String registerAddress;
    private String invoiceMark;
    private String invoiceStatus;
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
    @Column(name = "ENTERPRISE_ID")
    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @Basic
    @Column(name = "INVOICE_TYPE")
    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    @Basic
    @Column(name = "INVOICE_TITLE")
    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    @Basic
    @Column(name = "TAX_PAYER_NUMBER")
    public String getTaxPayerNumber() {
        return taxPayerNumber;
    }

    public void setTaxPayerNumber(String taxPayerNumber) {
        this.taxPayerNumber = taxPayerNumber;
    }

    @Basic
    @Column(name = "OPEN_BANK")
    public String getOpenBank() {
        return openBank;
    }

    public void setOpenBank(String openBank) {
        this.openBank = openBank;
    }

    @Basic
    @Column(name = "OPEN_ACCOUNT")
    public String getOpenAccount() {
        return openAccount;
    }

    public void setOpenAccount(String openAccount) {
        this.openAccount = openAccount;
    }

    @Basic
    @Column(name = "REGISTER_ADDRESS")
    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    @Basic
    @Column(name = "INVOICE_MARK")
    public String getInvoiceMark() {
        return invoiceMark;
    }

    public void setInvoiceMark(String invoiceMark) {
        this.invoiceMark = invoiceMark;
    }

    @Basic
    @Column(name = "INVOICE_STATUS")
    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
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
        EnterpriseInvoiceInfo that = (EnterpriseInvoiceInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(enterpriseId, that.enterpriseId) &&
                Objects.equals(invoiceType, that.invoiceType) &&
                Objects.equals(invoiceTitle, that.invoiceTitle) &&
                Objects.equals(taxPayerNumber, that.taxPayerNumber) &&
                Objects.equals(openBank, that.openBank) &&
                Objects.equals(openAccount, that.openAccount) &&
                Objects.equals(registerAddress, that.registerAddress) &&
                Objects.equals(invoiceMark, that.invoiceMark) &&
                Objects.equals(invoiceStatus, that.invoiceStatus) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enterpriseId, invoiceType, invoiceTitle, taxPayerNumber, openBank, openAccount, registerAddress, invoiceMark, invoiceStatus, createdBy, createdTime, updatedBy, updatedTime);
    }
}
