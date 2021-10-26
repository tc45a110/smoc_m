package com.smoc.cloud.admin.security.controller;

import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.admin.security.filter.MpmSecurityMetadataSourceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统缓存重新加载
 * 2019/5/31 14:27
 **/
@Controller
@RequestMapping("/systemCache")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class SystemCacheController {

    @Autowired
    private OauthTokenService oauthTokenService;


    @RequestMapping(value = "/systemAuthCache", method = RequestMethod.GET)
    public ModelAndView systemAuthCache() {
        ModelAndView view = new ModelAndView("sys_user/user_cache_update");
        return view;
    }

    /**
     * 更新系统权限缓存
     *
     * @return
     */
    @RequestMapping(value = "/updateSystemCache", method = RequestMethod.POST)
    public ModelAndView updateSystemCache(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("sys_user/user_cache_update");
        try {
            MpmSecurityMetadataSourceFilter securityMetadataSourceFilter = new MpmSecurityMetadataSourceFilter(oauthTokenService);
        } catch (Exception e) {

        }

        view.addObject("success", "0000:更新系统权限成功！");
        return view;
    }
}
