package com.smoc.cloud.configure.channel.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelPriceHistoryValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.configure.channel.service.ConfigChannelPriceHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;


/**
 * 通道价格历史
 */
@Slf4j
@Controller
@RequestMapping("/configure/channel/price/history")
public class ConfigChannelPriceHistoryController {

    @Autowired
    private ConfigChannelPriceHistoryService channelPriceHistoryService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{channelId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String channelId) {
        ModelAndView view = new ModelAndView("configure/channel/channel_price_history_list");

        //初始化数据
        PageParams<ConfigChannelPriceHistoryValidator> params = new PageParams<ConfigChannelPriceHistoryValidator>();
        params.setPageSize(31);
        params.setCurrentPage(1);
        ConfigChannelPriceHistoryValidator configChannelPriceHistoryValidator = new ConfigChannelPriceHistoryValidator();
        configChannelPriceHistoryValidator.setChannelId(channelId);
        //获取当前日期
        String today = DateTimeUtils.getDateFormat(new Date());
        String endDate = DateTimeUtils.checkOption(today, -31);
        configChannelPriceHistoryValidator.setStartDate(today);
        configChannelPriceHistoryValidator.setEndDate(endDate);
        params.setParams(configChannelPriceHistoryValidator);

        //查询
        ResponseData<PageList<ConfigChannelPriceHistoryValidator>> data = channelPriceHistoryService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("configChannelPriceHistoryValidator", configChannelPriceHistoryValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     * 分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ConfigChannelPriceHistoryValidator configChannelPriceHistoryValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/channel/channel_price_history_list");
        //分页查询
        pageParams.setParams(configChannelPriceHistoryValidator);

        ResponseData<PageList<ConfigChannelPriceHistoryValidator>> data = channelPriceHistoryService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("configChannelPriceHistoryValidator", configChannelPriceHistoryValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }
}
