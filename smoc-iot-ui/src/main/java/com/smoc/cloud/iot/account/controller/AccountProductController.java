package com.smoc.cloud.iot.account.controller;


import com.smoc.cloud.common.page.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/iot/account/product")
public class AccountProductController {

    /**
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("account/account_product_list");
        //初始化数据
        PageParams params = new PageParams();
        params.setTotalRows(86);
        params.setPageSize(9);
        params.setStartRow(1);
        params.setPageSize(10);
        params.setCurrentPage(1);
        params.setPages(9);
        view.addObject("pageParams", params);
        return view;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("account/account_product_list");
        //初始化数据
        PageParams params = new PageParams();
        params.setTotalRows(86);
        params.setPageSize(9);
        params.setStartRow(1);
        params.setPageSize(10);
        params.setCurrentPage(1);
        params.setPages(9);
        view.addObject("pageParams", params);
        return view;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add() {
        ModelAndView view = new ModelAndView("account/account_product_edit");
        return view;
    }


    /**
     * @return
     */
    @RequestMapping(value = "/center", method = RequestMethod.GET)
    public ModelAndView center() {
        ModelAndView view = new ModelAndView("account/account_product_view_center");
        return view;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView view() {
        ModelAndView view = new ModelAndView("account/account_product_view");
        return view;
    }
}
