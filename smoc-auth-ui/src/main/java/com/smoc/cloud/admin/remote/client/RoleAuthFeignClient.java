package com.smoc.cloud.admin.remote.client;

import com.smoc.cloud.common.auth.qo.RoleNodes;
import com.smoc.cloud.common.auth.validator.RoleAuthValidator;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 角色授权远程服务接口
 */
@FeignClient(name = "smoc-auth",path = "/auth")
public interface RoleAuthFeignClient {

    /**
     * 根据角色id 查询系统级别菜单
     *
     * @param roleId 角色id
     * @return
     */
    @RequestMapping(value = "/roleAuth/loadSystemMenus/{roleId}", method = RequestMethod.GET)
    ResponseData<List<RoleNodes>> loadSystemMenus(@PathVariable String roleId) throws Exception;

    /**
     * 查询 系统功能菜单
     *
     * @param roleId 角色id
     * @param parentId 父id
     * @return
     */
    @RequestMapping(value = "/roleAuth/loadMenus/{parentId}/{roleId}", method = RequestMethod.GET)
    ResponseData<List<RoleNodes>> loadMenus(@PathVariable String parentId, @PathVariable String roleId) throws Exception;

    /**
     * 加载角色信息
     *
     * @return
     */
    @RequestMapping(value = "/roleAuth/loadRoles", method = RequestMethod.GET)
    ResponseData<List<RoleNodes>> loadRoles() throws Exception;

    /**
     * 保存角色授权信息
     *
     * @param roleAuthValidator
     * @return
     */
    @RequestMapping(value = "/roleAuth/save", method = RequestMethod.POST)
    ResponseData save(@RequestBody RoleAuthValidator roleAuthValidator) throws Exception;
}
