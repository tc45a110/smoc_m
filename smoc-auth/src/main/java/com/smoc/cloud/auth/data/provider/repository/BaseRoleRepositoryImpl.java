package com.smoc.cloud.auth.data.provider.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 角色BaseRoleRepository实现
 * 2019/4/16 17:21
 **/
public class BaseRoleRepositoryImpl {

    @Resource
    public JdbcTemplate jdbcTemplate;

    public List<Map<String,Object>> findRolesAndMenus(String system){

       String sql = "SELECT distinct role.ROLE_NAME,role.ROLE_CODE,resources.ID, resources.MODULE_NAME, resources.MODULE_CODE, resources.MODULE_PATH, resources.PARENT_ID, resources.MODULE_ICON, resources.IS_OPERATING, resources.SYSTEM_ID FROM base_module_resources resources, base_role_module base_module,base_role role WHERE resources.ACTIVE = 1 AND resources.IS_OPERATING = 1 AND  resources.ID = base_module.MODULE_ID  AND base_module.ROLE_ID = role.ID ";
       List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);

       return list;
    }
}
