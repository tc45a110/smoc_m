package com.smoc.cloud.configure.product.controller;

import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.page.PageParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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

        ModelAndView view = new ModelAndView("configure/product/product_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(10);
        params.setPageSize(8);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(80);

        view.addObject("pageParams", params);

        return view;
    }

    /**
     * 产品分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("configure/product/product_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(10);
        params.setPageSize(8);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(80);

        view.addObject("pageParams", params);

        return view;

    }

    /**
     * 产品维护中心
     *
     * @return
     */
    @RequestMapping(value = "/edit/center/{id}", method = RequestMethod.GET)
    public ModelAndView edit_center(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/product/product_edit_center");

        return view;

    }

    /**
     * 产品基本信息维护
     *
     * @return
     */
    @RequestMapping(value = "/edit/base/{id}", method = RequestMethod.GET)
    public ModelAndView base(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/product/product_edit_base");

        Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>) request.getSession().getServletContext().getAttribute("dict");
        List<Dict> dictList = dictMap.get("productExtendField");

        view.addObject("productExtendField",dictList);

        return view;

    }

    /**
     * 产品通道配置
     *
     * @return
     */
    @RequestMapping(value = "/edit/channel/{id}", method = RequestMethod.GET)
    public ModelAndView channelList(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/product/product_edit_channel");

        return view;

    }

    /**
     * 产品详细中心
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{id}", method = RequestMethod.GET)
    public ModelAndView view_center(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/product/product_view_center");

        return view;

    }

    /**
     * 产品基本详细
     *
     * @return
     */
    @RequestMapping(value = "/view/base/{id}", method = RequestMethod.GET)
    public ModelAndView view_base(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/product/product_view_base");


        Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>) request.getSession().getServletContext().getAttribute("dict");
        List<Dict> dictList = dictMap.get("productExtendField");

        view.addObject("productExtendField",dictList);

        return view;

    }


}
