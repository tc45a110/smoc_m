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
@RequestMapping("/ec")
public class ChannelAccountController {

    /**
     * 客户通道账号列表
     * @return
     */
    @RequestMapping(value = "/customer/channel/account/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/channel_account/channel_account_list");

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
    @RequestMapping(value = "/customer/channel/account/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("customer/channel_account/channel_account_list");

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
    @RequestMapping(value = "/customer/channel/account/search", method = RequestMethod.POST)
    public ModelAndView search() {
        ModelAndView view = new ModelAndView("customer/channel_account/channel_account_search");

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
     * 客户通道账号配置
     * @return
     */
    @RequestMapping(value = "/customer/channel/account/config/{type}", method = RequestMethod.GET)
    public ModelAndView config(@PathVariable String type, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/channel_account/channel_account_config");
        view.addObject("type",type);
        view.addObject("url","/ec/customer/channel/account/edit/"+type+"/id");
        if("view".equals(type)){
            view.addObject("url","/ec/customer/channel/account/view/detail/id");
        }

        return view;

    }


    /**
     * 客户通道账号中心
     * @return
     */
    @RequestMapping(value = "/customer/channel/account/center/{type}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String type, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/channel_account/channel_account_"+type+"_center");

        return view;

    }

    /**
     * 客户通道账号明细
     * @return
     */
    @RequestMapping(value = "/customer/channel/account/view/{type}/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String type,@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/channel_account/channel_account_view");

        return view;

    }

    /**
     * 客户通道账号中心
     * @return
     */
    @RequestMapping(value = "/customer/channel/account/edit/{type}/{id}", method = RequestMethod.GET)
    public ModelAndView baseEdit(@PathVariable String type,@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/channel_account/channel_account_edit_"+type);
        return view;

    }

    /**
     * 客户通道账号操作记录
     * @return
     */
    @RequestMapping(value = "/customer/channel/account/operate/record", method = RequestMethod.GET)
    public ModelAndView record() {
        ModelAndView view = new ModelAndView("customer/channel_account/channel_account_operate_record");

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
     * 客户通道账号操作记录分页
     * @return
     */
    @RequestMapping(value = "/customer/channel/account/operate/page", method = RequestMethod.POST)
    public ModelAndView record_page() {
        ModelAndView view = new ModelAndView("customer/channel_account/channel_account_operate_record");

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
