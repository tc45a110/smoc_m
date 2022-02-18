package com.smoc.cloud.filter.white.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.CodeNumberInfoValidator;
import com.smoc.cloud.common.smoc.filter.FilterGroupListValidator;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.group.service.GroupService;
import com.smoc.cloud.filter.white.service.WhiteService;
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
 * 白名单管理
 **/
@Slf4j
@RestController
@RequestMapping("/filter/white")
public class WhiteController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private WhiteService whiteService;

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView main(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/white/white_main");

        view.addObject("parentId","smoc_white");

        return view;
    }

    /**
     * 白名单列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String parentId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/white/white_list");

        //初始化数据
        PageParams<FilterWhiteListValidator> params = new PageParams<FilterWhiteListValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FilterWhiteListValidator filterWhiteListValidator = new FilterWhiteListValidator();
        filterWhiteListValidator.setGroupId(parentId);
        params.setParams(filterWhiteListValidator);

        //查询
        ResponseData<PageList<FilterWhiteListValidator>> data = whiteService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterWhiteListValidator", filterWhiteListValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("parentId",parentId);

        return view;

    }

    /**
     * 白名单列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FilterWhiteListValidator filterWhiteListValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("filter/white/white_list");

        //分页查询
        pageParams.setParams(filterWhiteListValidator);

        ResponseData<PageList<FilterWhiteListValidator>> data = whiteService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterWhiteListValidator", filterWhiteListValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("parentId",filterWhiteListValidator.getGroupId());

        return view;

    }

    /**
     * 添加白名单
     *
     * @return
     */
    @RequestMapping(value = "/add/{parentId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String parentId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/white/white_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //初始化参数
        FilterWhiteListValidator filterWhiteListValidator = new FilterWhiteListValidator();
        filterWhiteListValidator.setId(UUID.uuid32());
        filterWhiteListValidator.setGroupId(parentId);
        filterWhiteListValidator.setEnterpriseId("SMOC");
        filterWhiteListValidator.setStatus("1");
        filterWhiteListValidator.setIsSync("0");

        //查询组
        ResponseData<FilterGroupListValidator> data = groupService.findById(parentId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterWhiteListValidator",filterWhiteListValidator);
        view.addObject("filterGroupListValidator",data.getData());
        view.addObject("op","add");
        return view;

    }

    /**
     * 编辑白名单
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/white/white_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<FilterWhiteListValidator> data = whiteService.findById(id);
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

        view.addObject("filterWhiteListValidator",data.getData());
        view.addObject("filterGroupListValidator",groupData.getData());
        view.addObject("op","edit");
        return view;
    }

    /**
     * 保存白名单
     * @param filterWhiteListValidator
     * @param result
     * @param op
     * @param request
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated FilterWhiteListValidator filterWhiteListValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("filter/white/white_edit");

        //查询组
        ResponseData<FilterGroupListValidator> groupData = groupService.findById(filterWhiteListValidator.getGroupId());
        if (!ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            view.addObject("error", groupData.getCode() + ":" + groupData.getMessage());
            return view;
        }
        view.addObject("filterGroupListValidator",groupData.getData());

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("filterWhiteListValidator", filterWhiteListValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            filterWhiteListValidator.setCreatedTime(new Date());
            filterWhiteListValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            ResponseData<FilterWhiteListValidator> data = whiteService.findById(filterWhiteListValidator.getId());
            filterWhiteListValidator.setCreatedTime(data.getData().getCreatedTime());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        log.info("[白名单管理][{}][{}]数据:{}",op, user.getUserName(), JSON.toJSONString(filterWhiteListValidator));

        //保存操作
        ResponseData data = whiteService.save(filterWhiteListValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/filter/white/list/" + filterWhiteListValidator.getGroupId(), true, false));
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
        ModelAndView view = new ModelAndView("filter/white/white_edit");
        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<FilterWhiteListValidator> whiteData = whiteService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(whiteData.getCode())) {
            view.addObject("error", whiteData.getCode() + ":" + whiteData.getMessage());
            return view;
        }

        //记录日志
        log.info("[白名单管理][delete][{}]数据::{}", user.getUserName(), JSON.toJSONString(whiteData.getData()));
        //删除操作
        ResponseData data = whiteService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/filter/white/list/" + whiteData.getData().getGroupId(), true, false));
        return view;
    }

    /**
     * 导入白名单
     *
     * @return
     */
    @RequestMapping(value = "/upFilesView", method = RequestMethod.GET)
    public ModelAndView upFilesView(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/white/white_upfiles_view");

        return view;

    }

    /**
     * 导出白名单
     *
     * @return
     */
    @RequestMapping(value = "/downFilesView", method = RequestMethod.GET)
    public ModelAndView downFilesView(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/white/white_downfiles_view");

        return view;

    }

}
