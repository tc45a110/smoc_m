package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.BaseRoleModule;
import com.smoc.cloud.auth.data.provider.rowmapper.RoleMenusRowMapper;
import com.smoc.cloud.auth.data.provider.rowmapper.RoleNodesRowMapper;
import com.smoc.cloud.common.auth.qo.RoleMenus;
import com.smoc.cloud.common.auth.qo.RoleNodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BaseRoleModuleRepositoryImpl {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public List<RoleNodes> loadSystemMenus(String roleId) {

        if (StringUtils.isEmpty(roleId)) {
            return new ArrayList<>();
        }

        //查询系统级别
        String sql = "select bm.ID,bm.SYSTEM_NAME MODULE_NAME,'' MODULE_ICON,rm.ID CHECKED,1 LAZY_LOAD,PROJECT_NAME SYSTEM_ID,0 IS_OPERATING  from base_system bm left join base_role_module rm on bm.ID = rm.MODULE_ID and rm.ROLE_ID=? where ACTIVE = 1  order by SORT ASC";

        Object[] params = new Object[1];
        params[0] = roleId;
        List<RoleNodes> nodes = jdbcTemplate.query(sql, params, new RoleNodesRowMapper());

        return nodes;
    }

    public List<RoleNodes> loadMenus(String parentId, String roleId) {

        if (StringUtils.isEmpty(roleId) || StringUtils.isEmpty(parentId)) {
            return new ArrayList<>();
        }

        List<RoleNodes> resultList = new ArrayList<>();

        Object[] params = new Object[2];
        params[0] = roleId;
        params[1] = parentId;

        String sql = "SELECT menus.ID,menus.MODULE_NAME,menus.MODULE_ICON,menus.SYSTEM_ID,rm.ID CHECKED,0 LAZY_LOAD,menus.IS_OPERATING FROM base_module_resources menus left join base_role_module rm on menus.id=rm.MODULE_ID and rm.ROLE_ID=? where   ACTIVE =1 AND menus.PARENT_ID=?  order by sort ASC";
        List<RoleNodes> nodes = jdbcTemplate.query(sql, params, new RoleNodesRowMapper());

        if (null != nodes && nodes.size() > 0) {
            for (RoleNodes node : nodes) {
                List<RoleNodes> subNodes = this.loadMenus(node.getId(), roleId);
                node.setNodes(subNodes);
                resultList.add(node);
            }
        }

        return resultList;
    }

    public List<RoleNodes> loadRoles() {

        //查询系统级别
        String sql = "select role.ID,role.ROLE_NAME MODULE_NAME,'' MODULE_ICON,'' CHECKED,0 LAZY_LOAD,ROLE_CODE SYSTEM_ID,1 IS_OPERATING  from base_role role order by CREATE_DATE ";

        List<RoleNodes> nodes = jdbcTemplate.query(sql, new RoleNodesRowMapper());

        return nodes;
    }


    public void batchSave(final List<BaseRoleModule> roleModules) {
        final String sql = "insert into base_role_module(ID,ROLE_ID,MODULE_ID) values(?,?,?) ";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return roleModules.size();

            }

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                BaseRoleModule roleModule = roleModules.get(i);
                ps.setString(1, roleModule.getId());
                ps.setString(2, roleModule.getRoleId());
                ps.setString(3, roleModule.getModuleId());

            }

        });
    }

    public void deleteByRoleId(String roleId) {
        String sql = "delete from base_role_module where ROLE_ID = ?";

        Object[] params = new Object[1];
        params[0] = roleId;
        jdbcTemplate.update(sql, params);

    }

    /**
     * 根据项目标示 的到系统可以操作的菜单与角色关系
     *
     * @param projectName
     */
    public List<RoleMenus> getOperatingMenus(String projectName) {

        String sql = "SELECT distinct menu.MODULE_PATH,role.ROLE_CODE FROM base_module_resources menu,base_role role,base_role_module role_menu where menu.SYSTEM_ID =? and menu.IS_OPERATING=1 and menu.ID= role_menu.MODULE_ID and role.ID = role_menu.ROLE_ID";
        Object[] params = new Object[1];
        params[0] = projectName;
        List<RoleMenus> list = jdbcTemplate.query(sql, params, new RoleMenusRowMapper());

        return list;

    }

}
