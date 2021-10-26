package com.smoc.cloud.admin.security.controller;

import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.admin.security.properties.SystemProperties;
import com.smoc.cloud.common.auth.qo.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 字典缓存 重新加载
 * 2019/5/16 12:47
 **/
@Slf4j
@Controller
@RequestMapping("/dictCache")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class DictCacheController {

    @Autowired
    private SystemProperties systemProperties;

    @Autowired
    private OauthTokenService oauthTokenService;

    @RequestMapping(value = "/reloadDictCache", method = RequestMethod.GET)
    public ModelAndView systemAuthCache() {
        ModelAndView view = new ModelAndView("sys_user/dict_cache_reload");
        return view;
    }

    /**
     * 清除用户缓存  缓存信息包括 用户信息、角色信息、菜单信息
     *
     * @return
     */
    @RequestMapping(value = "/updateDictCache", method = RequestMethod.POST)
    public ModelAndView clearUserCache(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("sys_user/dict_cache_reload");

        /**
         * 加载系统字典数据
         */
        log.info("[系统启动][数据初始化]数据:重新加载系统公用字典数据");
        Map<String, List<Dict>> dict = oauthTokenService.loadSysDict(systemProperties.getSystemMarking());
        request.getServletContext().setAttribute("dict", dict);

        view.addObject("success", "0000:更新字典缓存成功！");

        return view;
    }
}
