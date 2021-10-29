package com.smoc.cloud.smoc.channel.controller;

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
@RequestMapping("/channel")
public class ChannelController {



    /**
     * 通道管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView userProfile(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("channel/channel_list");

        return view;

    }

    /**
     * 通道分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("channel/channel_list");

        return view;

    }

}
