package com.smoc.cloud.auth.api;


import com.smoc.cloud.auth.data.provider.service.RoleAuthService;
import com.smoc.cloud.common.auth.qo.RoleMenus;
import com.smoc.cloud.common.auth.qo.RoleNodes;
import com.smoc.cloud.common.auth.validator.RoleAuthValidator;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 角色授权
 */
@Slf4j
@RestController
@RequestMapping("/roleAuth")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class RoleAuthController {

    @Autowired
    private RoleAuthService roleAuthService;

    /**
     * 根据角色id 查询系统级别菜单
     *
     * @param roleId 角色id
     * @return
     */
    @RequestMapping(value = "/loadSystemMenus/{roleId}", method = RequestMethod.GET)
    public ResponseData loadSystemMenus(@PathVariable String roleId) {

        ResponseData data = roleAuthService.loadSystemMenus(roleId);
        return data;

    }

    /**
     * 查询 系统功能菜单
     *
     * @param roleId   角色id
     * @param parentId 父id
     * @return
     */
    @RequestMapping(value = "/loadMenus/{parentId}/{roleId}", method = RequestMethod.GET)
    public ResponseData loadMenus(@PathVariable String parentId, @PathVariable String roleId) {

        ResponseData data = roleAuthService.loadMenus(parentId, roleId);
        return data;

    }

    /**
     * 加载角色信息
     *
     * @return
     */
    @RequestMapping(value = "/loadRoles", method = RequestMethod.GET)
    public ResponseData loadRoles() {

        ResponseData<List<RoleNodes>> data = roleAuthService.loadRoles();
        return data;

    }

    /**
     * 保存角色授权信息
     *
     * @param roleAuthValidator
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ResponseData save(@RequestBody @Validated RoleAuthValidator roleAuthValidator) {


        ResponseData data = roleAuthService.save(roleAuthValidator);


        return data;

    }

    /**
     * 根据项目标示 的到系统可以操作的菜单与角色关系
     *
     * @param projectName
     */
    @PreAuthorize("hasAuthority('SUPER-ROLE')")
    @RequestMapping(value = "/getMenus/{projectName}", method = RequestMethod.GET)
    public List<RoleMenus> getOperatingMenus(@PathVariable String projectName) {

        ResponseData<List<RoleMenus>> data = roleAuthService.getOperatingMenus(projectName);

        return data.getData();

    }


}
