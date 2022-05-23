package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.BaseRole;
import com.smoc.cloud.auth.data.provider.entity.BaseUserRole;
import com.smoc.cloud.common.smoc.customer.qo.ServiceAuthInfo;
import com.smoc.cloud.common.utils.UUID;
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

    public void batchDeleteByUserIdAndRoleId(String uId, List<BaseRole> list) {
        final String sql = "delete from base_user_role where USER_ID = ? and ROLE_ID = ? ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return list.size();
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                BaseRole roleModule = list.get(i);
                ps.setString(1, uId);
                ps.setString(2, roleModule.getId());

            }

        });
    }

    public void batchSaveWebAuth(ServiceAuthInfo serviceAuthInfo){
        final String sql = "insert into base_user_role(ID,ROLE_ID,USER_ID) values(?,?,?) ";

        String[] roleIds =  serviceAuthInfo.getRoleIds().split(",");

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return roleIds.length;
            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, UUID.uuid32());
                ps.setString(2, roleIds[i]);
                ps.setString(3, serviceAuthInfo.getUserId());

            }

        });
    }
}
