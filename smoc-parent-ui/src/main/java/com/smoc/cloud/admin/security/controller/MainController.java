package com.smoc.cloud.admin.security.controller;

import com.smoc.cloud.admin.oauth2.service.OauthTokenService;
import com.smoc.cloud.admin.security.properties.SystemProperties;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Nodes;
import com.smoc.cloud.common.auth.validator.SystemValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统入口
 * 2019/4/17 21:15
 **/
@Slf4j
@Controller
public class MainController {

    @Autowired
    private OauthTokenService oauthTokenService;

    @Autowired
    private SystemProperties systemProperties;

    /**
     * 系统入口
     *
     * @return
     */
    @RequestMapping("/main")
    public ModelAndView main(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("main");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        Map<String, SystemValidator> sysMap = (Map<String, SystemValidator>) request.getServletContext().getAttribute("sysMap");

        //加载菜单数据
        ResponseData<Nodes[]> data = oauthTokenService.getUserMenus(systemProperties.getSystemMarking(), user.getId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("menus", data);

        List<SystemValidator> list = new ArrayList();
        String[] scopes = user.getAuthScope().split(",");
        for (int i = 0; i < scopes.length; i++) {
            if (!systemProperties.getSystemMarking().equals(scopes[i])) {
                list.add(sysMap.get(scopes[i]));
            }
        }
        view.addObject("sysList", list);

        //引导页显示
        String guideCount = ""+request.getSession().getAttribute("guideCount");
        if(!StringUtils.isEmpty(guideCount) && !"null".equals(guideCount)){
            view.addObject("guideCount","1");
            request.getSession().setAttribute("guideCount", "");
        }

        return view;
    }
}
