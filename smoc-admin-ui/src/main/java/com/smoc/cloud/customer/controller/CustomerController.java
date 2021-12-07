package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.page.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@Controller
@RequestMapping("/ec/customer")
public class CustomerController {

    /**
     * 查询EC列表
     *
     * @return
     */
    @RequestMapping(value = "/base_info/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/base_info/customer_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(12);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(115);

        view.addObject("pageParams",params);

        return view;

    }

    /**
     * 分页查询EC
     *
     * @return
     */
    @RequestMapping(value = "/base_info/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("customer/base_info/customer_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(12);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(115);

        view.addObject("pageParams",params);
        return view;

    }

    /**
     * EC开户
     * level表示企业层级
     * @return
     */
    @RequestMapping(value = "/base_info/edit/{level}/{parentId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String level,@PathVariable String parentId) {
        ModelAndView view = new ModelAndView("customer/base_info/customer_edit");
        view.addObject("level",level);
        view.addObject("parentId",parentId);
        return view;

    }

    /**
     * EC开户
     * level表示企业层级
     * @return
     */
    @RequestMapping(value = "/base_info/center/{id}", method = RequestMethod.GET)
    public ModelAndView config(@PathVariable String id) {
        ModelAndView view = new ModelAndView("customer/base_info/customer_center");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams",params);

        if("0000".equals(id)){
            view.addObject("parentId","0");
        }else{
            view.addObject("parentId","1");
        }
        return view;

    }

    /**
     * 查看EC明细
     *
     * @return
     */
    @RequestMapping(value = "/base_info/view/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String id) {
        ModelAndView view = new ModelAndView("customer/base_info/customer_view");

        if("0000".equals(id)){
            view.addObject("parentId","0");
        }else{
            view.addObject("parentId","1");
        }

        return view;

    }

    /**
     * 用户产品查询
     *
     * @return
     */
    @RequestMapping(value = "/product/list", method = RequestMethod.GET)
    public ModelAndView product() {
        ModelAndView view = new ModelAndView("customer/product/customer_product_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams",params);

        return view;

    }

    /**
     * 用户产品分页查询
     *
     * @return
     */
    @RequestMapping(value = "/product/page", method = RequestMethod.POST)
    public ModelAndView product_page() {
        ModelAndView view = new ModelAndView("customer/product/customer_product_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams",params);

        return view;

    }

    /**
     * 用户通道查询
     *
     * @return
     */
    @RequestMapping(value = "/channel/list", method = RequestMethod.GET)
    public ModelAndView channel() {
        ModelAndView view = new ModelAndView("customer/channel/customer_channel_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams",params);

        return view;

    }

    /**
     * 用户通道分页查询
     *
     * @return
     */
    @RequestMapping(value = "/channel/page", method = RequestMethod.POST)
    public ModelAndView channel_page() {
        ModelAndView view = new ModelAndView("customer/channel/customer_channel_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams",params);

        return view;

    }





}
