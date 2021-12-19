package com.smoc.cloud.configure.search;

/**
 * 通道查询，更多的是其他模块 应用
 */

import com.smoc.cloud.common.page.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 业务账号通道挂起
 */
@Slf4j
@Controller
@RequestMapping("/channel/search")
public class ConfigureChannelSearchController {


    /**
     * 业务账号通道挂起 通道查询
     *
     * @return
     */
    @RequestMapping(value = "/suspend/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/search/channel_search_channel_suspend_list");
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
     * 业务账号通道挂起 通道查询分页
     *
     * @return
     */
    @RequestMapping(value = "/suspend/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("configure/search/channel_search_channel_suspend_list");
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
     * 业务账号特权通道 通道查询
     *
     * @return
     */
    @RequestMapping(value = "/privilege/list", method = RequestMethod.GET)
    public ModelAndView privilege(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/search/channel_search_channel_privilege_list");
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
     * 业务账号特权通道 通道查询分页
     *
     * @return
     */
    @RequestMapping(value = "/privilege/page", method = RequestMethod.POST)
    public ModelAndView privilege_page() {
        ModelAndView view = new ModelAndView("configure/search/channel_search_channel_privilege_list");
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
     * 业务账号普通通道 通道查询
     *
     * @return
     */
    @RequestMapping(value = "/normal/list", method = RequestMethod.GET)
    public ModelAndView normal(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/search/channel_search_channel_normal_list");
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
     * 业务账号普通通道 通道查询分页
     *
     * @return
     */
    @RequestMapping(value = "/normal/page", method = RequestMethod.POST)
    public ModelAndView normal_page() {
        ModelAndView view = new ModelAndView("configure/search/channel_search_channel_normal_list");
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
}
