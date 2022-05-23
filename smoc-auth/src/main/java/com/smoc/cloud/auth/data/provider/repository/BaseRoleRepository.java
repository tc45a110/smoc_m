package com.smoc.cloud.auth.data.provider.repository;


import com.smoc.cloud.auth.data.provider.entity.BaseRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * 角色数据操作类
 * 2019/3/29 14:29
 */
public interface BaseRoleRepository extends CrudRepository<BaseRole, String>, JpaRepository<BaseRole, String> {


    Iterable<BaseRole> findBaseRoleByRoleCodeOrAndRoleName(String roleCode, String roleName);

    /**
     * 根据用户id 查询角色
     *
     * @param userId
     * @return
     */
    @Query(value = "SELECT role.ID,role.ROLE_CODE,role.ROLE_NAME,role.ORGANIZATION,role.CREATE_DATE,role.UPDATE_DATE FROM base_role role,base_user_role user_role WHERE role.ID=user_role.ROLE_ID AND user_role.USER_ID=:userId ", nativeQuery = true)
    List<BaseRole> findBaseRoleByUserId(@Param("userId") String userId);

    /**
     * 查询角色及所对应的菜单
     *
     * @return
     */
    List<Map<String, Object>> findRolesAndMenus(String system);

    /**
     * 查询自服务平台角色
     * @return
     */
    @Query(value = "SELECT role.ID,role.ROLE_CODE,role.ROLE_NAME,role.ORGANIZATION,role.CREATE_DATE,role.UPDATE_DATE FROM base_role role WHERE role.ROLE_CODE like '%SMS%' ", nativeQuery = true)
    List<BaseRole> webLoginAuth();
}
