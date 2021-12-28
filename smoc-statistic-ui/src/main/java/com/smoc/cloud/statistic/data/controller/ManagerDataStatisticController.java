package com.smoc.cloud.statistic.data.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
/**
 * 运营数据统计分析
 */
@Slf4j
@Controller
@RequestMapping("/data/statistic")
public class ManagerDataStatisticController {


    /**
     * 运营数据综合月统计
     * @return
     */
    @RequestMapping(value = "/manager/month", method = RequestMethod.GET)
    public ModelAndView manager_month() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/data/manager_data_statistic_month");

        return view;

    }

    /**
     * 运营净毛利统计
     * @return
     */
    @RequestMapping(value = "/manager/income", method = RequestMethod.GET)
    public ModelAndView income() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/data/manager_data_statistic_income");

        return view;

    }



}
