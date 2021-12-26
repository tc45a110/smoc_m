package com.smoc.cloud.finance.controller;

import com.smoc.cloud.common.page.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 财务共享账户
 */
@Slf4j
@Controller
@RequestMapping("/finance")
public class FinanceShareAccountController {

    /**
     * 财务共享账户列表
     *
     * @return
     */
    @RequestMapping(value = "/account/share/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("finance/finance_account_share_list");

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
     * 财务共享账户分页
     *
     * @return
     */
    @RequestMapping(value = "/account/share/page", method = RequestMethod.POST)
    public ModelAndView page() {
        ModelAndView view = new ModelAndView("finance/finance_account_share_list");

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
     * 财务共享账户编辑
     *
     * @return
     */
    @RequestMapping(value = "/account/share/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_edit");

        return view;
    }

    /**
     * 财务共享账户查看中心
     *
     * @return
     */
    @RequestMapping(value = "/account/share/view/center/*", method = RequestMethod.GET)
    public ModelAndView center() {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_center");

        return view;
    }

    /**
     * 财务共享账户基本信息明细
     *
     * @return
     */
    @RequestMapping(value = "/account/share/view/base/{id}", method = RequestMethod.GET)
    public ModelAndView view_base(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_base");

        return view;
    }

    /**
     * 财务共享账户充值列表
     *
     * @return
     */
    @RequestMapping(value = "/account/share/view/recharge/{id}", method = RequestMethod.GET)
    public ModelAndView view_recharge(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_recharge_list");
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
     * 财务共享账户消费列表
     *
     * @return
     */
    @RequestMapping(value = "/account/share/view/consume/{id}", method = RequestMethod.GET)
    public ModelAndView view_consume(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("finance/finance_account_share_view_consume_list");
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
