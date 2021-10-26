package com.smoc.cloud.auth.data.provider.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;

/**
 * 2019/5/6 12:43
 **/
@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name = "base_user_extends", schema = "oauth2", catalog = "")
public class BaseUserExtends {
    private String id;
    private String realName;
    private String corporation;
    private String department;
    private Integer administrator;
    private Integer teamLeader;
    private String code;
    private String parentCode;
    private Integer type;
    private String header;
    private String email;
    private String webChat;
    private String qq;
    private String position;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "REAL_NAME")
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Basic
    @Column(name = "CORPORATION")
    public String getCorporation() {
        return corporation;
    }

    public void setCorporation(String corporation) {
        this.corporation = corporation;
    }

    @Basic
    @Column(name = "DEPARTMENT")
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Basic
    @Column(name = "ADMINISTRATOR")
    public Integer getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Integer administrator) {
        this.administrator = administrator;
    }

    @Basic
    @Column(name = "TEAM_LEADER")
    public Integer getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(Integer teamLeader) {
        this.teamLeader = teamLeader;
    }

    @Basic
    @Column(name = "CODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "PARENT_CODE")
    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Basic
    @Column(name = "TYPE")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Basic
    @Column(name = "HEADER")
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Basic
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "WEB_CHAT")
    public String getWebChat() {
        return webChat;
    }

    public void setWebChat(String webChat) {
        this.webChat = webChat;
    }

    @Basic
    @Column(name = "QQ")
    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    @Basic
    @Column(name = "POSITION")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseUserExtends that = (BaseUserExtends) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(realName, that.realName) &&
                Objects.equals(corporation, that.corporation) &&
                Objects.equals(department, that.department) &&
                Objects.equals(administrator, that.administrator) &&
                Objects.equals(teamLeader, that.teamLeader) &&
                Objects.equals(code, that.code) &&
                Objects.equals(parentCode, that.parentCode) &&
                Objects.equals(type, that.type) &&
                Objects.equals(header, that.header) &&
                Objects.equals(email, that.email) &&
                Objects.equals(webChat, that.webChat) &&
                Objects.equals(qq, that.qq) &&
                Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, realName, corporation, department, administrator, teamLeader, code, parentCode, type, header, email, webChat, qq, position);
    }
}
