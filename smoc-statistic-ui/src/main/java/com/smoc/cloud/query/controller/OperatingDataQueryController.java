package com.smoc.cloud.query.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;

/**
 * 运营数据查询
 */
@Slf4j
@Controller
@RequestMapping("/query/operating")
public class OperatingDataQueryController {

    /**
     * 运营综合年查询
     * @return
     */
    @RequestMapping(value = "/month", method = RequestMethod.GET)
    public ModelAndView year() throws ParseException {
        ModelAndView view = new ModelAndView("query/operating/operating_statistic_month");

        return view;

    }
}
