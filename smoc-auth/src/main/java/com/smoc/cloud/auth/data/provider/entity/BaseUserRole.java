package com.smoc.cloud.auth.data.provider.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 系统-角色操作类
 * 2019/3/29 14:29
 **/
@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name = "base_user_role")
public class BaseUserRole implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "ROLE_ID")
    private String roleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return USER_ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return ROLE_ID
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
