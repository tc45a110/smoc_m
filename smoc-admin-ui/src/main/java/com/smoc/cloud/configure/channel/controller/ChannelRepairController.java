package com.smoc.cloud.configure.channel.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.*;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelRepairService;
import com.smoc.cloud.configure.channel.service.ChannelService;
import com.smoc.cloud.configure.channel.service.ConfigRepairRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 通道补发管理
 **/
@Slf4j
@RestController
@RequestMapping("/configure/channel/repair")
public class ChannelRepairController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelRepairService channelRepairService;

    @Autowired
    private ConfigRepairRuleService configRepairRuleService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 通道补发管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_repair/channel_repair_list");

        ///初始化数据
        PageParams<ConfigChannelRepairValidator> params = new PageParams<ConfigChannelRepairValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        ConfigChannelRepairValidator configChannelRepairValidator = new ConfigChannelRepairValidator();
        params.setParams(configChannelRepairValidator);

        //查询
        ResponseData<PageList<ConfigChannelRepairValidator>> data = channelRepairService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("configChannelRepairValidator", configChannelRepairValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 通道补发分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ConfigChannelRepairValidator configChannelRepairValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("configure/channel/channel_repair/channel_repair_list");

        //分页查询
        pageParams.setParams(configChannelRepairValidator);

        ResponseData<PageList<ConfigChannelRepairValidator>> data = channelRepairService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("configChannelRepairValidator", configChannelRepairValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 失败补发中心
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/center/{id}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_repair/channel_repair_center");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询通道数据是否存在
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("channelBasicInfoValidator", data.getData());

        return view;
    }

    /**
     * 失败补发规则
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/rule/edit/{id}", method = RequestMethod.GET)
    public ModelAndView rule(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_repair/config_repair_rule");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");

        //查询通道数据是否存在
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询规则
        ResponseData<ConfigRepairRuleValidator> ruleData = configRepairRuleService.findByBusinessId(data.getData().getChannelId());
        if (!ResponseCode.SUCCESS.getCode().equals(ruleData.getCode())) {
            view.addObject("error", ruleData.getCode() + ":" + ruleData.getMessage());
            return view;
        }

        //不为空代表是修改
        if(!StringUtils.isEmpty(ruleData.getData())){
            view.addObject("op", "edit");
            view.addObject("configRepairRuleValidator", ruleData.getData());
            view.addObject("channelBasicInfoValidator", data.getData());
            return view;
        }

        //初始化数据
        ConfigRepairRuleValidator configRepairRuleValidator = new ConfigRepairRuleValidator();
        configRepairRuleValidator.setId(UUID.uuid32());
        configRepairRuleValidator.setBusinessId(data.getData().getChannelId());
        configRepairRuleValidator.setBusinessType("CHANNEL");

        view.addObject("configRepairRuleValidator", configRepairRuleValidator);
        view.addObject("channelBasicInfoValidator", data.getData());

        return view;
    }

    /**
     * 保存规则
     *
     * @return
     */
    @RequestMapping(value = "/rule/save/{op}", method = RequestMethod.POST)
    public ModelAndView ruleSave(@ModelAttribute @Validated ConfigRepairRuleValidator configRepairRuleValidator, @PathVariable String op, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_repair/config_repair_rule");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(configRepairRuleValidator.getBusinessId());
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询通道是否存在
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(configRepairRuleValidator.getBusinessId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            configRepairRuleValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            configRepairRuleValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            configRepairRuleValidator.setUpdatedTime(new Date());
            configRepairRuleValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存操作
        configRepairRuleValidator.setRepairStatus("1");
        ResponseData repairData = configRepairRuleService.save(configRepairRuleValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(repairData.getCode())) {
            view.addObject("error", repairData.getCode() + ":" + repairData.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(repairData.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_BASE", configRepairRuleValidator.getBusinessId(), "add".equals(op) ? user.getRealName() : user.getRealName(), op, "add".equals(op) ? "添加失败补发规则" : "修改失败补发规则", JSON.toJSONString(configRepairRuleValidator));
        }

        //记录日志
        log.info("[失败补发规则][补发配置][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(configRepairRuleValidator));

        view.setView(new RedirectView("/configure/channel/repair/rule/edit/"+configRepairRuleValidator.getBusinessId(), true, false));
        return view;
    }

    /**
     * 失败补发页面
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/add/{id}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_repair/channel_repair_add");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");

        //查询通道数据是否存在
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        List<String> carrierList = Arrays.asList(data.getData().getCarrier().split(","));

        //初始化数据
        ConfigChannelRepairRuleValidator configChannelRepairRuleValidator = new ConfigChannelRepairRuleValidator();
        configChannelRepairRuleValidator.setChannelId(data.getData().getChannelId());
        configChannelRepairRuleValidator.setBusinessType("CHANNEL");

        view.addObject("channelBasicInfoValidator", data.getData());
        view.addObject("configChannelRepairRuleValidator", configChannelRepairRuleValidator);
        view.addObject("carrierList", carrierList);

        return view;
    }

    /**
     * 查询备用通道
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/findChannel/{id}/{carrier}", method = RequestMethod.GET)
    public List<ConfigChannelRepairValidator> findChannel(@PathVariable String id,@PathVariable String carrier,  HttpServletRequest request) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return null;
        }

        //查询通道数据是否存在
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return null;
        }

        //根据运营商、业务类型查询符合要求的备用通道
        ConfigChannelRepairValidator configChannelRepairValidator = new ConfigChannelRepairValidator();
        configChannelRepairValidator.setBusinessType(data.getData().getBusinessType());
        configChannelRepairValidator.setCarrier(carrier);
        configChannelRepairValidator.setFlag("CHANNEL");
        configChannelRepairValidator.setChannelId(data.getData().getChannelId());
        ResponseData<List<ConfigChannelRepairValidator>> channelList = channelRepairService.findSpareChannel(configChannelRepairValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(channelList.getCode())) {
            return null;
        }

        return channelList.getData();
    }

    /**
     * 失败补发页面
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_repair/channel_repair_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");

        //查询通道数据是否存在
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询规则
        ResponseData<ConfigRepairRuleValidator> ruleData = configRepairRuleService.findByBusinessId(data.getData().getChannelId());
        if (!ResponseCode.SUCCESS.getCode().equals(ruleData.getCode())) {
            view.addObject("error", ruleData.getCode() + ":" + ruleData.getMessage());
            return view;
        }
        if(StringUtils.isEmpty(ruleData.getData())){
            view.addObject("error", "先添加主通道失败补发规则！");
            return view;
        }

        //初始化数据
        ConfigChannelRepairRuleValidator configChannelRepairRuleValidator = new ConfigChannelRepairRuleValidator();
        configChannelRepairRuleValidator.setChannelId(data.getData().getChannelId());
        configChannelRepairRuleValidator.setBusinessType("CHANNEL");

        //查询已经存在的备用通道
        ResponseData<List<ConfigChannelRepairRuleValidator>> channelRepairList = channelRepairService.findChannelRepairByChannelId(configChannelRepairRuleValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(channelRepairList.getCode())) {
            view.addObject("error", channelRepairList.getCode() + ":" + channelRepairList.getMessage());
            return view;
        }

        view.addObject("channelRepairList", channelRepairList.getData());
        view.addObject("channelBasicInfoValidator", data.getData());
        view.addObject("configChannelRepairRuleValidator", configChannelRepairRuleValidator);

        return view;
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated ConfigChannelRepairRuleValidator configChannelRepairRuleValidator, @PathVariable String op, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/channel/channel_repair/channel_repair_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(configChannelRepairRuleValidator.getChannelId());
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询通道是否存在
        ResponseData<ChannelBasicInfoValidator> data = channelService.findById(configChannelRepairRuleValidator.getChannelId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作
        configChannelRepairRuleValidator.setBusinessId(configChannelRepairRuleValidator.getChannelId());
        configChannelRepairRuleValidator.setId(UUID.uuid32());
        configChannelRepairRuleValidator.setBusinessType("CHANNEL");
        configChannelRepairRuleValidator.setCreatedBy(user.getRealName());
        configChannelRepairRuleValidator.setRepairStatus("1");
        ResponseData repairData = channelRepairService.save(configChannelRepairRuleValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(repairData.getCode())) {
            view.addObject("error", repairData.getCode() + ":" + repairData.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(repairData.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_BASE", configChannelRepairRuleValidator.getChannelId(), "add".equals(op) ? user.getRealName() : user.getRealName(), op, "add".equals(op) ? "添加失败补发通道" : "修改失败补发通道", JSON.toJSONString(configChannelRepairRuleValidator));
        }

        //记录日志
        log.info("[失败补发通道管理][补发配置][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(configChannelRepairRuleValidator));

        view.setView(new RedirectView("/configure/channel/repair/edit/"+configChannelRepairRuleValidator.getChannelId(), true, false));
        return view;
    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/contract/customer_contract_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<ConfigChannelRepairRuleValidator> infoDate = channelRepairService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        //删除操作
        ResponseData data = channelRepairService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CHANNEL_BASE", infoDate.getData().getChannelId(), user.getRealName(), "delete", "删除失败补发通道" , JSON.toJSONString(infoDate.getData()));
        }

        //记录日志
        log.info("[失败补发通道管理][补发配置][{}]数据:{}", "delete", user.getUserName(), JSON.toJSONString(infoDate.getData()));

        view.setView(new RedirectView("/configure/channel/repair/edit/"+infoDate.getData().getChannelId(), true, false));
        return view;
    }
}
