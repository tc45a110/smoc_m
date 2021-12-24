package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.page.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * EC资料管理
 */
@Slf4j
@Controller
@RequestMapping("/ec/customer")
public class MaterialController {

    /**
     * 查询EC资料
     *
     * @return
     */
    @RequestMapping(value = "/material/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/material/customer_material_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(10);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(100);

        view.addObject("pageParams",params);

        return view;

    }

    /**
     * 分页查询EC资料
     *
     * @return
     */
    @RequestMapping(value = "/material/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("customer/material/customer_material_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(10);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(100);

        view.addObject("pageParams",params);
        return view;

    }

    /**
     * 添加
     * @return
     */
    @RequestMapping(value = "/material/edit/center/{id}", method = RequestMethod.GET)
    public ModelAndView add() {

        ModelAndView view = new ModelAndView("customer/material/customer_material_edit_center");

        return view;

    }

    /**
     * 编辑
     * @return
     */
    @RequestMapping(value = "/material/edit/base/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/material/customer_material_edit_base");

        return view;

    }

    /**
     * 合同链列表
     * @return
     */
    @RequestMapping(value = "/material/chain/list/{id}", method = RequestMethod.GET)
    public ModelAndView chainList(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/material/customer_material_chain_list");

        return view;

    }

    /**
     * 新建合同链
     * @return
     */
    @RequestMapping(value = "/material/chain/add/{id}", method = RequestMethod.GET)
    public ModelAndView addChain(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/material/customer_material_chain_edit");

        return view;

    }

    /**
     * 编辑合同链
     * @return
     */
    @RequestMapping(value = "/material/chain/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editChain(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/material/customer_material_chain_edit");

        return view;

    }

    /**
     * 显示详情
     * @return
     */
    @RequestMapping(value = "/material/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/material/customer_material_detail");

        return view;

    }

    /**
     * EC中心查询EC资料
     *
     * @return
     */
    @RequestMapping(value = "/center/material/list", method = RequestMethod.GET)
    public ModelAndView customer_data_list() {
        ModelAndView view = new ModelAndView("customer/material/customer_center_material_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(10);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(100);

        view.addObject("pageParams",params);
        view.addObject("type","contract");

        return view;

    }

    /**
     * 分页查询EC资料
     *
     * @return
     */
    @RequestMapping(value = "/center/material/page", method = RequestMethod.POST)
    public ModelAndView customer_data_page() {
        ModelAndView view = new ModelAndView("customer/material/customer_center_material_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(10);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(100);

        view.addObject("pageParams",params);
        view.addObject("type","contract");
        return view;

    }



}
