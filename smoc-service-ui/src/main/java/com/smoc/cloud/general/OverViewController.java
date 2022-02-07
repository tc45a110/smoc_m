package com.smoc.cloud.general;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
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
     * 行业短信概览
     *
     * @return
     */
    @RequestMapping(value = "/industry/overview", method = RequestMethod.GET)
    public ModelAndView industry(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");
        return view;
    }

    /**
     * 会销短信概览
     *
     * @return
     */
    @RequestMapping(value = "/marketing/overview", method = RequestMethod.GET)
    public ModelAndView marketing( HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");
        return view;
    }

    /**
     * 拉新短信概览
     *
     * @return
     */
    @RequestMapping(value = "/innovations/overview", method = RequestMethod.GET)
    public ModelAndView innovations(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("index");
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
        return view;
    }
}
