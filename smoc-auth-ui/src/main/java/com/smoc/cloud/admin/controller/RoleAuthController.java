package com.smoc.cloud.admin.controller;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.service.RoleAuthService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.RoleNodes;
import com.smoc.cloud.common.auth.validator.RoleAuthValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
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
     * 进入角色授权页
     *
     * @return
     */
    @RequestMapping(value = "/listMain", method = RequestMethod.GET)
    public ModelAndView listMain() {

        ModelAndView view = new ModelAndView("role_auth/list_main");

        return view;
    }

    /**
     * 加载角色授权 左边菜单的角色数据
     *
     * @return
     */
    @RequestMapping(value = "/loadRoles", method = RequestMethod.GET)
    public List<RoleNodes> loadRoles() {

        ResponseData<List<RoleNodes>> data = roleAuthService.loadRoles();
        return data.getData();

    }

    /**
     * 查询授权的右侧菜单
     *
     * @return
     */
    @RequestMapping(value = "/menusTree/{roleId}", method = RequestMethod.GET)
    public ModelAndView menusTree(@PathVariable String roleId) {

        ModelAndView view = new ModelAndView("role_auth/menus_tree");
        view.addObject("roleId", roleId);

        RoleAuthValidator roleAuthValidator = new RoleAuthValidator();
        roleAuthValidator.setRoleId(roleId);

        view.addObject("roleAuthValidator", roleAuthValidator);


        return view;

    }

    /**
     * 根据角色id 查询系统级别菜单
     *
     * @param roleId 角色id
     * @return
     */
    @RequestMapping(value = "/loadSystemMenus/{roleId}", method = RequestMethod.GET)
    public List<RoleNodes> loadSystemMenus(@PathVariable String roleId) {

        ResponseData<List<RoleNodes>> data = roleAuthService.loadSystemMenus(roleId);
        return data.getData();

    }

    /**
     * 查询 系统功能菜单
     *
     * @param roleId   角色id
     * @param parentId 父id
     * @return
     */
    @RequestMapping(value = "/loadMenus/{parentId}/{roleId}", method = RequestMethod.GET)
    public List<RoleNodes> loadMenus(@PathVariable String parentId, @PathVariable String roleId) {

        ResponseData<List<RoleNodes>> data = roleAuthService.loadMenus(parentId, roleId);
        return data.getData();

    }


    /**
     * 保存角色授权信息
     *
     * @param roleAuthValidator
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated RoleAuthValidator roleAuthValidator, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("role_auth/menusList");

        //记录日志
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        log.info("[角色授权][save][{}]数据:{}", user.getUserName(),JSON.toJSONString(roleAuthValidator));
        //保存授权信息
        ResponseData data = roleAuthService.save(roleAuthValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/roleAuth/menusTree/" + roleAuthValidator.getRoleId(), true, false));
        return view;
    }


}
