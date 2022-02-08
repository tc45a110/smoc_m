package com.smoc.cloud.material.controller;

import com.smoc.cloud.common.page.PageParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 短信模板
 */
@Controller
@RequestMapping("/template")
public class MessageTemplateController {

    /**
     * 短信模板列表
     * @param signType
     * @param request
     * @return
     */
    @RequestMapping(value = "list/{signType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String signType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("template/message_template_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams",params);
        view.addObject("signType", signType);
        return view;
    }

    /**
     *  短信模板分页
     * @param signType
     * @param request
     * @return
     */
    @RequestMapping(value = "page/{signType}", method = RequestMethod.POST)
    public ModelAndView page(@PathVariable String signType,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("template/message_template_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams",params);
        view.addObject("signType", signType);
        return view;
    }
}
