package com.smoc.cloud.configure.channel.controller;

import com.smoc.cloud.common.page.PageParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 通道管理
 **/
@Controller
@RequestMapping("/configure/channel")
public class ChannelController {



    /**
     * 通道管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView userProfile(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_list");

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
     * 通道分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("configure/channel/channel_list");

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
     * 添加通道
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_add");

        return view;

    }

    /**
     * 编辑通道
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_base");

        return view;

    }

    /**
     * 查看通道
     *
     * @return
     */
    @RequestMapping(value = "/config/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_config");

        return view;

    }

    /**
     * 接口信息
     *
     * @return
     */
    @RequestMapping(value = "/interface/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_interface");

        return view;

    }

    /**
     * 过滤信息
     *
     * @return
     */
    @RequestMapping(value = "/filter/{id}", method = RequestMethod.GET)
    public ModelAndView filter(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_filter");

        return view;

    }

    /**
     * 客户使用记录
     *
     * @return
     */
    @RequestMapping(value = "/customerRecode/{id}", method = RequestMethod.GET)
    public ModelAndView customerRecode(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_customer_recode");

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
     * 产品使用记录
     *
     * @return
     */
    @RequestMapping(value = "/productRecode/{id}", method = RequestMethod.GET)
    public ModelAndView productRecode(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_product_recode");

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
     * 通道历史价格
     *
     * @return
     */
    @RequestMapping(value = "/channelPriceList/{id}", method = RequestMethod.GET)
    public ModelAndView channelPriceList(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_price_recode");

        return view;

    }

    /**
     * 通道商务费历史价格
     *
     * @return
     */
    @RequestMapping(value = "/channelBusinessFeesList/{id}", method = RequestMethod.GET)
    public ModelAndView channelBusinessFeesList(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_business_fees_recode");

        return view;

    }

    /**
     * 统计发送量
     *
     * @return
     */
    @RequestMapping(value = "/statisticsSendCount/{id}", method = RequestMethod.GET)
    public ModelAndView statisticsSendCount(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_statistics_send");

        return view;

    }
}
