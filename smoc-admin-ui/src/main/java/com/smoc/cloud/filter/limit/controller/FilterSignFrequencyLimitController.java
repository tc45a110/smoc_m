package com.smoc.cloud.filter.limit.controller;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.filter.FilterSignFrequencyLimitValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.limit.service.FilterSignFrequencyLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * 单号签名频率限制
 */
@Slf4j
@RestController
@RequestMapping("/filter/limit")
public class FilterSignFrequencyLimitController {


    @Autowired
    private FilterSignFrequencyLimitService filterSignFrequencyLimitService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {

        ModelAndView view = new ModelAndView("filter/limit/filter_sign_number_limit_list");

        //初始化数据
        PageParams<FilterSignFrequencyLimitValidator> params = new PageParams<FilterSignFrequencyLimitValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FilterSignFrequencyLimitValidator filterSignFrequencyLimitValidator = new FilterSignFrequencyLimitValidator();
        params.setParams(filterSignFrequencyLimitValidator);

        //查询
        ResponseData<PageList<FilterSignFrequencyLimitValidator>> data = filterSignFrequencyLimitService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterSignFrequencyLimitValidator", filterSignFrequencyLimitValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FilterSignFrequencyLimitValidator filterSignFrequencyLimitValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("filter/limit/filter_sign_number_limit_list");

        //分页查询
        pageParams.setParams(filterSignFrequencyLimitValidator);

        ResponseData<PageList<AccountBasicInfoValidator>> data = filterSignFrequencyLimitService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterSignFrequencyLimitValidator", filterSignFrequencyLimitValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 新建
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add() {
        ModelAndView view = new ModelAndView("filter/limit/filter_sign_number_limit_edit");

        //初始化参数
        FilterSignFrequencyLimitValidator filterSignFrequencyLimitValidator = new FilterSignFrequencyLimitValidator();
        filterSignFrequencyLimitValidator.setId(UUID.uuid32());
        filterSignFrequencyLimitValidator.setStatus("1");//初始状态

        view.addObject("filterSignFrequencyLimitValidator", filterSignFrequencyLimitValidator);
        view.addObject("op", "add");

        return view;
    }

    /**
     * 编辑
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id) {
        ModelAndView view = new ModelAndView("filter/limit/filter_sign_number_limit_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询数据
        ResponseData<FilterSignFrequencyLimitValidator> data = filterSignFrequencyLimitService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }
        view.addObject("filterSignFrequencyLimitValidator", data.getData());
        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");

        return view;
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated FilterSignFrequencyLimitValidator filterSignFrequencyLimitValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("filter/limit/filter_sign_number_limit_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            filterSignFrequencyLimitValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            filterSignFrequencyLimitValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            filterSignFrequencyLimitValidator.setUpdatedTime(new Date());
            filterSignFrequencyLimitValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = filterSignFrequencyLimitService.save(filterSignFrequencyLimitValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("LIMIT", filterSignFrequencyLimitValidator.getId(), user.getRealName(), op, "add".equals(op) ? "添加单号签名频次限制" : "修改单号签名频次限制", JSON.toJSONString(filterSignFrequencyLimitValidator));
        }

        //记录日志
        log.info("[单号签名频次限制][保存][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(filterSignFrequencyLimitValidator));

        view.setView(new RedirectView("/filter/limit/list", true, false));
        return view;

    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable String id, HttpServletRequest request) {

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("filter/limit/filter_sign_number_limit_edit");
        ResponseData<FilterSignFrequencyLimitValidator> data = filterSignFrequencyLimitService.findById(id);

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("LIMIT", data.getData().getId(), user.getRealName(), "delete", "删除单号签名频次限制", JSON.toJSONString(data.getData()));
        }
        filterSignFrequencyLimitService.deleteById(id);

        view.setView(new RedirectView("/filter/limit/list", true, false));
        return view;

    }


}
