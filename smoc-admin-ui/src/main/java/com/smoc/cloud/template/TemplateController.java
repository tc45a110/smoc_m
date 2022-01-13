package com.smoc.cloud.template;


import com.smoc.cloud.common.page.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 模板管理
 */
@Slf4j
@Controller
@RequestMapping("/template")
public class TemplateController {

    /**
     * 模板管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("templates/template_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams", params);

        return view;
    }

    /**
     * 模板管理分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("templates/template_list");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams", params);

        return view;
    }

    /**
     * 添加模板
     *
     * @return
     */
    @RequestMapping(value = "/add/{businessAccountId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String businessAccountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("templates/template_edit");

        return view;
    }

    /**
     * 同业务账号新建模板
     *
     * @return
     */
    @RequestMapping(value = "/copy/{businessAccountId}", method = RequestMethod.GET)
    public ModelAndView copy(@PathVariable String businessAccountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("templates/template_edit");

        return view;
    }

    /**
     * 修改模板
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("templates/template_edit");

        return view;
    }

    /**
     * 模板详细中心
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{id}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("templates/template_view_center");

        return view;
    }

    /**
     * 模板详细
     *
     * @return
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("templates/template_view");

        return view;
    }
}
