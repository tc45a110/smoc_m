package com.smoc.cloud.saler.controller;

import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.ChannelStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.CustomerChannelInfoQo;
import com.smoc.cloud.saler.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 通道管理
 */
@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerChannelController {

    @Autowired
    private ChannelService channelService;

    /**
     * 客户通道列表
     *
     * @return
     */
    @RequestMapping(value = "/channel/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("channel/channel_customer_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<CustomerChannelInfoQo> params = new PageParams<CustomerChannelInfoQo>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        CustomerChannelInfoQo customerChannelInfoQo = new CustomerChannelInfoQo();
        customerChannelInfoQo.setSalerId(user.getId());
        params.setParams(customerChannelInfoQo);

        //查询
        ResponseData<PageList<CustomerChannelInfoQo>> data = channelService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("customerChannelInfoQo", customerChannelInfoQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 客户通道列表查询
     *
     * @return
     */
    @RequestMapping(value = "/channel/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute CustomerChannelInfoQo customerChannelInfoQo, PageParams pageParams,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("channel/channel_customer_list");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        customerChannelInfoQo.setSalerId(user.getId());
        pageParams.setParams(customerChannelInfoQo);

        ResponseData<PageList<CustomerChannelInfoQo>> data = channelService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("customerChannelInfoQo", customerChannelInfoQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     * 单个通道发送量统计
     *
     * @return
     */
    @RequestMapping(value = "/channel/statistic/messageSend/{channelId}", method = RequestMethod.GET)
    public ModelAndView statistic(@PathVariable String channelId,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("channel/channel_customer_statistic_send");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询通道信息
        ResponseData<ChannelBasicInfoValidator> data = channelService.findChannelById(channelId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //判断是否是本人客户
        if(!user.getId().equals(data.getData().getChannelAccessSales())){
            view.addObject("error", "无权限查看");
            return view;
        }

        view.addObject("channelId", channelId);

        return view;
    }

    /**
     * 单个通道发送量统计按月Ajax
     * @param channelId
     * @param request
     * @return
     */
    @RequestMapping(value = "/channel/statistic/statisticChannelSendMonth/{channelId}/{type}", method = RequestMethod.GET)
    public ChannelStatisticSendData statisticAccountSendMonth(@PathVariable String channelId, @PathVariable String type, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询通道信息
        ResponseData<ChannelBasicInfoValidator> data = channelService.findChannelById(channelId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return new ChannelStatisticSendData();
        }

        //判断是否是本人客户
        if(!user.getId().equals(data.getData().getChannelAccessSales())){
            return new ChannelStatisticSendData();
        }

        ChannelStatisticSendData statisticSendData = new ChannelStatisticSendData();
        statisticSendData.setChannelId(channelId);
        statisticSendData.setDimension(type);

        statisticSendData = channelService.statisticSendNumberMonthByChannel(statisticSendData);

        return statisticSendData;
    }

    /**
     * 通道发送量统计显示页面：根据通道查询
     *
     * @return
     */
    @RequestMapping(value = "/channel/statistic/statisticSendMonth", method = RequestMethod.GET)
    public ModelAndView statisticSendMonth(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("channel/channel_statistic_total_send");

        ChannelStatisticSendData channelStatisticSendData = new ChannelStatisticSendData();

        view.addObject("channelStatisticSendData", channelStatisticSendData);

        return view;
    }

    /**
     * 查询通道发送量统计显示页面：根据通道查询
     *
     * @return
     */
    @RequestMapping(value = "/channel/statistic/statisticSendQuery", method = RequestMethod.POST)
    public ModelAndView statisticSendQuery(@ModelAttribute ChannelStatisticSendData channelStatisticSendData,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("channel/channel_statistic_total_send");

        view.addObject("channelStatisticSendData", channelStatisticSendData);

        return view;
    }

    /**
     * 通道发送量统计按月：根据通道查询
     * @param statisticSendData
     * @param request
     * @return
     */
    @RequestMapping(value = "/channel/statistic/statisticSendMonthByChannel", method = RequestMethod.POST)
    public ChannelStatisticSendData statisticSendByAccount(@RequestBody ChannelStatisticSendData statisticSendData, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        statisticSendData.setSaler(user.getId());
        statisticSendData.setDimension("month");

        statisticSendData = channelService.statisticTotalSendNumberByChannel(statisticSendData);

        return statisticSendData;
    }

    /**
     * 通道发送量统计按天：根据通道查询
     * @param statisticSendData
     * @param request
     * @return
     */
    @RequestMapping(value = "/channel/statistic/statisticSendDayByChannel", method = RequestMethod.POST)
    public ChannelStatisticSendData statisticSendDayByName(@RequestBody ChannelStatisticSendData statisticSendData, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        statisticSendData.setSaler(user.getId());
        statisticSendData.setDimension("day");

        statisticSendData = channelService.statisticTotalSendNumberByChannel(statisticSendData);

        return statisticSendData;
    }
}
