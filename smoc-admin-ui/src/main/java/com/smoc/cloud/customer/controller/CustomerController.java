package com.smoc.cloud.customer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@Controller
@RequestMapping("/ec")
public class CustomerController {

    /**
     * 查询EC列表
     *
     * @return
     */
    @RequestMapping(value = "/customer/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/customer_list");

        return view;

    }

    /**
     * 查看EC明细
     *
     * @return
     */
    @RequestMapping(value = "/customer/view/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String id) {
        ModelAndView view = new ModelAndView("customer/customer_view");

        return view;

    }

}
