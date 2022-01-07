package com.smoc.cloud.admin.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.UserCacheService;
import com.smoc.cloud.admin.service.OrgService;
import com.smoc.cloud.admin.service.RolesService;
import com.smoc.cloud.admin.service.SystemService;
import com.smoc.cloud.admin.service.UsersService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Users;
import com.smoc.cloud.common.auth.validator.*;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户
 * 2019/4/18 14:47
 **/
@Slf4j
@Controller
@RequestMapping("/users")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class UserController {

    @Autowired
    private OrgService orgService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private UserCacheService userCacheService;

    /**
     * 用户列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView view = new ModelAndView("users/user_list");

        //查询数据
        PageParams<Users> params = new PageParams<>();
        params.setPageSize(12);
        params.setCurrentPage(1);

        //分页查询
        ResponseData<PageList<Users>> responseData = usersService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
            return view;
        }
        view.addObject("user", new Users());
        view.addObject("list", responseData.getData().getList());
        view.addObject("pageParams", responseData.getData().getPageParams());

        //修改密码表单
        view.addObject("userPasswordValidator", new UserPasswordValidator());

        view.addObject("clearCacheValidator", new ClearCacheValidator());
        return view;
    }

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute Users user, PageParams pageParams) {

        ModelAndView view = new ModelAndView("users/user_list");

        //分页查询
        pageParams.setParams(user);
        ResponseData<PageList<Users>> responseData = usersService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
            return view;
        }
        view.addObject("user", user);
        view.addObject("list", responseData.getData().getList());
        view.addObject("pageParams", responseData.getData().getPageParams());

        //修改密码表单
        view.addObject("userPasswordValidator", new UserPasswordValidator());
        view.addObject("clearCacheValidator", new ClearCacheValidator());
        return view;
    }

    /**
     * 进入添加页面
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add() {

        ModelAndView view = new ModelAndView("users/user_edit");

        //初始化数据
        String userId = UUID.uuid32();
        UserValidator userValidator = new UserValidator();
        userValidator.getBaseUserValidator().setId(userId);
        userValidator.getBaseUserValidator().setCreateDate(new Date());
        userValidator.getBaseUserExtendsValidator().setId(userId);
        userValidator.getBaseUserExtendsValidator().setDepartment("1");
        view.addObject("userValidator", userValidator);

        //添加标记
        view.addObject("op", "add");

        //加载角色数据
        ResponseData<List<RoleValidator>> roleData = rolesService.list();
        if (!ResponseCode.SUCCESS.getCode().equals(roleData.getCode())) {
            view.addObject("error", roleData.getCode() + ":" + roleData.getMessage());
            return view;
        }
        view.addObject("roleList", roleData.getData());

        //复选框选中的角色
        Map<String, Boolean> checkedMap = new HashMap<>();
        view.addObject("checkedMap", checkedMap);

        //加载系统信息
        ResponseData<Iterable<SystemValidator>> systemData = systemService.list();
        if (!ResponseCode.SUCCESS.getCode().equals(systemData.getCode())) {
            view.addObject("error", systemData.getCode() + ":" + systemData.getMessage());
            return view;
        }
        view.addObject("systemList", systemData.getData());
        //处理复选框选中
        Map<String, Boolean> systemMap = new HashMap<>();
        view.addObject("systemMap", systemMap);

        return view;
    }

    /**
     * 进入修改页面
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id) {

        ModelAndView view = new ModelAndView("users/user_edit");

        //加载角色数据
        ResponseData<List<RoleValidator>> roleData = rolesService.list();
        if (!ResponseCode.SUCCESS.getCode().equals(roleData.getCode())) {
            view.addObject("error", roleData.getCode() + ":" + roleData.getMessage());
            return view;
        }
        view.addObject("roleList", roleData.getData());

        //查询用户数据
        ResponseData<UserValidator> data = usersService.findById(id);
        view.addObject("userValidator", data.getData());
        view.addObject("op", "edit");

        //复选框选中的角色
        Map<String, Boolean> checkedMap = new HashMap<>();
        if (!StringUtils.isEmpty(data.getData().getRoleIds())) {
            String[] roles = data.getData().getRoleIds().split(",");
            for (int i = 0; i < roles.length; i++) {
                checkedMap.put(roles[i], true);
            }
        }
        view.addObject("checkedMap", checkedMap);

        //加载系统信息
        ResponseData<Iterable<SystemValidator>> systemData = systemService.list();
        if (!ResponseCode.SUCCESS.getCode().equals(systemData.getCode())) {
            view.addObject("error", systemData.getCode() + ":" + systemData.getMessage());
            return view;
        }
        view.addObject("systemList", systemData.getData());
        //系统选中处理
        Map<String, Boolean> systemMap = new HashMap<>();
        if (!StringUtils.isEmpty(data.getData().getBaseUserExtendsValidator().getWebChat())) {
            String[] checks = data.getData().getBaseUserExtendsValidator().getWebChat().split(",");
            for (int i = 0; i < checks.length; i++) {
                systemMap.put(checks[i], true);
            }
        }
        view.addObject("systemMap", systemMap);

        return view;
    }

    /**
     * 添加、修改信息
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated UserValidator userValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("users/user_edit");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("userValidator", userValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            userValidator.getBaseUserValidator().setCreateDate(new Date());
            userValidator.getBaseUserValidator().setActive(1);
            userValidator.getBaseUserValidator().setGender("male");
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            ResponseData<UserValidator> data = usersService.findById(userValidator.getBaseUserValidator().getId());
            userValidator.getBaseUserValidator().setCreateDate(data.getData().getBaseUserValidator().getCreateDate());
            userValidator.getBaseUserValidator().setUpdateDate(new Date());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }


        //把企业编码付给用户
       /* ResponseData<OrgValidator> responseData = orgService.findById(userValidator.getBaseUserValidator().getOrganization());
        userValidator.getBaseUserExtendsValidator().setCode(responseData.getData().getOrgCode());*/
        //如果销售码为空默认XY0000
        if(StringUtils.isEmpty(userValidator.getBaseUserExtendsValidator().getCode())){
            userValidator.getBaseUserExtendsValidator().setCode("XY0000");
        }
        if(StringUtils.isEmpty(userValidator.getBaseUserExtendsValidator().getParentCode())){
            userValidator.getBaseUserExtendsValidator().setCode("0");
        }

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[角色授权][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(userValidator));

        //保存数据
        ResponseData data = usersService.save(userValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/users/list", true, false));
        return view;
    }


    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("users/list");

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[角色授权][delete][{}]数据:{}", user.getUserName(), id);
        //删除数据
        ResponseData data = usersService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/users/list", true, false));
        return view;
    }

    /**
     * 修改密码
     *
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ModelAndView resetPassword(@ModelAttribute @Validated UserPasswordValidator userPasswordValidator, BindingResult result, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("users/list");
        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("userPasswordValidator", userPasswordValidator);
            return view;
        }

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[用户管理][resetPassword][{}]数据:{}", user.getUserName(), JSON.toJSONString(userPasswordValidator));
        //重置密码
        ResponseData data = usersService.resetPassword(userPasswordValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("userPasswordValidator", userPasswordValidator);
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        return new ModelAndView("redirect:/users/list");

    }

    /**
     * 禁用、启用用户
     *
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/forbiddenUser/{id}/{status}", method = RequestMethod.GET)
    public ModelAndView forbiddenUser(@PathVariable String id, @PathVariable Integer status, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("users/list");

        //用户状态转换
        if (0 == status) {
            status = 1;
        } else {
            status = 0;
        }

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[用户管理][forbiddenUser][{}]数据:{}", user.getUserName(),  id + "-" + status);
        //禁用、启用用户
        ResponseData data = usersService.forbiddenUser(id, status);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/users/list", true, false));
        return view;
    }

    /**
     * 进入修改页面
     *
     * @return
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String id) {

        ModelAndView view = new ModelAndView("users/user_view");

        //加载角色数据
        ResponseData<List<RoleValidator>> roleData = rolesService.list();
        if (!ResponseCode.SUCCESS.getCode().equals(roleData.getCode())) {
            view.addObject("error", roleData.getCode() + ":" + roleData.getMessage());
            return view;
        }
        view.addObject("roleList", roleData.getData());

        //查询用户数据
        ResponseData<UserValidator> data = usersService.findById(id);
        view.addObject("userValidator", data.getData());

        //复选框选中的角色
        Map<String, Boolean> checkedMap = new HashMap<>();
        if (!StringUtils.isEmpty(data.getData().getRoleIds())) {
            String[] roles = data.getData().getRoleIds().split(",");
            for (int i = 0; i < roles.length; i++) {
                checkedMap.put(roles[i], true);
            }
        }
        view.addObject("checkedMap", checkedMap);

        //加载系统信息
        ResponseData<Iterable<SystemValidator>> systemData = systemService.list();
        if (!ResponseCode.SUCCESS.getCode().equals(systemData.getCode())) {
            view.addObject("error", systemData.getCode() + ":" + systemData.getMessage());
            return view;
        }
        view.addObject("systemList", systemData.getData());

        //系统选中处理
        Map<String, Boolean> systemMap = new HashMap<>();
        if (!StringUtils.isEmpty(data.getData().getBaseUserExtendsValidator().getWebChat())) {
            String[] checks = data.getData().getBaseUserExtendsValidator().getWebChat().split(",");
            for (int i = 0; i < checks.length; i++) {
                systemMap.put(checks[i], true);
            }
        }
        view.addObject("systemMap", systemMap);

        return view;
    }

    /**
     * 修改密码
     *
     * @return
     */
    @RequestMapping(value = "/clearUsersCache", method = RequestMethod.POST)
    public ModelAndView clearUsersCache(@ModelAttribute @Validated ClearCacheValidator clearCacheValidator, BindingResult result, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("users/list");


        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[用户管理][clearUsersCache][{}]数据:{}", user.getUserName(), JSON.toJSONString(clearCacheValidator));

        //查询用户数据
        ResponseData<UserValidator> userData = usersService.findById(clearCacheValidator.getUserId());
        ResponseData data = userCacheService.clearUserCache(clearCacheValidator.getSystem(), userData.getData().getBaseUserValidator().getId(), userData.getData().getBaseUserValidator().getUserName());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", "用户缓存清除失败！");
            return view;
        }

        return new ModelAndView("redirect:/users/list");

    }



}
