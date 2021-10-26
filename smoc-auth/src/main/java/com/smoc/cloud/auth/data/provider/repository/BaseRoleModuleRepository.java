package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.BaseRoleModule;
import com.smoc.cloud.common.auth.qo.RoleMenus;
import com.smoc.cloud.common.auth.qo.RoleNodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 角色资源数据操作类
 * 2019/3/29 14:29
 */
public interface BaseRoleModuleRepository extends CrudRepository<BaseRoleModule, String>, JpaRepository<BaseRoleModule, String> {

    /**
     * 根据角色id 查询系统级别菜单
     *
     * @param roleId
     * @return
     */
    List<RoleNodes> loadSystemMenus(String roleId);

    /**
     * 查询 系统功能菜单
     *
     * @param parentId
     * @param roleId
     * @return
     */
    List<RoleNodes> loadMenus(String parentId, String roleId);


    /**
     * 加载角色信息
     *
     * @return
     */
    List<RoleNodes> loadRoles();

    /**
     * 批量保存
     *
     * @param roleModules
     */
    void batchSave(final List<BaseRoleModule> roleModules);

    /**
     * 根据roleId删除
     *
     * @param roleId
     */
    void deleteByRoleId(String roleId);

    /**
     * 根据项目标示 的到系统可以操作的菜单与角色关系
     * @param projectName
     */
    List<RoleMenus> getOperatingMenus(String projectName);
}
