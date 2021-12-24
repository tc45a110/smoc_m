package com.smoc.cloud.customer.controller;


import com.smoc.cloud.common.page.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/account")
public class AccountController {

    /**
     * 客户通道账号列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/account/account_list");

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
     * 客户通道账号列表
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("customer/account/account_list");

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
     * 查询EC列表
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView search() {
        ModelAndView view = new ModelAndView("customer/account/account_search");

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
     * EC业务账号配置中心
     * @return
     */
    @RequestMapping(value = "/edit/center/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_center(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_center");
        return view;

    }

    /**
     * 业务账号基本信息
     * @return
     */
    @RequestMapping(value = "/edit/base/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_base(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_base");
        return view;

    }

    /**
     * 业务账号财务配置
     * @return
     */
    @RequestMapping(value = "/edit/finance/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_finance(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_finance");
        return view;

    }

    /**
     * 业务账号接口配置
     * @return
     */
    @RequestMapping(value = "/edit/interface/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_interface(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_interface");
        return view;

    }

    /**
     * 业务账号产品配置
     * @return
     */
    @RequestMapping(value = "/edit/product/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_product(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel_group");
        return view;

    }
    /**
     * 业务账号通道配置
     * @return
     */
    @RequestMapping(value = "/edit/channel/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_channel(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel");
        return view;

    }


    /**
     * EC业务账号中心
     * @return
     */
    @RequestMapping(value = "/center/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String enterpriseId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_account_center");

        return view;

    }

    /**
     * EC业务账号查看中心
     * @return
     */
    @RequestMapping(value = "/view/center/{accountId}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_view_center");

        return view;

    }

    /**
     * EC业务账号查看中心
     * @return
     */
    @RequestMapping(value = "/view/base/{accountId}", method = RequestMethod.GET)
    public ModelAndView view_base(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_view_base");

        return view;

    }

    /**
     * EC业务账号通道明细
     * @return
     */
    @RequestMapping(value = "/view/channel/detail/{accountId}", method = RequestMethod.GET)
    public ModelAndView account_channel(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_view_channel_detail");

        return view;

    }

}
