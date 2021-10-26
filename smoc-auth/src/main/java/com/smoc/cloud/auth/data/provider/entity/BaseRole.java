package com.smoc.cloud.auth.data.provider.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统角色操作类
 * 2019/3/29 14:29
 **/
@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name = "base_role")
public class BaseRole implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "ROLE_CODE")
    private String roleCode;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @Column(name = "ORGANIZATION")
    private String organization;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return ROLE_CODE
     */
    public String getRoleCode() {
        return roleCode;
    }

    /**
     * @param roleCode
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    /**
     * @return ROLE_NAME
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganization() {
        return organization;
    }

    /**
     * @return CREATE_DATE
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return UPDATE_DATE
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
