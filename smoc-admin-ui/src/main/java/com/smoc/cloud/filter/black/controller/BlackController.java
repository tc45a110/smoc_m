package com.smoc.cloud.filter.black.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import com.smoc.cloud.common.smoc.filter.FilterGroupListValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.black.service.BlackService;
import com.smoc.cloud.filter.group.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 黑名单管理
 **/
@Slf4j
@Controller
@RequestMapping("/filter/black")
public class BlackController {

    @Autowired
    private BlackService blackService;

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView main(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/black/black_main");

        view.addObject("parentId","smoc_black");

        return view;
    }


    /**
     * 黑名单列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String parentId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/black/black_list");

        //初始化数据
        PageParams<FilterBlackListValidator> params = new PageParams<FilterBlackListValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FilterBlackListValidator filterBlackListValidator = new FilterBlackListValidator();
        filterBlackListValidator.setGroupId(parentId);
        params.setParams(filterBlackListValidator);

        //查询
        ResponseData<PageList<FilterBlackListValidator>> data = blackService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterBlackListValidator", filterBlackListValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("parentId",parentId);

        return view;

    }

    /**
     * 黑名单列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FilterBlackListValidator filterBlackListValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("filter/black/black_list");

        //分页查询
        pageParams.setParams(filterBlackListValidator);

        ResponseData<PageList<FilterBlackListValidator>> data = blackService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterBlackListValidator", filterBlackListValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("parentId",filterBlackListValidator.getGroupId());

        return view;

    }

    /**
     * 添加黑名单
     *
     * @return
     */
    @RequestMapping(value = "/add/{parentId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String parentId,HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/black/black_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //初始化参数
        FilterBlackListValidator filterBlackListValidator = new FilterBlackListValidator();
        filterBlackListValidator.setId(UUID.uuid32());
        filterBlackListValidator.setGroupId(parentId);
        filterBlackListValidator.setEnterpriseId("SMOC");
        filterBlackListValidator.setStatus("1");
        filterBlackListValidator.setIsSync("0");

        //查询组
        ResponseData<FilterGroupListValidator> data = groupService.findById(parentId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterBlackListValidator",filterBlackListValidator);
        view.addObject("filterGroupListValidator",data.getData());
        view.addObject("op","add");

        return view;

    }

    /**
     * 编辑黑名单
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/black/black_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<FilterBlackListValidator> data = blackService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询组
        ResponseData<FilterGroupListValidator> groupData = groupService.findById(data.getData().getGroupId());
        if (!ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            view.addObject("error", groupData.getCode() + ":" + groupData.getMessage());
            return view;
        }

        view.addObject("filterBlackListValidator",data.getData());
        view.addObject("filterGroupListValidator",groupData.getData());
        view.addObject("op","edit");

        return view;

    }

    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated FilterBlackListValidator filterBlackListValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("filter/black/black_edit");

        //查询组
        ResponseData<FilterGroupListValidator> groupData = groupService.findById(filterBlackListValidator.getGroupId());
        if (!ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            view.addObject("error", groupData.getCode() + ":" + groupData.getMessage());
            return view;
        }
        view.addObject("filterGroupListValidator",groupData.getData());

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("filterBlackListValidator", filterBlackListValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            filterBlackListValidator.setCreatedTime(new Date());
            filterBlackListValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            ResponseData<FilterBlackListValidator> data = blackService.findById(filterBlackListValidator.getId());
            filterBlackListValidator.setCreatedTime(data.getData().getCreatedTime());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        log.info("[黑名单管理][{}][{}]数据:{}",op, user.getUserName(), JSON.toJSONString(filterBlackListValidator));

        //保存操作
        ResponseData data = blackService.save(filterBlackListValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/filter/black/list/" + filterBlackListValidator.getGroupId(), true, false));
        return view;
    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("filter/black/black_edit");
        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<FilterBlackListValidator> whiteData = blackService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(whiteData.getCode())) {
            view.addObject("error", whiteData.getCode() + ":" + whiteData.getMessage());
            return view;
        }

        //记录日志
        log.info("[黑名单管理][delete][{}]数据::{}", user.getUserName(), JSON.toJSONString(whiteData.getData()));
        //删除操作
        ResponseData data = blackService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/filter/black/list/" + whiteData.getData().getGroupId(), true, false));
        return view;
    }

    /**
     * 添加黑名单
     *
     * @return
     */
    @RequestMapping(value = "/upFilesView", method = RequestMethod.GET)
    public ModelAndView upFilesView(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/black/black_upfiles_view");

        return view;

    }

    /**
     * 导出黑名单
     *
     * @return
     */
    @RequestMapping(value = "/downFilesView", method = RequestMethod.GET)
    public ModelAndView downFilesView(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/black/black_downfiles_view");

        return view;

    }

}
