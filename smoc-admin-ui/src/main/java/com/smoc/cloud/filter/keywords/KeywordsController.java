package com.smoc.cloud.filter.keywords;

import com.smoc.cloud.common.page.PageParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 关键词管理
 **/
@Controller
@RequestMapping("/filter/keywords")
public class KeywordsController {


    /**
     * 关键字管理列表
     *
     * @return
     */
    @RequestMapping(value = "/{keywordsType}/list", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String keywordsType,HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_list");

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
    @RequestMapping(value = "/{keywordsType}/page", method = RequestMethod.POST)
    public ModelAndView page(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_list");

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
    @RequestMapping(value = "/{keywordsType}/add", method = RequestMethod.GET)
    public ModelAndView add(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_edit_batch");

        return view;

    }

    /**
     * 编辑关键字
     *
     * @return
     */
    @RequestMapping(value = "/{keywordsType}/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_edit");

        return view;

    }

    /**
     * 关键字管理列表
     *
     * @return
     */
    @RequestMapping(value = "/{keywordsType}/list/{businessId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String keywordsType,@PathVariable String businessId,HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_list_common");

        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(10);
        params.setPageSize(1000);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(80);

        view.addObject("pageParams",params);
        view.addObject("keywordsType",keywordsType);
        view.addObject("businessId",businessId);

        return view;

    }

    /**
     * 添加关键字
     *
     * @return
     */
    @RequestMapping(value = "/{keywordsType}/add/{businessId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String keywordsType,@PathVariable String businessId,HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_edit_batch_common");

        return view;

    }

    /**
     * 编辑关键字
     *
     * @return
     */
    @RequestMapping(value = "/{keywordsType}/edit/{businessId}/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String keywordsType,@PathVariable String businessId,@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/keywords/keyword_edit_common");

        return view;

    }




}
