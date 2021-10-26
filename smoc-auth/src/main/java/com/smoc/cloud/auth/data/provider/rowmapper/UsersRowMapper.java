package com.smoc.cloud.auth.data.provider.rowmapper;

import com.smoc.cloud.common.auth.qo.Users;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用户查询映射对象
 * 2019/5/6 12:57
 **/
public class UsersRowMapper implements RowMapper<Users> {

    @Override
    public Users mapRow(ResultSet resultSet, int i) throws SQLException {
        Users user = new Users();
        user.setId(resultSet.getString("ID"));
        user.setUserName(resultSet.getString("USER_NAME"));
        user.setPhone(resultSet.getString("PHONE"));
        user.setActive(resultSet.getInt("ACTIVE"));
        user.setRealName(resultSet.getString("REAL_NAME"));
        user.setCorporation(resultSet.getString("CORPORATION"));
        user.setCode(resultSet.getString("CODE"));
        user.setType(resultSet.getInt("TYPE"));
        return user;
    }
}
