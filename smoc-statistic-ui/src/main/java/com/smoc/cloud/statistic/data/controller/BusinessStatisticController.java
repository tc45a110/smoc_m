package com.smoc.cloud.statistic.data.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;

/**
 * 业务列别统计
 */
@Slf4j
@Controller
@RequestMapping("/statistic/business")
public class BusinessStatisticController {

    /**
     * 业务账号统计分析
     * @return
     */
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ModelAndView account() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/business/business_statistic_account");

        return view;

    }

    /**
     * 企业统计分析
     * @return
     */
    @RequestMapping(value = "/enterprise", method = RequestMethod.GET)
    public ModelAndView enterprise() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/business/business_statistic_enterprise");

        return view;

    }

    /**
     * 通道统计分析
     * @return
     */
    @RequestMapping(value = "/channel", method = RequestMethod.GET)
    public ModelAndView channel() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/business/business_statistic_channel");

        return view;

    }



}
