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
 * 系统操作类
 * 2019/3/29 14:29
 **/
@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name = "base_system")
public class BaseSystem implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "SYSTEM_NAME")
    private String systemName;

    @Column(name = "PROJECT_NAME")
    private String projectName;

    @Column(name = "URL")
    private String url;

    @Column(name = "ICON")
    private String icon;

    @Column(name = "STYLE_CLASS")
    private String styleClass;

    /**
     * 0 禁用、1 启用
     */
    @Column(name = "ACTIVE")
    private Integer active;

    @Column(name = "SORT")
    private Integer sort;

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
     * @return SYSTEM_NAME
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * @param systemName
     */
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    /**
     * @return PROJECT_NAME
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * 获取0 禁用、1 启用
     *
     * @return ACTIVE - 0 禁用、1 启用
     */
    public Integer getActive() {
        return active;
    }

    /**
     * 设置0 禁用、1 启用
     *
     * @param active 0 禁用、1 启用
     */
    public void setActive(Integer active) {
        this.active = active;
    }

    /**
     * @return SORT
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort
     */
    public void setSort(Integer sort) {
        this.sort = sort;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
}
