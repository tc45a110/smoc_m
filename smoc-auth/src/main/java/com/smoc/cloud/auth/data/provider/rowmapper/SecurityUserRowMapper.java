package com.smoc.cloud.auth.data.provider.rowmapper;

import com.smoc.cloud.common.auth.entity.SecurityUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 系统用户查询映射对象
 * 2019/5/6 12:57
 **/
public class SecurityUserRowMapper implements RowMapper<SecurityUser> {

    @Override
    public SecurityUser mapRow(ResultSet resultSet, int i) throws SQLException {
        SecurityUser user = new SecurityUser();
        user.setId(resultSet.getString("ID"));
        user.setUserName(resultSet.getString("USER_NAME"));
        user.setRealName(resultSet.getString("REAL_NAME"));
        user.setPassword(resultSet.getString("PASSWORD"));
        user.setPhone(resultSet.getString("PHONE"));
        user.setOrganization(resultSet.getString("ORGANIZATION"));
        user.setCorporation(resultSet.getString("CORPORATION"));
        user.setDepartment(resultSet.getString("DEPARTMENT"));
        user.setParentCode(resultSet.getString("PARENT_CODE"));
        user.setCode(resultSet.getString("CODE"));
        user.setAdministrator(resultSet.getInt("ADMINISTRATOR"));
        user.setTeamLeader(resultSet.getInt("TEAM_LEADER"));
        user.setType(resultSet.getInt("TYPE"));
        user.setAuthScope(resultSet.getString("WEB_CHAT"));
        return user;
    }
}
