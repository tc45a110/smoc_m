package com.smoc.cloud.admin.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.service.SystemService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.SystemValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
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

/**
 * 系统
 * 2019/4/18 14:47
 **/
@Slf4j
@Controller
@RequestMapping("/system")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class SystemController {

    @Autowired
    private SystemService systemService;

    /**
     * 系统列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView view = new ModelAndView("system/system_list");

        //查询数据
        ResponseData<Iterable<SystemValidator>> data = systemService.list();
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("list", data);

        return view;

    }

    /**
     * 添加系统
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add() {

        ModelAndView view = new ModelAndView("system/system_edit");

        //初始化变量
        SystemValidator systemValidator = new SystemValidator();
        systemValidator.setId(UUID.uuid32());
        systemValidator.setActive(1);
        systemValidator.setCreateDate(new Date());

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");
        view.addObject("systemValidator", systemValidator);
        return view;
    }

    /**
     * 进入修改页面
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id) {

        ModelAndView view = new ModelAndView("system/system_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询数据
        ResponseData<SystemValidator> data = systemService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        view.addObject("systemValidator", data.getData());
        return view;
    }

    /**
     * 添加、修改系统
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated SystemValidator systemValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("system/system_edit");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("systemValidator", systemValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            systemValidator.setCreateDate(new Date());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            systemValidator.setUpdateDate(new Date());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[角色授权][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(systemValidator));
        //保存操作
        ResponseData data = systemService.save(systemValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/system/list", true, false));
        return view;
    }

    /**
     * 根据id 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("system/system_list");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[角色授权][delete][{}]数据:{}", user.getUserName(), id);
        //删除操作
        ResponseData data = systemService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/system/list", true, false));
        return view;
    }

}
