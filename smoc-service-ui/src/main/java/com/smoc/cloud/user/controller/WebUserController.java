package com.smoc.cloud.user.controller;

import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.UserPasswordValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.user.service.WebUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 公用系统用户（可自定义扩展）
 * 2019/5/14 11:37
 **/
@Slf4j
@Controller
@RequestMapping("/sysUser")
public class WebUserController {

    @Autowired
    private WebUserService webUserService;

    @RequestMapping(value = "/web/resetPassword", method = RequestMethod.GET)
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
    @RequestMapping(value = "/web/resetOwnPassword", method = RequestMethod.POST)
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
        userPasswordValidator.setId(user.getId());
        ResponseData data = webUserService.resetOwnPassword(userPasswordValidator);
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
