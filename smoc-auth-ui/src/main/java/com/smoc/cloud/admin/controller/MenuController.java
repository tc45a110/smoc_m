package com.smoc.cloud.admin.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.service.MenusService;
import com.smoc.cloud.admin.service.SystemService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.MenusValidator;
import com.smoc.cloud.common.auth.validator.SystemValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 菜单
 * 2019/4/18 14:47
 **/
@Slf4j
@RestController
@RequestMapping("/menus")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class MenuController {

    @Autowired
    private MenusService menusService;

    @Autowired
    private SystemService systemService;

    /**
     * 菜单管理 加载左侧菜单
     *
     * @return
     */
    @RequestMapping(value = "/getMenusByParentId/{parentId}", method = RequestMethod.GET)
    public List<Nodes> getMenusByParentId(@PathVariable String parentId) {

        ResponseData<List<Nodes>> nodes = menusService.getMenusByParentId(parentId);

        return nodes.getData();
    }

    /**
     * 菜单选择 查询根目录
     *
     * @return
     */
    @RequestMapping(value = "/getRootByProjectName/{projectName}", method = RequestMethod.GET)
    public List<Nodes> getRootByProjectName(@PathVariable String projectName) {

        ResponseData<List<Nodes>> nodes = menusService.getRootByProjectName(projectName);

        return nodes.getData();
    }

    /**
     * 根据父id查询所有 子菜单
     */
    @RequestMapping(value = "/getAllSubMenusByParentId/{parentId}", method = RequestMethod.GET)
    public List<Nodes> getAllSubMenusByParentId(@PathVariable String parentId) {

        ResponseData<List<Nodes>> nodes = menusService.getAllSubMenusByParentId(parentId);

        return nodes.getData();
    }



    /**
     * 菜单框架
     *
     * @return
     */
    @RequestMapping(value = "/menuMain", method = RequestMethod.GET)
    public ModelAndView menuMain() {

        ModelAndView view = new ModelAndView("menu/menu_main");

        return view;
    }

    /**
     * 菜单树状结构
     *
     * @return
     */
    @RequestMapping(value = "/list/{systemId}/{parentId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String systemId, @PathVariable String parentId) {

        ModelAndView view = new ModelAndView("menu/menu_list");
        view.addObject("systemId", systemId);
        view.addObject("parentId", parentId);

        //默认进入
        if ("root".equals(parentId)) {
            ResponseData<List<MenusValidator>> data = new ResponseData<List<MenusValidator>>();
            view.addObject("list", data);
            return view;
        }

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //数据查询
        ResponseData<List<MenusValidator>> data = menusService.list(parentId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("list", data);

        return view;
    }

    /**
     * 进入添加页面
     *
     * @return
     */
    @RequestMapping(value = "/add/{systemId}/{parentId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String systemId, @PathVariable String parentId) {

        ModelAndView view = new ModelAndView("menu/menu_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //初始化参数
        MenusValidator menusValidator = new MenusValidator();
        menusValidator.setId(UUID.uuid32());
        menusValidator.setParentId(parentId);
        menusValidator.setSystemId(systemId);
        menusValidator.setCreateDate(new Date());

        //查询父节点
        ResponseData<MenusValidator> parentData = menusService.findById(parentId);
        if (ResponseCode.SUCCESS.getCode().equals(parentData.getCode()) && null != parentData.getData()) {
            view.addObject("parentName", parentData.getData().getModuleName());
        }

        //父节点数据为空 ，去系统管理找
        if(null == parentData.getData()){
            ResponseData<SystemValidator> systemData = systemService.findById(parentId);
            view.addObject("parentName", systemData.getData().getSystemName());
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");
        view.addObject("menusValidator", menusValidator);

        return view;
    }

    /**
     * 进入修改页面
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id) {

        ModelAndView view = new ModelAndView("menu/menu_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询数据
        ResponseData<MenusValidator> data = menusService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询父节点
        ResponseData<MenusValidator> parentData = menusService.findById(data.getData().getParentId());
        if (ResponseCode.SUCCESS.getCode().equals(parentData.getCode()) && null != parentData.getData()) {
            view.addObject("parentName", parentData.getData().getModuleName());
        }

        //父节点数据为空 ，去系统管理找
        if(null == parentData.getData()){
            ResponseData<SystemValidator> systemData = systemService.findById(data.getData().getParentId());
            view.addObject("parentName", systemData.getData().getSystemName());
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        view.addObject("menusValidator", data.getData());

        return view;
    }

    /**
     * 添加、修改信息
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated MenusValidator menusValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("menu/menu_edit");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("menusValidator", menusValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            menusValidator.setCreateDate(new Date());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            menusValidator.setUpdateDate(new Date());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        log.info("[菜单管理][{}][{}]数据:{}",op, user.getUserName(), JSON.toJSONString(menusValidator));
        //保存操作
        ResponseData data = menusService.save(menusValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/menus/list/" + menusValidator.getSystemId() + "/" + menusValidator.getParentId(), true, false));
        return view;
    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("menu/menu_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询数据
        ResponseData<MenusValidator> rd = menusService.findById(id);

        //记录日志
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        log.info("[菜单管理][delete][{}]数据:{}", user.getUserName(),id);
        //删除操作
        ResponseData data = menusService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/menus/list/" + rd.getData().getSystemId() + "/" + rd.getData().getParentId(), true, false));
        return view;
    }

}
