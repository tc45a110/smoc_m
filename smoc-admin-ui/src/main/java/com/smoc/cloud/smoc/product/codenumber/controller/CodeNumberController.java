package com.smoc.cloud.smoc.product.codenumber.controller;

import com.smoc.cloud.common.page.PageParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 码号管理
 **/
@Controller
@RequestMapping("/codenumber")
public class CodeNumberController {


    /**
     * 码号管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView userProfile(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("codenumber/codenumber_list");

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
     * 码号分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("codenumber/codenumber_list");

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
     * 码号添加
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("codenumber/codenumber_edit");

        return view;

    }

    /**
     * 码号编辑
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("codenumber/codenumber_edit");

        return view;

    }

    /**
     * 码号查看
     *
     * @return
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("codenumber/codenumber_view");

        return view;

    }

    /**
     * 码号详情
     *
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("codenumber/codenumber_detail");

        return view;

    }

    /**
     * 通道使用记录
     *
     * @return
     */
    @RequestMapping(value = "/channelRecode/{id}", method = RequestMethod.GET)
    public ModelAndView channelRecode(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("codenumber/codenumber_channel_recode");

        return view;

    }

    /**
     * 统计发送量
     *
     * @return
     */
    @RequestMapping(value = "/statisticsSendCount/{id}", method = RequestMethod.GET)
    public ModelAndView statisticsSendCount(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("codenumber/codenumber_statistics_send");

        return view;

    }
}
