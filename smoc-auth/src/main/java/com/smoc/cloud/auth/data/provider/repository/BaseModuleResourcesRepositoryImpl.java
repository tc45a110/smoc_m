package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.rowmapper.NodesRowMapper;
import com.smoc.cloud.common.auth.qo.Nodes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 菜单管理数据操作实现类
 * 2019/4/17 16:03
 **/
public class BaseModuleResourcesRepositoryImpl {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getMenusByUserIdAndSystemId(String userId, String systemId) {

        Object[] params = new Object[2];
        params[0] = systemId;
        params[1] = userId;
        String sql = "SELECT distinct resources.ID, resources.MODULE_NAME, resources.MODULE_CODE, resources.MODULE_PATH, resources.PARENT_ID, resources.MODULE_ICON FROM base_module_resources resources, base_role_module base_module,base_user_role user_role WHERE resources.ACTIVE = 1 and resources.SYSTEM_ID =? AND  resources.ID = base_module.MODULE_ID  AND base_module.ROLE_ID = user_role.ROLE_ID AND user_role.USER_ID= ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, params);

        return list;
    }

    public List<Nodes> getMenusByParentId(String parentId) {
        if (StringUtils.isEmpty(parentId)) {
            return new ArrayList<>();
        }

        String sql;
        if ("root".equals(parentId)) {
            sql = "select ID,SYSTEM_NAME MODULE_NAME, '' MODULE_PATH,'' MODULE_ICON,PROJECT_NAME SYSTEM_ID,0 IS_OPERATING from base_system where ACTIVE = 1  order by SORT ASC";
            List<Nodes> nodes = jdbcTemplate.query(sql, new NodesRowMapper());
            return nodes;
        }

        Object[] params = new Object[1];
        params[0] = parentId;
        sql = "select ID,MODULE_NAME,MODULE_PATH,MODULE_ICON,SYSTEM_ID,IS_OPERATING from base_module_resources where ACTIVE =1 and PARENT_ID= ? order by sort ASC ";
        List<Nodes> nodes = jdbcTemplate.query(sql, params, new NodesRowMapper());

        return nodes;

    }

    public List<Nodes> getRootByProjectName(String projectName) {


        if (StringUtils.isEmpty(projectName)) {
            return new ArrayList<>();
        }

        Object[] params = new Object[1];
        params[0] = projectName;

        String sql = "select ID,SYSTEM_NAME MODULE_NAME, '' MODULE_PATH,'' MODULE_ICON,PROJECT_NAME SYSTEM_ID,0 IS_OPERATING from base_system where ACTIVE = 1 and PROJECT_NAME =?  order by SORT ASC";
        List<Nodes> nodes = jdbcTemplate.query(sql, params, new NodesRowMapper());


        return nodes;

    }

    public List<Nodes> getAllSubMenusByParentId(String parentId) {

        if (StringUtils.isEmpty(parentId)) {
            return new ArrayList<>();
        }

        List<Nodes> resultList = new ArrayList<>();

        List<Nodes> nodes = this.getMenusByParentId(parentId);

        if (null != nodes && nodes.size() > 0) {

            for (Nodes node : nodes) {
                List<Nodes> subNodes = this.getAllSubMenusByParentId(node.getId());
                node.setNodes(subNodes);
                resultList.add(node);
            }
        }

        return resultList;

    }

    public List<Nodes> getUserMenus(String userId, String projectName) {

        String sql = "";

        Object[] params = new Object[2];
        params[0] = projectName;
        params[1] = userId;
        sql = "select menu.ID,menu.MODULE_NAME,menu.MODULE_PATH,menu.MODULE_ICON,menu.SYSTEM_ID,menu.IS_OPERATING from base_module_resources menu,base_system bm,base_role_module rm,base_user_role ur where menu.ACTIVE =1 and IS_DISPLAY = 1 and menu.PARENT_ID = bm.ID and bm.PROJECT_NAME= ? and ur.USER_ID=? and ur.ROLE_ID = rm.ROLE_ID and rm.MODULE_ID = menu.ID group by menu.ID order by menu.sort ASC ";
        List<Nodes> nodes = jdbcTemplate.query(sql, params, new NodesRowMapper());

        List<Nodes> resultNodes = new ArrayList<>();
        //第一级
        for (Nodes node : nodes) {

            //第二级
            List<Nodes> subNodes = this.getSubNodes(userId, node.getId());
            List<Nodes> subResultNodes = new ArrayList<>();
            for (Nodes subNode : subNodes) {

                //第三级
                List<Nodes> subbNodes = this.getSubNodes(userId, subNode.getId());
                subNode.setNodes(subbNodes);
                subResultNodes.add(subNode);

            }
            node.setNodes(subResultNodes);

            resultNodes.add(node);
        }

        return resultNodes;

    }

    public List<Nodes> getSubNodes(String userId, String parentId) {

        String sql = "";

        Object[] params = new Object[2];
        params[0] = parentId;
        params[1] = userId;
        sql = "select menu.ID,menu.MODULE_NAME,menu.MODULE_PATH,menu.MODULE_ICON,menu.SYSTEM_ID,menu.IS_OPERATING from base_module_resources menu ,base_role_module rm,base_user_role ur where menu.ACTIVE =1 and IS_DISPLAY = 1 and menu.PARENT_ID = ? and ur.USER_ID=? and ur.ROLE_ID = rm.ROLE_ID and rm.MODULE_ID = menu.ID group by menu.ID order by menu.sort ASC ";
        List<Nodes> nodes = jdbcTemplate.query(sql, params, new NodesRowMapper());

        return nodes;

    }
}
