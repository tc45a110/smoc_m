package com.smoc.cloud.admin.security.controller;

import com.smoc.cloud.admin.security.remote.service.SysUserService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.UserPasswordValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统用户基本操作
 * 2019/5/16 12:47
 **/
@Slf4j
@Controller
@RequestMapping("/sysUser")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class SystemUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 首页入口
     *
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index() {

        ModelAndView view = new ModelAndView("index");

        return view;
    }

    /**
     * 查看详细信息
     *
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public ModelAndView resetPassword(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("sys_user/user_reset_password");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        UserPasswordValidator userPasswordValidator = new UserPasswordValidator();
        userPasswordValidator.setId(user.getId());

        //修改密码表单
        view.addObject("userPasswordValidator", userPasswordValidator);

        return view;
    }

    /**
     * 修改密码
     *
     * @return
     */
    @RequestMapping(value = "/resetOwnPassword", method = RequestMethod.POST)
    public ModelAndView resetOwnPassword(@ModelAttribute @Validated UserPasswordValidator userPasswordValidator, BindingResult result, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("sys_user/user_reset_password");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("userPasswordValidator", userPasswordValidator);
            return view;
        }

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[用户管理][重置密码][{}]数据:{}",user.getUserName(), userPasswordValidator.getId());

        //修改密码操作
        ResponseData data = sysUserService.resetOwnPassword(userPasswordValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("userPasswordValidator", userPasswordValidator);
            FieldError err = new FieldError("原密码", "oldPassword", "原密码错误！");
            result.addError(err);
            return view;
        }
        view.addObject("success", "密码修改成功！");

        return view;

    }

}
