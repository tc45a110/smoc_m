package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.BaseUserRole;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 处理用户角色关系
 * 2019/5/6 18:17
 **/
public class BaseUserRoleRepositoryImpl {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public void batchSave(List<BaseUserRole> baseUserRoles) {
        final String sql = "insert into base_user_role(ID,ROLE_ID,USER_ID) values(?,?,?) ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return baseUserRoles.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                BaseUserRole roleModule = baseUserRoles.get(i);
                ps.setString(1, roleModule.getId());
                ps.setString(2, roleModule.getRoleId());
                ps.setString(3, roleModule.getUserId());

            }

        });
    }

    public void deleteByUserId(String userId) {
        String sql = "delete from base_user_role where USER_ID = ?";

        Object[] params = new Object[1];
        params[0] = userId;
        jdbcTemplate.update(sql, params);

    }
}
