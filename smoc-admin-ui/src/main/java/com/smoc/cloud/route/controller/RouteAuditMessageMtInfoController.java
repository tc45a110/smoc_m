package com.smoc.cloud.route.controller;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.route.RouteAuditMessageMtInfoValidator;
import com.smoc.cloud.common.smoc.route.qo.RouteAuditMessageAccountQo;
import com.smoc.cloud.route.service.RouteAuditMessageMtInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * 待审批下发信息
 */
@Slf4j
@Controller
@RequestMapping("/route/message/mt/audit")
public class RouteAuditMessageMtInfoController {

    @Autowired
    private RouteAuditMessageMtInfoService routeAuditMessageMtInfoService;

    /**
     * 待审批下发列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("route/message/message_mt_audit_list");

        //初始化数据
        PageParams<RouteAuditMessageMtInfoValidator> params = new PageParams<RouteAuditMessageMtInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator = new RouteAuditMessageMtInfoValidator();
        params.setParams(routeAuditMessageMtInfoValidator);

        //查询
        ResponseData<PageList<RouteAuditMessageMtInfoValidator>> data = routeAuditMessageMtInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> count = routeAuditMessageMtInfoService.count(routeAuditMessageMtInfoValidator);

        view.addObject("routeAuditMessageMtInfoValidator", routeAuditMessageMtInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("count", count.getData());

        return view;
    }

    /**
     * 待审批下发分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("route/message/message_mt_audit_list");

        //分页查询
        pageParams.setParams(routeAuditMessageMtInfoValidator);

        ResponseData<PageList<RouteAuditMessageMtInfoValidator>> data = routeAuditMessageMtInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String, Object>> count = routeAuditMessageMtInfoService.count(routeAuditMessageMtInfoValidator);

        view.addObject("routeAuditMessageMtInfoValidator", routeAuditMessageMtInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("count", count.getData());

        return view;
    }

    /**
     * 审核页面
     *
     * @return
     */
    @RequestMapping(value = "/check/{type}/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String type, @PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("route/message/message_mt_audit_check_single");

        ResponseData<RouteAuditMessageMtInfoValidator> data = routeAuditMessageMtInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("routeAuditMessageMtInfoValidator",data.getData());
        view.addObject("type",type);

        //批量审核
        if("many".equals(type)){
            return checkManyMessage(data.getData());
        }

        return view;

    }

    //批量审核
    public ModelAndView checkManyMessage(RouteAuditMessageMtInfoValidator info) {
        ModelAndView view = new ModelAndView("route/message/message_mt_audit_check_many");

        //初始化数据
        PageParams<RouteAuditMessageMtInfoValidator> params = new PageParams<RouteAuditMessageMtInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        RouteAuditMessageMtInfoValidator queryMessageMtInfoValidator = new RouteAuditMessageMtInfoValidator();
        queryMessageMtInfoValidator.setAccountId(info.getAccountId());
        queryMessageMtInfoValidator.setInfoType(info.getInfoType());
        queryMessageMtInfoValidator.setChannelId(info.getChannelId());
        queryMessageMtInfoValidator.setMessageContent(info.getMessageContent());
        queryMessageMtInfoValidator.setId(info.getId());
        params.setParams(queryMessageMtInfoValidator);

        //查询
        ResponseData<PageList<RouteAuditMessageMtInfoValidator>> data = routeAuditMessageMtInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("routeAuditMessageMtInfoValidator", info);
        view.addObject("queryMessageMtInfoValidator", queryMessageMtInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("totalNum", data.getData().getPageParams().getTotalRows());

        return view;
    }

    /**
     * 待审批下发分页
     *
     * @return
     */
    @RequestMapping(value = "/check/page", method = RequestMethod.POST)
    public ModelAndView checkPage(@ModelAttribute RouteAuditMessageMtInfoValidator queryMessageMtInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("route/message/message_mt_audit_check_many");

        ResponseData<RouteAuditMessageMtInfoValidator> info = routeAuditMessageMtInfoService.findById(""+queryMessageMtInfoValidator.getId());
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }
        view.addObject("routeAuditMessageMtInfoValidator",info.getData());

        //分页查询
        queryMessageMtInfoValidator.setAccountId(info.getData().getAccountId());
        queryMessageMtInfoValidator.setInfoType(info.getData().getInfoType());
        queryMessageMtInfoValidator.setChannelId(info.getData().getChannelId());
        queryMessageMtInfoValidator.setMessageContent(info.getData().getMessageContent());
        queryMessageMtInfoValidator.setId(info.getData().getId());
        pageParams.setParams(queryMessageMtInfoValidator);

        ResponseData<PageList<RouteAuditMessageMtInfoValidator>> data = routeAuditMessageMtInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("queryMessageMtInfoValidator", queryMessageMtInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("totalNum", data.getData().getPageParams().getTotalRows());

        return view;
    }

    /**
     * 审核
     * @param routeAuditMessageMtInfoValidator
     * @param type single:单条 many:批量
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveCheck/{type}", method = RequestMethod.POST)
    public ModelAndView saveCheck(@ModelAttribute RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator,@PathVariable String type, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("route/message/message_mt_audit_check_single");
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");

        ResponseData<RouteAuditMessageMtInfoValidator> info = routeAuditMessageMtInfoService.findById(""+routeAuditMessageMtInfoValidator.getId());
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }

        //批量审核根据messageMD5
        if("many".equals(type)){
            routeAuditMessageMtInfoValidator.setMessageMd5(info.getData().getMessageMd5());
        }
        //操作审核
        ResponseData data = routeAuditMessageMtInfoService.check(routeAuditMessageMtInfoValidator,type);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //记录日志
        log.info("[待审批下发信息][{}][{}]数据:{}",type, user.getUserName(), JSON.toJSONString(routeAuditMessageMtInfoValidator));

        //按账号条数审核
        if("account".equals(type)){
            view.setView(new RedirectView("/route/message/mt/audit/account/list/", true, false));
            return view;
        }

        view.setView(new RedirectView("/route/message/mt/audit/list/", true, false));
        return view;
    }

    /**
     * 模糊查询待审批
     *
     * @return
     */
    @RequestMapping(value = "/likeCheck/list", method = RequestMethod.POST)
    public ModelAndView likeCheck(@ModelAttribute RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator) {
        ModelAndView view = new ModelAndView("route/message/message_mt_audit_check_like");

        //分页查询
        PageParams<RouteAuditMessageMtInfoValidator> pageParams = new PageParams<RouteAuditMessageMtInfoValidator>();
        pageParams.setPageSize(10);
        pageParams.setCurrentPage(1);
        pageParams.setParams(routeAuditMessageMtInfoValidator);

        ResponseData<PageList<RouteAuditMessageMtInfoValidator>> data = routeAuditMessageMtInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("routeAuditMessageMtInfoValidator", routeAuditMessageMtInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("totalNum", data.getData().getPageParams().getTotalRows());

        return view;
    }

    /**
     * 模糊查询待审批分页
     *
     * @return
     */
    @RequestMapping(value = "/likeCheck/page", method = RequestMethod.POST)
    public ModelAndView likeCheck(@ModelAttribute RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("route/message/message_mt_audit_check_like");

        //分页查询
        pageParams.setParams(routeAuditMessageMtInfoValidator);

        ResponseData<PageList<RouteAuditMessageMtInfoValidator>> data = routeAuditMessageMtInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("routeAuditMessageMtInfoValidator", routeAuditMessageMtInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("totalNum", data.getData().getPageParams().getTotalRows());

        return view;
    }

    /**
     * 账号条数统计
     *
     * @return
     */
    @RequestMapping(value = "/account/list", method = RequestMethod.GET)
    public ModelAndView account() {
        ModelAndView view = new ModelAndView("route/message/message_mt_audit_account");

        //分页查询
        PageParams<RouteAuditMessageAccountQo> pageParams = new PageParams<RouteAuditMessageAccountQo>();
        pageParams.setPageSize(12);
        pageParams.setCurrentPage(1);
        RouteAuditMessageAccountQo routeAuditMessageAccount = new RouteAuditMessageAccountQo();
        pageParams.setParams(routeAuditMessageAccount);

        ResponseData<PageList<RouteAuditMessageAccountQo>> data = routeAuditMessageMtInfoService.accountPage(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("routeAuditMessageAccount", routeAuditMessageAccount);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("total", data.getData().getPageParams().getTotalRows());

        return view;
    }

    /**
     * 账号条数统计分页
     *
     * @return
     */
    @RequestMapping(value = "/account/page", method = RequestMethod.POST)
    public ModelAndView account(@ModelAttribute RouteAuditMessageAccountQo routeAuditMessageAccount, PageParams pageParams) {
        ModelAndView view = new ModelAndView("route/message/message_mt_audit_account");

        //分页查询
        pageParams.setParams(routeAuditMessageAccount);

        ResponseData<PageList<RouteAuditMessageAccountQo>> data = routeAuditMessageMtInfoService.accountPage(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("routeAuditMessageAccount", routeAuditMessageAccount);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("total", data.getData().getPageParams().getTotalRows());

        return view;
    }

    /**
     * 账号待审批页面
     *
     * @return
     */
    @RequestMapping(value = "/check/account/{accountId}", method = RequestMethod.GET)
    public ModelAndView accountCheck(@PathVariable String accountId) {
        ModelAndView view = new ModelAndView("route/message/message_mt_audit_check_account");

        //分页查询
        PageParams<RouteAuditMessageMtInfoValidator> pageParams = new PageParams<RouteAuditMessageMtInfoValidator>();
        pageParams.setPageSize(10);
        pageParams.setCurrentPage(1);
        RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator = new RouteAuditMessageMtInfoValidator();
        routeAuditMessageMtInfoValidator.setAccountId(accountId);
        pageParams.setParams(routeAuditMessageMtInfoValidator);

        ResponseData<PageList<RouteAuditMessageMtInfoValidator>> data = routeAuditMessageMtInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("routeAuditMessageMtInfoValidator", routeAuditMessageMtInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("totalNum", data.getData().getPageParams().getTotalRows());

        return view;
    }

    /**
     * 账号待审批分页
     *
     * @return
     */
    @RequestMapping(value = "/accountCheck/page", method = RequestMethod.POST)
    public ModelAndView accountCheckPage(@ModelAttribute RouteAuditMessageMtInfoValidator routeAuditMessageMtInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("route/message/message_mt_audit_check_account");

        //分页查询
        pageParams.setParams(routeAuditMessageMtInfoValidator);

        ResponseData<PageList<RouteAuditMessageMtInfoValidator>> data = routeAuditMessageMtInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("routeAuditMessageMtInfoValidator", routeAuditMessageMtInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("totalNum", data.getData().getPageParams().getTotalRows());

        return view;
    }
}
