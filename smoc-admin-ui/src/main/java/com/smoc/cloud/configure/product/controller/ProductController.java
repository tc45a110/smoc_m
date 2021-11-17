package com.smoc.cloud.configure.product.controller;

import com.smoc.cloud.common.page.PageParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 产品管理
 **/
@Controller
@RequestMapping("/configure/product")
public class ProductController {



    /**
     * 产品管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView userProfile(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("/configure/product/product_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(10);
        params.setPageSize(8);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(80);

        view.addObject("pageParams",params);

        return view;

    }

    /**
     * 产品分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("/configure/product/product_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(10);
        params.setPageSize(8);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(80);

        view.addObject("pageParams",params);

        return view;

    }

    /**
     * 显示产品配置
     *
     * @return
     */
    @RequestMapping(value = "/config/{id}", method = RequestMethod.GET)
    public ModelAndView config(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("/configure/product/product_config");

        return view;

    }

    /**
     * 添加产品
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("/configure/product/product_add");

        return view;

    }
    /**
     * 编辑产品
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("/configure/product/product_edit");

        return view;

    }

    /**
     * 通道配置
     *
     * @return
     */
    @RequestMapping(value = "/channel/list/{id}", method = RequestMethod.GET)
    public ModelAndView channelList(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("/configure/product/product_config_channel");

        return view;

    }

    /**
     * 通道配置查询
     *
     * @return
     */
    @RequestMapping(value = "/channel/page", method = RequestMethod.POST)
    public ModelAndView channelPage() {
        ModelAndView view = new ModelAndView("/configure/product/product_config_channel");

        return view;

    }

    /**
     * 过滤信息
     *
     * @return
     */
    @RequestMapping(value = "/filter/edit/{id}", method = RequestMethod.GET)
    public ModelAndView filterEdit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("/configure/product/product_config_filter");

        return view;

    }

    /**
     * 操作记录
     *
     * @return
     */
    @RequestMapping(value = "/operate/list/{id}", method = RequestMethod.GET)
    public ModelAndView operateList(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("/configure/product/product_config_operate");

        return view;

    }

}
