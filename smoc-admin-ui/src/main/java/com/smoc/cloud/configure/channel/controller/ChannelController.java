package com.smoc.cloud.configure.channel.controller;

import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.page.PageParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
     * 通道维护中心
     *
     * @return
     */
    @RequestMapping(value = "/edit/center/{id}", method = RequestMethod.GET)
    public ModelAndView edit_center(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_center");

        return view;

    }

    /**
     * 通道基本信息编辑
     *
     * @return
     */
    @RequestMapping(value = "/edit/base/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_base");

        return view;

    }

    /**
     * 通道接口信息维护
     *
     * @return
     */
    @RequestMapping(value = "/edit/interface/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_interface");

        return view;

    }

    /**
     * 通道过滤信息维护
     *
     * @return
     */
    @RequestMapping(value = "/edit/filter/{id}", method = RequestMethod.GET)
    public ModelAndView filter(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_filter");

        return view;

    }

    /**
     * 计价设置
     *
     * @return
     */
    @RequestMapping(value = "/edit/price/{id}", method = RequestMethod.GET)
    public ModelAndView price(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_price");

        return view;

    }

    /**
     * 通道扩展参数
     *
     * @return
     */
    @RequestMapping(value = "/edit/extend/{id}", method = RequestMethod.GET)
    public ModelAndView extend(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_extend_param");

        Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>) request.getSession().getServletContext().getAttribute("dict");
        List<Dict> dictList = dictMap.get("channelExtendField");

        view.addObject("channelExtendFields",dictList);

        return view;

    }

    /**
     * 补发通道设置
     *
     * @return
     */
    @RequestMapping(value = "/edit/spare/{id}", method = RequestMethod.GET)
    public ModelAndView spare(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_edit_spare");

        return view;

    }

    /**
     * 通道详情中心
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{id}", method = RequestMethod.GET)
    public ModelAndView view_center(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_view_center");

        return view;

    }

    /**
     * 通道详情
     *
     * @return
     */
    @RequestMapping(value = "/view/base/{id}", method = RequestMethod.GET)
    public ModelAndView view_base(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_view_base");
        return view;

    }

}
