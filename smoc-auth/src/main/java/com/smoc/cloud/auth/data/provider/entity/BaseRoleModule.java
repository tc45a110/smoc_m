package com.smoc.cloud.auth.data.provider.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 角色资源操作类
 * 2019/3/29 14:29
 **/
@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name = "base_role_module")
public class BaseRoleModule implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "MODULE_ID")
    private String moduleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    /**
     * @return MODULE_ID
     */
    public String getModuleId() {
        return moduleId;
    }

    /**
     * @param moduleId
     */
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
}
