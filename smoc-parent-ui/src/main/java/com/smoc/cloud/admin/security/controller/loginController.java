package com.smoc.cloud.admin.security.controller;

import com.smoc.cloud.admin.security.properties.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录操作
 * 2019/4/17 10:50
 **/
@Controller
public class loginController {

    @Autowired
    private SystemProperties systemProperties;

    /**
     * 进入登录页面
     *
     * @return
     */
    @RequestMapping("/")
    public String login() {
        return "login";
    }

    /**
     * 进入登录页面
     *
     * @return
     */
    @RequestMapping("/login")
    public String login_() {
        return "login";
    }

    /**
     * 进入登录页面
     *
     * @return
     */
    @RequestMapping("/login/{failure}")
    public ModelAndView login_(@PathVariable String failure) {

        ModelAndView view = new ModelAndView("login");
        String errorMessage = "登录失败";
        if("verify".equals(failure)){
            errorMessage = "验证码错误";
        }
        if("failure".equals(failure)){
            errorMessage = "用户名或密码错误";
        }
        if("UserNotFound".equals(failure)){
            errorMessage = "用户不存在";
        }
        if("TokenIsEmpty".equals(failure)){
            errorMessage = "TOKEN为空";
        }
        if("TokenError".equals(failure)){
            errorMessage = "TOKEN错误";
        }
        if("TokenServerException".equals(failure)){
            errorMessage = "验证服务器异常";
        }
        if("400".equals(failure)){
            errorMessage = "Token校验失败";
        }
        if("userIsLocked".equals(failure)){
            errorMessage = "密码错误次数超过5次，账号锁定30分钟，30分钟后再试！";
        }
        if("passwordInvalidOneTimes".equals(failure)){
            errorMessage = "用户名或密码错误，你还有4次机会，否则账户将被锁定30分钟";
        }
        if("passwordInvalidTwoTimes".equals(failure)){
            errorMessage = "用户名或密码错误，你还有3次机会，否则账户将被锁定30分钟";
        }
        if("passwordInvalidThreeTimes".equals(failure)){
            errorMessage = "用户名或密码错误，你还有2次机会，否则账户将被锁定30分钟";
        }
        if("passwordInvalidFourTimes".equals(failure)){
            errorMessage = "用户名或密码错误，你还有1次机会，否则账户将被锁定30分钟";
        }
        if("passwordInvalidFiveTimes".equals(failure)){
            errorMessage = "用户名或密码错误，次数超过5次，账户将被锁定30分钟";
        }
        view.addObject("errorMessage",errorMessage);
        return view;
    }


    /**
     * 自定义了系统退出，目的为了支持get提交
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/security_login",method = RequestMethod.GET)
    public String securityLogout(){
        return login();
    }


}
