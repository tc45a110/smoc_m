package com.smoc.cloud.admin.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 首页
 * 2019/4/18 12:22
 **/
@Controller
public class ErrorController {

    /**
     * 无权访问
     *
     * @return
     */
    @RequestMapping("/denied")
    public ModelAndView denied() {

        ModelAndView view = new ModelAndView("denied");
        view.addObject("error","无该功能的访问权限！");
        return view;
    }
}
