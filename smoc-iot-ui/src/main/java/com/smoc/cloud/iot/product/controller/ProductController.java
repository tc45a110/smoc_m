package com.smoc.cloud.iot.product.controller;


import com.smoc.cloud.common.page.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 物联网卡产品管理
 */
@Slf4j
@Controller
@RequestMapping("/iot/product")
public class ProductController {

    /**
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("product/product_list");
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
        ModelAndView view = new ModelAndView("product/product_list");
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
        ModelAndView view = new ModelAndView("product/product_edit");
        return view;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public ModelAndView config() {
        ModelAndView view = new ModelAndView("product/product_config");
        return view;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/center", method = RequestMethod.GET)
    public ModelAndView center() {
        ModelAndView view = new ModelAndView("product/product_view_center");
        return view;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView view() {
        ModelAndView view = new ModelAndView("product/product_view");
        return view;
    }

}
