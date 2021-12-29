package com.smoc.cloud.statistic.data.controller;


import com.google.gson.Gson;
import com.smoc.cloud.common.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.Date;
import java.util.Random;

/**
 * 运营数据统计
 */
@Slf4j
@Controller
@RequestMapping("/statistic/operating")
public class OperatingStatisticController {

    /**
     * 运营综合日统计
     * @return
     */
    @RequestMapping(value = "/daily", method = RequestMethod.GET)
    public ModelAndView manager_daily() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/operating/operating_statistic_daily");

        return view;

    }

    /**
     * 运营净毛利统计
     * @return
     */
    @RequestMapping(value = "/income", method = RequestMethod.GET)
    public ModelAndView income() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/operating/operating_statistic_income");

        return view;

    }


}
