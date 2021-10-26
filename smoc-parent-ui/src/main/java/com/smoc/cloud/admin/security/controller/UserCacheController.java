package com.smoc.cloud.admin.security.controller;

import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.admin.security.properties.SystemProperties;
import com.smoc.cloud.admin.security.remote.service.UserCacheService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.SystemValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户缓存重新加载
 * 2019/5/16 12:47
 **/
@Slf4j
@Controller
@RequestMapping("/userCache")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class UserCacheController {

    @Autowired
    private SystemProperties systemProperties;

    @Autowired
    private UserCacheService userCacheService;

    @Autowired
    private OauthTokenService oauthTokenService;

    /**
     * 清除用户缓存  缓存信息包括 用户信息、角色信息、菜单信息
     *
     * @return
     */
    @RequestMapping(value = "/clearUserCache", method = RequestMethod.GET)
    public ModelAndView clearUserCache(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("sys_user/user_cache_clear");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ResponseData data = userCacheService.clearUserCache(systemProperties.getSystemMarking(), user.getId(), user.getUserName());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", "用户缓存清除失败！");
            return view;
        }


        log.info("[系统数据更新][数据初始化]数据:重新加载系统SSO列表数据");
        Map<String, SystemValidator> sysMap = oauthTokenService.getSystemList();
        request.getServletContext().setAttribute("sysMap", sysMap);


        view.addObject("success", "0000:用户缓存清除成功！");

        return view;
    }
}
