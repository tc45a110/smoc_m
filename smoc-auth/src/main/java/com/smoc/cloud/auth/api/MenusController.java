package com.smoc.cloud.auth.api;

import com.google.gson.Gson;
import com.smoc.cloud.auth.data.provider.entity.BaseModuleResources;
import com.smoc.cloud.auth.data.provider.service.BaseModuleResourcesService;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.MenusValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

/**
 * 菜单接口
 * 2019/4/16 20:26
 **/
@Slf4j
@RestController
@RequestMapping("menus")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class MenusController {

    @Autowired
    private BaseModuleResourcesService baseModuleResourcesService;

    /**
     * 根据项目标示、token查询用户左侧的菜单（三级）
     *
     * @param projectName 项目标示
     * @return
     */
    @PreAuthorize("hasAuthority('SUPER-ROLE')")
    @RequestMapping(value = "/getUserMenus/{projectName}/{userId}", method = RequestMethod.GET)
    public List<Nodes> getUserMenus(@PathVariable String projectName, @PathVariable String userId) {

        ResponseData<List<Nodes>> data = baseModuleResourcesService.getUserMenus(projectName,userId);
        return data.getData() ;
    }

    /**
     * 根据父id获取系统菜单
     *
     * @param parentId 父ID
     */
    @RequestMapping(value = "/getMenusByParentId/{parentId}", method = RequestMethod.GET)
    public ResponseData getMenusByParentId(@PathVariable String parentId) {

        ResponseData<List<Nodes>> data = baseModuleResourcesService.getMenusByParentId(parentId);

        return data;
    }

    /**
     * 根据 用户ID 父id 关联角色信息，查询 一级菜单
     * @param userId
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/getSubNodes/{userId}/{parentId}", method = RequestMethod.GET)
    public List<Nodes> getSubNodes(@PathVariable String userId,@PathVariable String parentId) {

        List<Nodes> data = baseModuleResourcesService.getSubNodes(userId,parentId);

        return data;
    }



    /**
     * 菜单选择 查询根目录
     *
     * @param projectName 项目标识
     */
    @RequestMapping(value = "/getRootByProjectName/{projectName}", method = RequestMethod.GET)
    public ResponseData getRootByProjectName(@PathVariable String projectName) {

        ResponseData<List<Nodes>> data = baseModuleResourcesService.getRootByProjectName(projectName);
        return data;
    }

    /**
     * 根据父id查询所有 子菜单
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/getAllSubMenusByParentId/{parentId}", method = RequestMethod.GET)
    public ResponseData getAllSubMenusByParentId(@PathVariable String parentId) {

        ResponseData<List<Nodes>> data = baseModuleResourcesService.getAllSubMenusByParentId(parentId);
        return data;
    }

    /**
     * 根据父id查询所有 子菜单
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/getAllMenusByParentId/{parentId}", method = RequestMethod.GET)
    public List<Nodes> getAllMenusByParentId(@PathVariable String parentId) {

        ResponseData<List<Nodes>> data = baseModuleResourcesService.getAllSubMenusByParentId(parentId);
        log.info("[resource]:{}",new Gson().toJson(data));
        return data.getData();
    }


    /**
     * 根据父id查询菜单列表
     *
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    public ResponseData list(@PathVariable String parentId) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR);
        }

        ResponseData<List<BaseModuleResources>> data = baseModuleResourcesService.findBaseModuleResourcesByParentId(parentId);

        return data;
    }

    /**
     * 根据ID查询菜单数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public ResponseData findById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData<BaseModuleResources> data = baseModuleResourcesService.findById(id);

        return data;
    }

    /**
     * 添加、修改菜单数据
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody MenusValidator menusValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(menusValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(menusValidator));
        }

        //数据复制
        BaseModuleResources resources = new BaseModuleResources();
        BeanUtils.copyProperties(menusValidator, resources);

        //保存操作
        ResponseData data = baseModuleResourcesService.save(resources, op);

        return data;
    }

    /**
     * 根据ID 删除数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ResponseData deleteById(@PathVariable String id) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(validator));
        }

        ResponseData data = baseModuleResourcesService.deleteById(id);

        return data;
    }
}
