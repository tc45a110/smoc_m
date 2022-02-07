package com.smoc.cloud.auth.data.provider.repository;

import com.smoc.cloud.auth.data.provider.entity.BaseModuleResources;
import com.smoc.cloud.common.auth.qo.Nodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统资源数据操作类
 * 2019/3/29 14:29
 */
public interface BaseModuleResourcesRepository extends CrudRepository<BaseModuleResources, String>, JpaRepository<BaseModuleResources, String> {


    /**
     * 根据父id查询菜单信息
     * @param parentId
     * @return
     */
    List<BaseModuleResources> findBaseModuleResourcesByParentIdOrderBySortAsc(String parentId);

    /**
     * 根据角色查询拥有菜单信息
     * @param roleId
     * @return
     */
    @Query(value = "SELECT resources.ID, resources.MODULE_NAME, resources.MODULE_CODE, resources.MODULE_PATH, resources.PARENT_ID, resources.MODULE_ICON, resources.HTTP_METHOD, resources.IS_OPERATING, resources.SORT, resources.SYSTEM_ID, resources. ACTIVE, resources.CREATE_DATE, resources.UPDATE_DATE FROM base_module_resources resources, base_role_module base_module WHERE resources.ID = base_module.MODULE_ID  AND resources.ACTIVE = 1 AND base_module.ROLE_ID = :roleId ", nativeQuery = true)
    List<BaseModuleResources> findBaseModuleResourcesByRoleId(@Param("roleId") String roleId);

    /**
     * 根据用户ID 查询其拥有资源
     *
     * @return
     */
    //@Query(value = "SELECT resources.ID, resources.MODULE_NAME, resources.MODULE_CODE, resources.MODULE_PATH, resources.PARENT_ID, resources.MODULE_ICON, resources.HTTP_METHOD, resources.IS_OPERATING, resources.SORT, resources.SYSTEM_ID, resources. ACTIVE, resources.CREATE_DATE, resources.UPDATE_DATE FROM base_module_resources resources, base_role_module base_module,base_user_role user_role WHERE resources.ACTIVE = 1 AND  resources.ID = base_module.MODULE_ID  AND base_module.ROLE_ID = user_role.ROLE_ID AND user_role.USER_ID= :userId", nativeQuery = true)
    List<Map<String,Object>> getMenusByUserIdAndSystemId(String userId, String systemId);

    /** 
     * 根据父ID 查询下面菜单
     * @param parentId
     * @return
     */
    List<Nodes>  getMenusByParentId(String parentId);

    /**
     * 菜单选择 查询根目录
     * @param projectName
     * @return
     */
    List<Nodes>  getRootByProjectName(String projectName);

    /**
     * 根据父id查询所有 子菜单
     * @param parentId
     * @return
     */
    List<Nodes> getAllSubMenusByParentId(String parentId);


    /**
     * 根据项目标示、用户id查询用户左侧的菜单（三级）
     * @param projectName 项目标示
     * @param userId 用户id
     * @return
     */
    List<Nodes>  getUserMenus(String userId,String projectName);

    /**
     * 根据 用户ID 父id 关联角色信息，查询 一级菜单
     * @param userId
     * @param parentId
     * @return
     */
    List<Nodes> getSubNodes(String userId, String parentId);
}
