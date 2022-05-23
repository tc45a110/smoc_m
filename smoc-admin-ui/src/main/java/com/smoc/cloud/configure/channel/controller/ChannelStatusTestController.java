package com.smoc.cloud.configure.channel.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.configure.channel.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 通道状态测试
 **/
@Slf4j
@RestController
@RequestMapping("/configure/channel")
public class ChannelStatusTestController {

    @Autowired
    private ChannelService channelService;

    /**
     * 通道状态测试
     *
     * @return
     */
    @RequestMapping(value = "/test/{id}", method = RequestMethod.GET)
    public ModelAndView page(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("configure/channel/channel_status_test");

        //查询通道信息
        ResponseData<ChannelBasicInfoValidator> data = channelService.findChannelById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        view.addObject("channelBasicInfoValidator", data.getData());

        return view;
    }
}
