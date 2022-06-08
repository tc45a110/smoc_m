package com.smoc.cloud.configure.channel.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairValidator;
import com.smoc.cloud.configure.channel.service.ChannelRepairService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 通道补发管理
 **/
@Slf4j
@RestController
@RequestMapping("/configure/channel/repair")
public class ChannelRepairController {

    @Autowired
    private ChannelRepairService channelRepairService;

    /**
     * 通道补发管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/repair/channel_repair_list");

        ///初始化数据
        PageParams<ConfigChannelRepairValidator> params = new PageParams<ConfigChannelRepairValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        ConfigChannelRepairValidator configChannelRepairValidator = new ConfigChannelRepairValidator();
        params.setParams(configChannelRepairValidator);

        //查询
        ResponseData<PageList<ConfigChannelRepairValidator>> data = channelRepairService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("configChannelRepairValidator", configChannelRepairValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 通道补发分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ConfigChannelRepairValidator configChannelRepairValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/channel/repair/channel_repair_list");

        //分页查询
        pageParams.setParams(configChannelRepairValidator);

        ResponseData<PageList<ConfigChannelRepairValidator>> data = channelRepairService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("configChannelRepairValidator", configChannelRepairValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

}
