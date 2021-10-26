package com.smoc.cloud.auth.data.provider.rowmapper;

import com.smoc.cloud.common.auth.qo.RoleMenus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 映射角色菜单查询
 */
public class RoleMenusRowMapper implements RowMapper<RoleMenus> {

    @Override
    public RoleMenus mapRow(ResultSet resultSet, int i) throws SQLException {

        RoleMenus roleMenus = new RoleMenus();
        roleMenus.setMenusPath(resultSet.getString("MODULE_PATH"));
        roleMenus.setRoleCode(resultSet.getString("ROLE_CODE"));

        return roleMenus;
    }
}
