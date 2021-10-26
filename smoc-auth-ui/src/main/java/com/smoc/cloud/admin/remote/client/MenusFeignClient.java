package com.smoc.cloud.admin.remote.client;

import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.MenusValidator;
import com.smoc.cloud.common.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 菜单管理远程服务接口
 * 2019/4/23 16:28
 **/
@FeignClient(name = "smoc-auth",path = "/auth")
public interface MenusFeignClient {

    /**
     * 根据父id获取系统菜单
     */
    @RequestMapping(value = "/menus/getMenusByParentId/{parentId}", method = RequestMethod.GET)
    ResponseData<List<Nodes>> getMenusByParentId(@PathVariable String parentId) throws Exception;

    /**
     * 菜单选择 查询根目录
     */
    @RequestMapping(value = "/menus/getRootByProjectName/{projectName}", method = RequestMethod.GET)
    ResponseData<List<Nodes>> getRootByProjectName(@PathVariable String projectName) throws Exception;

    /**
     * 根据父id查询所有 子菜单
     */
    @RequestMapping(value = "/menus/getAllSubMenusByParentId/{parentId}", method = RequestMethod.GET)
    ResponseData<List<Nodes>> getAllSubMenusByParentId(@PathVariable String parentId) throws Exception;

    /**
     * 根据父id获取菜单列表信息
     */
    @RequestMapping(value = "/menus/list/{parentId}", method = RequestMethod.GET)
    ResponseData<List<MenusValidator>> list(@PathVariable String parentId) throws Exception;

    /**
     * 根据id获取菜单信息
     */
    @RequestMapping(value = "/menus/findById/{id}", method = RequestMethod.GET)
    ResponseData<MenusValidator> findById(@PathVariable String id) throws Exception;

    /**
     * 保存、修改系统数据
     * op 是类型 表示了保存或修改
     */
    @RequestMapping(value = "/menus/save/{op}", method = RequestMethod.POST)
    ResponseData save(@RequestBody MenusValidator menusValidator, @PathVariable String op) throws Exception;

    /**
     * 根据id删除菜单数据
     */
    @RequestMapping(value = "/menus/deleteById/{id}", method = RequestMethod.GET)
    ResponseData deleteById(@PathVariable String id) throws Exception;

}
