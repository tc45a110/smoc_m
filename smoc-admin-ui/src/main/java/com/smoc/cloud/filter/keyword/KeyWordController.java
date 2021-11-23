package com.smoc.cloud.filter.keyword;

import com.smoc.cloud.common.page.PageParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 关键字管理
 **/
@Controller
@RequestMapping("/filter/keyword")
public class KeyWordController {


    /**
     * 关键字管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keyword/keyword_list");

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
     * 关键字管理列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keyword/keyword_list");

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
     * 添加关键字
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keyword/keyword_edit");

        return view;

    }

    /**
     * 编辑关键字
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keyword/keyword_edit");

        return view;

    }

    /**
     * 导入关键字
     *
     * @return
     */
    @RequestMapping(value = "/upFilesView", method = RequestMethod.GET)
    public ModelAndView upFilesView(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keyword/keyword_upfiles_view");

        return view;

    }
}
