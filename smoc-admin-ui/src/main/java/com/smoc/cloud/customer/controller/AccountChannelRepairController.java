package com.smoc.cloud.customer.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairRuleValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelRepairValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigRepairRuleValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelRepairQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelRepairService;
import com.smoc.cloud.configure.channel.service.ConfigRepairRuleService;
import com.smoc.cloud.customer.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * EC业务账号失败补发管理
 */
@Slf4j
@RestController
@RequestMapping("/account/channel/repair")
public class AccountChannelRepairController {

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private AccountChannelRepairService accountChannelRepairService;

    @Autowired
    private ChannelRepairService channelRepairService;

    @Autowired
    private ConfigRepairRuleService configRepairRuleService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 业务账号列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/account/account_channel_repair/repair_list");

        //初始化数据
        PageParams<AccountChannelRepairQo> params = new PageParams<AccountChannelRepairQo>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountChannelRepairQo accountChannelRepairQo = new AccountChannelRepairQo();
        params.setParams(accountChannelRepairQo);

        //查询
        ResponseData<PageList<AccountChannelRepairQo>> data = accountChannelRepairService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountChannelRepairQo", accountChannelRepairQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 业务账号列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute AccountChannelRepairQo accountChannelRepairQo, PageParams pageParams) {
        ModelAndView view = new ModelAndView("customer/account/account_channel_repair/repair_list");

        //分页查询
        pageParams.setParams(accountChannelRepairQo);

        ResponseData<PageList<AccountChannelRepairQo>> data = accountChannelRepairService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountChannelRepairQo", accountChannelRepairQo);
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

        ModelAndView view = new ModelAndView("customer/account/account_channel_repair/account_repair_center");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询账号数据是否存在
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountBasicInfoValidator", data.getData());

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

        ModelAndView view = new ModelAndView("customer/account/account_channel_repair/config_repair_rule");

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
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询规则
        ResponseData<ConfigRepairRuleValidator> ruleData = configRepairRuleService.findByBusinessId(data.getData().getAccountId());
        if (!ResponseCode.SUCCESS.getCode().equals(ruleData.getCode())) {
            view.addObject("error", ruleData.getCode() + ":" + ruleData.getMessage());
            return view;
        }

        //不为空代表是修改
        if(!StringUtils.isEmpty(ruleData.getData())){
            view.addObject("op", "edit");
            view.addObject("configRepairRuleValidator", ruleData.getData());
            return view;
        }

        //初始化数据
        ConfigRepairRuleValidator configRepairRuleValidator = new ConfigRepairRuleValidator();
        configRepairRuleValidator.setId(UUID.uuid32());
        configRepairRuleValidator.setBusinessId(data.getData().getAccountId());
        configRepairRuleValidator.setBusinessType("ACCOUNT");

        view.addObject("configRepairRuleValidator", configRepairRuleValidator);

        return view;
    }

    /**
     * 保存规则
     *
     * @return
     */
    @RequestMapping(value = "/rule/save/{op}", method = RequestMethod.POST)
    public ModelAndView ruleSave(@ModelAttribute @Validated ConfigRepairRuleValidator configRepairRuleValidator, @PathVariable String op, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/account/account_channel_repair/config_repair_rule");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(configRepairRuleValidator.getBusinessId());
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询通道是否存在
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(configRepairRuleValidator.getBusinessId());
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
            systemUserLogService.logsAsync("BUSINESS_ACCOUNT", configRepairRuleValidator.getBusinessId(), "add".equals(op) ? user.getRealName() : user.getRealName(), op, "add".equals(op) ? "添加账号补发规则" : "修改账号补发规则", JSON.toJSONString(configRepairRuleValidator));
        }

        //记录日志
        log.info("[失败补发规则][补发配置][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(configRepairRuleValidator));

        view.setView(new RedirectView("/account/channel/repair/rule/edit/"+configRepairRuleValidator.getBusinessId(), true, false));
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

        ModelAndView view = new ModelAndView("customer/account/account_channel_repair/repair_add");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");

        //查询账号数据是否存在
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        List<String> carrierList = Arrays.asList(data.getData().getCarrier().split(","));

        //初始化数据
        ConfigChannelRepairRuleValidator configChannelRepairRuleValidator = new ConfigChannelRepairRuleValidator();
        configChannelRepairRuleValidator.setBusinessId(data.getData().getAccountId());
        configChannelRepairRuleValidator.setBusinessType("ACCOUNT");

        view.addObject("accountBasicInfoValidator", data.getData());
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

        ModelAndView view = new ModelAndView("customer/account/account_channel_repair/repair_add");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return null;
        }

        //查询账号数据是否存在
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return null;
        }

       //根据运营商、业务类型查询符合要求的备用通道
        ConfigChannelRepairValidator configChannelRepairValidator = new ConfigChannelRepairValidator();
        configChannelRepairValidator.setBusinessType(data.getData().getBusinessType());
        configChannelRepairValidator.setCarrier(carrier);
        configChannelRepairValidator.setFlag("ACCOUNT");
        configChannelRepairValidator.setChannelId(data.getData().getAccountId());
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
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/account/account_channel_repair/repair_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");

        //查询账号数据是否存在
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询规则
        ResponseData<ConfigRepairRuleValidator> ruleData = configRepairRuleService.findByBusinessId(data.getData().getAccountId());
        if (!ResponseCode.SUCCESS.getCode().equals(ruleData.getCode())) {
            view.addObject("error", ruleData.getCode() + ":" + ruleData.getMessage());
            return view;
        }
        if(StringUtils.isEmpty(ruleData.getData())){
            view.addObject("error", "先添加账号失败补发规则！");
            return view;
        }

        //初始化数据
        ConfigChannelRepairRuleValidator configChannelRepairRuleValidator = new ConfigChannelRepairRuleValidator();
        configChannelRepairRuleValidator.setBusinessId(data.getData().getAccountId());
        configChannelRepairRuleValidator.setBusinessType("ACCOUNT");

        //查询已经存在的备用通道
        ResponseData<List<ConfigChannelRepairRuleValidator>> channelRepairList = channelRepairService.findChannelRepairByChannelId(configChannelRepairRuleValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(channelRepairList.getCode())) {
            view.addObject("error", channelRepairList.getCode() + ":" + channelRepairList.getMessage());
            return view;
        }

        view.addObject("channelRepairList", channelRepairList.getData());
        view.addObject("accountBasicInfoValidator", data.getData());
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

        ModelAndView view = new ModelAndView("customer/account/account_channel_repair/repair_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(configChannelRepairRuleValidator.getBusinessId());
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询账号是否存在
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(configChannelRepairRuleValidator.getBusinessId());;
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作
        configChannelRepairRuleValidator.setId(UUID.uuid32());
        configChannelRepairRuleValidator.setBusinessType("ACCOUNT");
        configChannelRepairRuleValidator.setCreatedBy(user.getRealName());
        configChannelRepairRuleValidator.setRepairStatus("1");
        ResponseData repairData = channelRepairService.save(configChannelRepairRuleValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(repairData.getCode())) {
            view.addObject("error", repairData.getCode() + ":" + repairData.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(repairData.getCode())) {
            systemUserLogService.logsAsync("BUSINESS_ACCOUNT", configChannelRepairRuleValidator.getBusinessId(), "add".equals(op) ? user.getRealName() : user.getRealName(), op, "add".equals(op) ? "添加账号失败补发通道" : "修改账号失败补发通道", JSON.toJSONString(configChannelRepairRuleValidator));
        }

        //记录日志
        log.info("[账号失败补发通道管理][补发配置][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(configChannelRepairRuleValidator));

        view.setView(new RedirectView("/account/channel/repair/edit/"+configChannelRepairRuleValidator.getBusinessId(), true, false));
        return view;
    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_channel_repair/repair_edit");

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
            systemUserLogService.logsAsync("BUSINESS_ACCOUNT", infoDate.getData().getBusinessId(), user.getRealName(), "delete", "删除账号失败补发通道" , JSON.toJSONString(infoDate.getData()));
        }

        //记录日志
        log.info("[账号失败补发通道管理][补发配置][{}]数据:{}", "delete", user.getUserName(), JSON.toJSONString(infoDate.getData()));

        view.setView(new RedirectView("/account/channel/repair/edit/"+infoDate.getData().getBusinessId(), true, false));
        return view;
    }
}
