package com.smoc.cloud.customer.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "system_attachment_info", schema = "smoc", catalog = "")
public class SystemAttachmentInfo {
    private String id;
    private String moudleId;
    private String moudleInentification;
    private String attachmentName;
    private String attachmentUri;
    private String docType;
    private BigDecimal docSize;
    private String attachmentStatus;
    private String createdBy;
    private Date createdTime;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "MOUDLE_ID")
    public String getMoudleId() {
        return moudleId;
    }

    public void setMoudleId(String moudleId) {
        this.moudleId = moudleId;
    }

    @Basic
    @Column(name = "MOUDLE_INENTIFICATION")
    public String getMoudleInentification() {
        return moudleInentification;
    }

    public void setMoudleInentification(String moudleInentification) {
        this.moudleInentification = moudleInentification;
    }

    @Basic
    @Column(name = "ATTACHMENT_NAME")
    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    @Basic
    @Column(name = "ATTACHMENT_URI")
    public String getAttachmentUri() {
        return attachmentUri;
    }

    public void setAttachmentUri(String attachmentUri) {
        this.attachmentUri = attachmentUri;
    }

    @Basic
    @Column(name = "DOC_TYPE")
    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    @Basic
    @Column(name = "DOC_SIZE")
    public BigDecimal getDocSize() {
        return docSize;
    }

    public void setDocSize(BigDecimal docSize) {
        this.docSize = docSize;
    }

    @Basic
    @Column(name = "ATTACHMENT_STATUS")
    public String getAttachmentStatus() {
        return attachmentStatus;
    }

    public void setAttachmentStatus(String attachmentStatus) {
        this.attachmentStatus = attachmentStatus;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemAttachmentInfo that = (SystemAttachmentInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(moudleId, that.moudleId) &&
                Objects.equals(moudleInentification, that.moudleInentification) &&
                Objects.equals(attachmentName, that.attachmentName) &&
                Objects.equals(attachmentUri, that.attachmentUri) &&
                Objects.equals(docType, that.docType) &&
                Objects.equals(docSize, that.docSize) &&
                Objects.equals(attachmentStatus, that.attachmentStatus) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, moudleId, moudleInentification, attachmentName, attachmentUri, docType, docSize, attachmentStatus, createdBy, createdTime);
    }
}
