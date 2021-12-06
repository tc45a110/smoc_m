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
@RequestMapping("/ec")
public class ResourceController {

    /**
     * 查询EC资料
     *
     * @return
     */
    @RequestMapping(value = "/resource/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/resource/resource_list");

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
    @RequestMapping(value = "/resource/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("customer/resource/resource_list");
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
    @RequestMapping(value = "/resource/add", method = RequestMethod.GET)
    public ModelAndView add() {

        ModelAndView view = new ModelAndView("customer/resource/resource_edit");

        return view;

    }

    /**
     * 编辑
     * @return
     */
    @RequestMapping(value = "/resource/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/resource/resource_edit");

        return view;

    }

    /**
     * 显示详情
     * @return
     */
    @RequestMapping(value = "/resource/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/resource/resource_detail");

        return view;

    }

    /**
     * 查询EC资料审核
     *
     * @return
     */
    @RequestMapping(value = "/resource/checkList", method = RequestMethod.GET)
    public ModelAndView checkList() {
        ModelAndView view = new ModelAndView("customer/resource/resource_check_list");

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
     * 分页查询EC资料审核
     *
     * @return
     */
    @RequestMapping(value = "/resource/checkPage", method = RequestMethod.POST)
    public ModelAndView checkPage() {
        ModelAndView view = new ModelAndView("customer/resource/resource_check_list");
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
     * 显示审核页面
     * @return
     */
    @RequestMapping(value = "/resource/checkView/{id}", method = RequestMethod.GET)
    public ModelAndView checkView(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/resource/resource_check_view");

        return view;

    }

    /**
     * 显示审核详情
     * @return
     */
    @RequestMapping(value = "/resource/checkDetail/{id}", method = RequestMethod.GET)
    public ModelAndView checkDetail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/resource/resource_check_detail");

        return view;

    }

}
