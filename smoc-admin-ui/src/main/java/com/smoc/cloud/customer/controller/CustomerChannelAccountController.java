package com.smoc.cloud.customer.controller;


import com.smoc.cloud.common.page.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/ec")
public class CustomerChannelAccountController {

    /**
     * 客户通道账号列表
     * @return
     */
    @RequestMapping(value = "/customer/channel/account/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/channel_account/customer_channel_account_list");

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
        ModelAndView view = new ModelAndView("customer/channel_account/customer_channel_account_list");

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
    @RequestMapping(value = "/customer/channel/account/search", method = RequestMethod.POST)
    public ModelAndView search() {
        ModelAndView view = new ModelAndView("customer/channel_account/customer_channel_account_search");

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
     * 客户通道账号添加
     * @return
     */
    @RequestMapping(value = "/customer/channel/account/add", method = RequestMethod.GET)
    public ModelAndView add() {
        ModelAndView view = new ModelAndView("customer/channel_account/customer_channel_account_edit");

        return view;

    }

    /**
     * 客户通道账号添加
     * @return
     */
    @RequestMapping(value = "/customer/channel/account/account_config", method = RequestMethod.GET)
    public ModelAndView edit() {
        ModelAndView view = new ModelAndView("customer/channel_account/customer_channel_account_edit_config");

        return view;

    }


    /**
     * 客户通道账号配置
     * @return
     */
    @RequestMapping(value = "/customer/channel/account/config", method = RequestMethod.GET)
    public ModelAndView config() {
        ModelAndView view = new ModelAndView("customer/channel_account/customer_channel_account_config");

        return view;

    }
}
