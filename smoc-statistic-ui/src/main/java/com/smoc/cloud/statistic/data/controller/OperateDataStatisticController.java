package com.smoc.cloud.statistic.data.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 运营数据统计
 */
@Slf4j
@Controller
@RequestMapping("/data/statistic")
public class OperateDataStatisticController {

    /**
     * 运营数据统计
     * @return
     */
    @RequestMapping(value = "/operating", method = RequestMethod.GET)
    public ModelAndView add() {
        ModelAndView view = new ModelAndView("statistic/data/operating_data_statistic");
        return view;

    }
}
