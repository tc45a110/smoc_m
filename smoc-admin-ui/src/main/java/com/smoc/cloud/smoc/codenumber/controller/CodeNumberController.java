package com.smoc.cloud.smoc.codenumber.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 码号管理
 **/
@Controller
@RequestMapping("/codenumber")
public class CodeNumberController {


    /**
     * 码号管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView userProfile(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("codenumber/codenumber_list");

        return view;

    }

}
