package com.smoc.cloud.statistic.data.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;

/**
 * 投诉统计
 */
@Slf4j
@Controller
@RequestMapping("/data/statistic")
public class ComplaintStatisticController {

    /**
     * 投诉数据全国分布
     * @return
     */
    @RequestMapping(value = "/complaint/map/china", method = RequestMethod.GET)
    public ModelAndView manager_china() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/data/complaint_data_statistic_china");

        return view;

    }

    /**
     * 投诉 运营商分布
     * @return
     */
    @RequestMapping(value = "/complaint/carrier", method = RequestMethod.GET)
    public ModelAndView manager_carrier() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/data/complaint_data_statistic_carrier");

        return view;

    }

    /**
     * 投诉 通道分布
     * @return
     */
    @RequestMapping(value = "/complaint/channel", method = RequestMethod.GET)
    public ModelAndView manager_channel() throws ParseException {
        ModelAndView view = new ModelAndView("statistic/data/complaint_data_statistic_channel");

        return view;

    }
}
