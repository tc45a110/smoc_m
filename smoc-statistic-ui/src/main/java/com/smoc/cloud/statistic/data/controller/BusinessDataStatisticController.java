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
@RequestMapping("/data/statistic")
public class BusinessDataStatisticController {

    /**
     * 运营数据综合月统计
     * @return
     */
    @RequestMapping(value = "/business/month", method = RequestMethod.GET)
    public ModelAndView manager_month() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/data/business_data_statistic_month");

        return view;

    }
}
