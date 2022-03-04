package com.smoc.cloud.material.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 各种类型的概览
 */
@Slf4j
@RestController
public class OverViewController {

    /**
     * 普通短信概览
     *
     * @return
     */
    @RequestMapping(value = "/textsms/overview", method = RequestMethod.GET)
    public ModelAndView textsms(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");

        view.addObject("businessTypeName", "普通短信");
        return view;
    }

    /**
     * 多媒体短信概览
     *
     * @return
     */
    @RequestMapping(value = "/multisms/overview", method = RequestMethod.GET)
    public ModelAndView multisms( HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");
        view.addObject("businessTypeName", "多媒体短信");
        return view;
    }

    /**
     * 彩信短信概览
     *
     * @return
     */
    @RequestMapping(value = "/mms/overview", method = RequestMethod.GET)
    public ModelAndView mms(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");
        view.addObject("businessTypeName", "彩信");
        return view;
    }

    /**
     * 5G短信概览
     *
     * @return
     */
    @RequestMapping(value = "/sms5g/overview", method = RequestMethod.GET)
    public ModelAndView sms5g(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");
        view.addObject("businessTypeName", "5G短信");
        return view;
    }

    /**
     * 国际短信概览
     *
     * @return
     */
    @RequestMapping(value = "/international/overview", method = RequestMethod.GET)
    public ModelAndView international(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");
        view.addObject("businessTypeName", "国际短信");
        return view;
    }

    /**
     * 过黑服务概览
     *
     * @return
     */
    @RequestMapping(value = "/blackservice/overview", method = RequestMethod.GET)
    public ModelAndView blackservice(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");
        view.addObject("businessTypeName", "过黑服务");
        return view;
    }
}
