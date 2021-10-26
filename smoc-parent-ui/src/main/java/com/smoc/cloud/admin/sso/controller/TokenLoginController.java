package com.smoc.cloud.admin.sso.controller;


import com.smoc.cloud.admin.security.remote.service.UserCacheService;
import com.smoc.cloud.admin.utils.SecurityMenus;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.SystemValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * token 登录处理
 */
@Slf4j
@Controller
@RequestMapping("/token")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class TokenLoginController {

    @Autowired
    private UserCacheService userCacheService;

    /**
     * sso 跳转
     * @return
     */
    @RequestMapping(value = "/login/{projectId}", method = RequestMethod.GET)
    public ModelAndView login(@PathVariable String projectId, HttpServletRequest request) {

        //验证projectId
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //验证projectId 是否合法
        if (user.getAuthScope().indexOf(projectId) < 0) {
            ModelAndView view = new ModelAndView("denied");
            view.addObject("error", "无该功能的访问权限！");
            return view;
        }

        //去用户登录信息
        String token = (String) request.getSession().getAttribute("token");
        String key = SecurityMenus.getStringRandom(20).toUpperCase();

        //缓存token
        ResponseData data = userCacheService.cacheSSOToekn(key, token);
        if(!ResponseCode.SUCCESS.getCode().equals(data.getCode())){
            ModelAndView view = new ModelAndView("denied");
            view.addObject("error", "功能出错，请联系管理员！");
            return view;
        }

        //获取跳转url
        Map<String, SystemValidator> sysMap = (Map<String, SystemValidator>) request.getServletContext().getAttribute("sysMap");
        String url = sysMap.get(projectId).getUrl();

        return new ModelAndView("redirect:" + url + "/tokenLogin?token=" + key);
    }

}
