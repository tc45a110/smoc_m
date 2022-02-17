package com.smoc.cloud.customer.controller;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountChannelInfoValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.AccountChannelService;
import com.smoc.cloud.customer.service.BusinessAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * EC业务账号通道管理
 */
@Slf4j
@RestController
@RequestMapping("/account")
public class AccountChannelController {

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private AccountChannelService accountChannelService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 业务账号通道配置
     *
     * @return
     */
    @RequestMapping(value = "/edit/channel/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_channel(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel");

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        AccountChannelInfoQo accountChannelInfoQo = new AccountChannelInfoQo();
        accountChannelInfoQo.setAccountId(data.getData().getAccountId());
        accountChannelInfoQo.setCarrier(data.getData().getCarrier());
        accountChannelInfoQo.setAccountChannelType(data.getData().getAccountChannelType());

        if("INTERNATIONAL".equals(data.getData().getCarrier())){
            accountChannelInfoQo.setCarrier(data.getData().getCountryCode());
        }

        //查询配置的业务账号通道
        ResponseData<Map<String, AccountChannelInfoQo>> channelData = accountChannelService.findAccountChannelConfig(accountChannelInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(channelData.getCode())) {
            view.addObject("error", channelData.getCode() + ":" + channelData.getMessage());
            return view;
        }

        //初始化查询条件
        ChannelBasicInfoQo channelBasicInfoQo = new ChannelBasicInfoQo();
        channelBasicInfoQo.setAccountId(data.getData().getAccountId());

        view.addObject("channelMap", channelData.getData());
        view.addObject("accountBasicInfoValidator", data.getData());
        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("accountChannelInfoValidator", new AccountChannelInfoValidator());
        return view;

    }

    /**
     * 检索通道
     * @param accountId
     * @param carrier
     * @param request
     * @return
     */
    @RequestMapping(value = "/channel/findChannelList/{accountId}/{carrier}", method = RequestMethod.GET)
    public ModelAndView findChannelList(@PathVariable String accountId, @PathVariable String carrier, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel");

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        /**
         * 查询配置的业务账号通道
         */
        AccountChannelInfoQo accountChannelInfoQo = new AccountChannelInfoQo();
        accountChannelInfoQo.setAccountId(data.getData().getAccountId());
        accountChannelInfoQo.setCarrier(data.getData().getCarrier());
        accountChannelInfoQo.setAccountChannelType(data.getData().getAccountChannelType());
        //国际
        if("INTERNATIONAL".equals(data.getData().getCarrier())){
            accountChannelInfoQo.setCarrier(data.getData().getCountryCode());
        }
        ResponseData<Map<String, AccountChannelInfoQo>> channelData = accountChannelService.findAccountChannelConfig(accountChannelInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(channelData.getCode())) {
            view.addObject("error", channelData.getCode() + ":" + channelData.getMessage());
            return view;
        }

        /**
         * 检索通道列表
         */
        ChannelBasicInfoQo channelBasicInfoQo = new ChannelBasicInfoQo();
        channelBasicInfoQo.setAccountId(data.getData().getAccountId());
        channelBasicInfoQo.setCarrier(carrier);
        channelBasicInfoQo.setBusinessType(data.getData().getBusinessType());
        channelBasicInfoQo.setInfoType(data.getData().getInfoType());
        channelBasicInfoQo.setChannelStatus("001");//正常
        //国际
        if("INTERNATIONAL".equals(data.getData().getCarrier())){
            channelBasicInfoQo.setCarrier(data.getData().getCarrier());
            channelBasicInfoQo.setCountryCode(carrier);//国家代码
        }
        ResponseData<List<ChannelBasicInfoQo>> listDate = accountChannelService.findChannelList(channelBasicInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(listDate.getCode())) {
            view.addObject("error", listDate.getCode() + ":" + listDate.getMessage());
        }


        AccountChannelInfoValidator accountChannelInfoValidator = new AccountChannelInfoValidator();
        accountChannelInfoValidator.setCarrier(carrier);
        accountChannelInfoValidator.setAccountId(data.getData().getAccountId());

        view.addObject("channelMap", channelData.getData());
        view.addObject("list", listDate.getData());
        view.addObject("accountBasicInfoValidator", data.getData());
        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("accountChannelInfoValidator", accountChannelInfoValidator);

        return view;

    }


    /**
     * 检索通道-查询
     * @param channelBasicInfoQo
     * @param request
     * @return
     */
    @RequestMapping(value = "/channel/channelPage", method = RequestMethod.POST)
    public ModelAndView channelPage(@ModelAttribute ChannelBasicInfoQo channelBasicInfoQo, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel");

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(channelBasicInfoQo.getAccountId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        /**
         * 查询配置的业务账号通道
         */
        AccountChannelInfoQo accountChannelInfoQo = new AccountChannelInfoQo();
        accountChannelInfoQo.setAccountId(data.getData().getAccountId());
        accountChannelInfoQo.setCarrier(data.getData().getCarrier());
        accountChannelInfoQo.setAccountChannelType(data.getData().getAccountChannelType());
        //国际
        if("INTERNATIONAL".equals(data.getData().getCarrier())){
            accountChannelInfoQo.setCarrier(data.getData().getCountryCode());
        }
        ResponseData<Map<String, AccountChannelInfoQo>> channelData = accountChannelService.findAccountChannelConfig(accountChannelInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(channelData.getCode())) {
            view.addObject("error", channelData.getCode() + ":" + channelData.getMessage());
            return view;
        }

        /**
         * 检索通道列表
         */
        channelBasicInfoQo.setChannelStatus("001");//正常
        //国际
        if("INTERNATIONAL".equals(data.getData().getCarrier())){
            channelBasicInfoQo.setCarrier(data.getData().getCarrier());
        }

        ResponseData<List<ChannelBasicInfoQo>> listDate = accountChannelService.findChannelList(channelBasicInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(listDate.getCode())) {
            view.addObject("error", listDate.getCode() + ":" + listDate.getMessage());
        }

        AccountChannelInfoValidator accountChannelInfoValidator = new AccountChannelInfoValidator();
        if("INTERNATIONAL".equals(data.getData().getCarrier())){
            accountChannelInfoValidator.setCarrier(channelBasicInfoQo.getCountryCode());
        }else{
            accountChannelInfoValidator.setCarrier(channelBasicInfoQo.getCarrier());
        }
        accountChannelInfoValidator.setAccountId(data.getData().getAccountId());

        view.addObject("channelMap", channelData.getData());
        view.addObject("list", listDate.getData());
        view.addObject("accountBasicInfoValidator", data.getData());
        view.addObject("channelBasicInfoQo", channelBasicInfoQo);
        view.addObject("accountChannelInfoValidator", accountChannelInfoValidator);

        return view;

    }

    /**
     * 业务账号通道保存
     * @param accountChannelInfoValidator
     * @param request
     * @return
     */
    @RequestMapping(value = "/channel/save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute AccountChannelInfoValidator accountChannelInfoValidator, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("accountChannelInfoValidator", accountChannelInfoValidator);
            return view;
        }

        //查询账号下得运营商是否配置过通道
        ResponseData<List<AccountChannelInfoValidator>> infoData = accountChannelService.findByAccountIdAndCarrier(accountChannelInfoValidator.getAccountId(),accountChannelInfoValidator.getCarrier());
        if (ResponseCode.SUCCESS.getCode().equals(infoData.getCode()) && infoData.getData().size()>0) {
            view.addObject("error", "该业务账号下的运营商已配置过通道");
            return view;
        }

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountChannelInfoValidator.getAccountId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存账号通道
        String op= "add";
        accountChannelInfoValidator.setId(UUID.uuid32());
        accountChannelInfoValidator.setConfigType("ACCOUNT_CHANNEL");
        accountChannelInfoValidator.setChannelPriority("1");//等级
        accountChannelInfoValidator.setChannelSource("CHANNEL");//通道配置
        accountChannelInfoValidator.setChannelStatus("1");
        accountChannelInfoValidator.setCreatedTime(new Date());
        accountChannelInfoValidator.setCreatedBy(user.getRealName());
        ResponseData resData = accountChannelService.save(accountChannelInfoValidator,op);
        if (!ResponseCode.SUCCESS.getCode().equals(resData.getCode())) {
            view.addObject("error", resData.getCode() + ":" + resData.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("BUSINESS_ACCOUNT", accountChannelInfoValidator.getAccountId(), accountChannelInfoValidator.getCreatedBy() , op,  "添加业务账号通道配置", JSON.toJSONString(accountChannelInfoValidator));
        }

        //记录日志
        log.info("[EC业务账号管理][业务账号通道配置][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(accountChannelInfoValidator));

        view.setView(new RedirectView("/account/channel/findChannelList/"+accountChannelInfoValidator.getAccountId()+"/"+accountChannelInfoValidator.getCarrier(), true, false));

        return view;

    }

    /**
     * 移除账号已配置通道
     *
     * @return
     */
    @RequestMapping(value = "/channel/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteConfigChannelById(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/account/account_edit_channel");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询配置通道数据
        ResponseData<AccountChannelInfoValidator> responseData = accountChannelService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
        }

        //记录日志
        log.info("[EC业务账号管理][移除账号已配置通道][delete][{}]数据:{}", user.getUserName(), JSON.toJSONString(id));

        //删除操作
        ResponseData data = accountChannelService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("BUSINESS_ACCOUNT", responseData.getData().getAccountId(), user.getRealName(), "delete", "移除账号已配置通道", JSON.toJSONString(responseData));
        }

        view.setView(new RedirectView("/account/channel/findChannelList/"+responseData.getData().getAccountId()+"/"+responseData.getData().getCarrier(), true, false));

        return view;

    }

    /**
     * 业务账号通道组配置
     *
     * @return
     */
    @RequestMapping(value = "/edit/product/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_product(@PathVariable String accountId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/account/account_edit_channel_group");

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询配置的业务账号通道
        AccountChannelInfoQo accountChannelInfoQo = new AccountChannelInfoQo();
        accountChannelInfoQo.setAccountId(data.getData().getAccountId());
        accountChannelInfoQo.setCarrier(data.getData().getCarrier());
        accountChannelInfoQo.setAccountChannelType(data.getData().getAccountChannelType());
        if("INTERNATIONAL".equals(data.getData().getCarrier())){
            accountChannelInfoQo.setCarrier(data.getData().getCountryCode());
        }
        ResponseData<Map<String, AccountChannelInfoQo>> channelData = accountChannelService.findAccountChannelConfig(accountChannelInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(channelData.getCode())) {
            view.addObject("error", channelData.getCode() + ":" + channelData.getMessage());
            return view;
        }

        //初始化查询条件
        ChannelGroupInfoValidator channelGroupInfoValidator = new ChannelGroupInfoValidator();
        channelGroupInfoValidator.setAccountId(data.getData().getAccountId());

        view.addObject("channelMap", channelData.getData());
        view.addObject("accountBasicInfoValidator", data.getData());
        view.addObject("channelGroupInfoValidator", channelGroupInfoValidator);
        view.addObject("accountChannelInfoValidator", new AccountChannelInfoValidator());
        return view;
    }

    /**
     * 检索通道组
     * @param accountId
     * @param carrier
     * @param request
     * @return
     */
    @RequestMapping(value = "/channelGroup/channelGroupList/{accountId}/{carrier}", method = RequestMethod.GET)
    public ModelAndView findChannelGroupList(@PathVariable String accountId, @PathVariable String carrier, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel_group");

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        /**
         * 查询配置的业务账号通道
         */
        AccountChannelInfoQo accountChannelInfoQo = new AccountChannelInfoQo();
        accountChannelInfoQo.setAccountId(data.getData().getAccountId());
        accountChannelInfoQo.setCarrier(data.getData().getCarrier());
        accountChannelInfoQo.setAccountChannelType(data.getData().getAccountChannelType());
        if("INTERNATIONAL".equals(data.getData().getCarrier())){
            accountChannelInfoQo.setCarrier(data.getData().getCountryCode());
        }
        ResponseData<Map<String, AccountChannelInfoQo>> channelData = accountChannelService.findAccountChannelConfig(accountChannelInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(channelData.getCode())) {
            view.addObject("error", channelData.getCode() + ":" + channelData.getMessage());
            return view;
        }

        /**
         * 检索通道组列表
         */
        ChannelGroupInfoValidator channelGroupInfoValidator = new ChannelGroupInfoValidator();
        channelGroupInfoValidator.setAccountId(data.getData().getAccountId());
        channelGroupInfoValidator.setCarrier(carrier);
        channelGroupInfoValidator.setBusinessType(data.getData().getBusinessType());
        channelGroupInfoValidator.setInfoType(data.getData().getInfoType());
        channelGroupInfoValidator.setChannelGroupStatus("1");//正常
        //国际
        if("INTERNATIONAL".equals(data.getData().getCarrier())){
            channelGroupInfoValidator.setCarrier(data.getData().getCarrier());
            channelGroupInfoValidator.setCountryCode(carrier);//国家代码
        }
        ResponseData<List<ChannelGroupInfoValidator>> listDate = accountChannelService.findChannelGroupList(channelGroupInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(listDate.getCode())) {
            view.addObject("error", listDate.getCode() + ":" + listDate.getMessage());
        }


        AccountChannelInfoValidator accountChannelInfoValidator = new AccountChannelInfoValidator();
        accountChannelInfoValidator.setCarrier(carrier);
        accountChannelInfoValidator.setAccountId(data.getData().getAccountId());

        view.addObject("channelMap", channelData.getData());
        view.addObject("list", listDate.getData());
        view.addObject("accountBasicInfoValidator", data.getData());
        view.addObject("channelGroupInfoValidator", channelGroupInfoValidator);
        view.addObject("accountChannelInfoValidator", accountChannelInfoValidator);

        return view;
    }

    /**
     * 检索通道组-查询
     * @param channelGroupInfoValidator
     * @param request
     * @return
     */
    @RequestMapping(value = "/channelGroup/channelGroupPage", method = RequestMethod.POST)
    public ModelAndView channelGroupPage(@ModelAttribute ChannelGroupInfoValidator channelGroupInfoValidator, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel_group");

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(channelGroupInfoValidator.getAccountId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        /**
         * 查询配置的业务账号通道
         */
        AccountChannelInfoQo accountChannelInfoQo = new AccountChannelInfoQo();
        accountChannelInfoQo.setAccountId(data.getData().getAccountId());
        accountChannelInfoQo.setCarrier(data.getData().getCarrier());
        accountChannelInfoQo.setAccountChannelType(data.getData().getAccountChannelType());
        if("INTERNATIONAL".equals(data.getData().getCarrier())){
            accountChannelInfoQo.setCarrier(data.getData().getCountryCode());
        }
        ResponseData<Map<String, AccountChannelInfoQo>> channelData = accountChannelService.findAccountChannelConfig(accountChannelInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(channelData.getCode())) {
            view.addObject("error", channelData.getCode() + ":" + channelData.getMessage());
            return view;
        }

        /**
         * 检索通道组列表
         */
        channelGroupInfoValidator.setChannelGroupStatus("1");//正常
        ResponseData<List<ChannelGroupInfoValidator>> listDate = accountChannelService.findChannelGroupList(channelGroupInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(listDate.getCode())) {
            view.addObject("error", listDate.getCode() + ":" + listDate.getMessage());
        }

        AccountChannelInfoValidator accountChannelInfoValidator = new AccountChannelInfoValidator();
        accountChannelInfoValidator.setAccountId(data.getData().getAccountId());
        if("INTERNATIONAL".equals(data.getData().getCarrier())){
            accountChannelInfoValidator.setCarrier(channelGroupInfoValidator.getCountryCode());
        }else{
            accountChannelInfoValidator.setCarrier(channelGroupInfoValidator.getCarrier());
        }

        view.addObject("channelMap", channelData.getData());
        view.addObject("list", listDate.getData());
        view.addObject("accountBasicInfoValidator", data.getData());
        view.addObject("channelGroupInfoValidator", channelGroupInfoValidator);
        view.addObject("accountChannelInfoValidator", accountChannelInfoValidator);

        return view;

    }

    /**
     * 业务账号通道組保存
     * @param accountChannelInfoValidator
     * @param request
     * @return
     */
    @RequestMapping(value = "/channelGroup/save", method = RequestMethod.POST)
    public ModelAndView channelGroupSave(@ModelAttribute AccountChannelInfoValidator accountChannelInfoValidator, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_channel_group");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("accountChannelInfoValidator", accountChannelInfoValidator);
            return view;
        }

        //查询账号下得运营商是否配置过通道
        ResponseData<List<AccountChannelInfoValidator>> infoData = accountChannelService.findByAccountIdAndCarrier(accountChannelInfoValidator.getAccountId(),accountChannelInfoValidator.getCarrier());
        if (ResponseCode.SUCCESS.getCode().equals(infoData.getCode()) && infoData.getData().size()>0) {
            view.addObject("error", "该业务账号下的运营商已配置过通道組");
            return view;
        }

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountChannelInfoValidator.getAccountId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存账号通道
        String op= "add";
        accountChannelInfoValidator.setId(UUID.uuid32());
        accountChannelInfoValidator.setConfigType("ACCOUNT_CHANNEL_GROUP");
        accountChannelInfoValidator.setChannelPriority("1");//等级
        accountChannelInfoValidator.setChannelSource("CHANNEL_GROUP");//通道组配置
        accountChannelInfoValidator.setChannelStatus("1");
        accountChannelInfoValidator.setCreatedTime(new Date());
        accountChannelInfoValidator.setCreatedBy(user.getRealName());
        ResponseData resData = accountChannelService.save(accountChannelInfoValidator,op);
        if (!ResponseCode.SUCCESS.getCode().equals(resData.getCode())) {
            view.addObject("error", resData.getCode() + ":" + resData.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("BUSINESS_ACCOUNT", accountChannelInfoValidator.getAccountId(), accountChannelInfoValidator.getCreatedBy() , op,  "添加业务账号通道組配置", JSON.toJSONString(accountChannelInfoValidator));
        }

        //记录日志
        log.info("[EC业务账号管理][业务账号通道組配置][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(accountChannelInfoValidator));

        view.setView(new RedirectView("/account/channelGroup/channelGroupList/"+accountChannelInfoValidator.getAccountId()+"/"+accountChannelInfoValidator.getCarrier(), true, false));

        return view;
    }

    /**
     * 移除账号已配置通道组
     *
     * @return
     */
    @RequestMapping(value = "/channelGroup/deleteChannelGroup/{accountId}/{carrier}/{channelGroupId}", method = RequestMethod.GET)
    public ModelAndView deleteChannelGroup(@PathVariable String accountId, @PathVariable String carrier, @PathVariable String channelGroupId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/account/account_edit_channel");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> base = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(base.getCode())) {
            view.addObject("error", base.getCode() + ":" + base.getMessage());
            return view;
        }

        //查询删除的数据
        ResponseData<List<AccountChannelInfoValidator>> responseData = accountChannelService.findByAccountIdAndCarrierAndChannelGroupId(accountId,carrier,channelGroupId);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
            return view;
        }

        //记录日志
        log.info("[EC业务账号管理][移除账号已配置通道][delete][{}]数据:{}", user.getUserName(), JSON.toJSONString(responseData));

        //删除操作
        ResponseData data = accountChannelService.deleteChannelGroup(accountId,carrier,channelGroupId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("BUSINESS_ACCOUNT", accountId, user.getRealName(), "delete", "移除账号已配置通道组", JSON.toJSONString(responseData));
        }

        view.setView(new RedirectView("/account/channelGroup/channelGroupList/"+accountId+"/"+carrier, true, false));

        return view;

    }

    /**
     * 业务账号通道明细
     *
     * @return
     */
    @RequestMapping(value = "/view/channel/detail/{accountId}", method = RequestMethod.GET)
    public ModelAndView account_channel(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_view_channel_detail");

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> base = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(base.getCode())) {
            view.addObject("error", base.getCode() + ":" + base.getMessage());
            return view;
        }

        //初始化数据
        AccountChannelInfoValidator accountChannelInfoValidator = new AccountChannelInfoValidator();
        accountChannelInfoValidator.setConfigType(base.getData().getAccountChannelType());
        accountChannelInfoValidator.setAccountId(base.getData().getAccountId());

        //查询
        ResponseData<List<AccountChannelInfoValidator>> data = accountChannelService.channelDetail(accountChannelInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountBasicInfoValidator", base.getData());
        view.addObject("list", data.getData());
        return view;
    }

    /**
     * 查看已配置通道数
     *
     * @return
     */
    @RequestMapping(value = "/channel/configChannelNum/{accountId}", produces = "text/html;charset=utf-8", method = RequestMethod.GET)
    public String configChannelNum(@PathVariable String accountId, HttpServletRequest request) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            return MpmValidatorUtil.validateMessage(validator);
        }

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> base = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(base.getCode())) {
            return "业务账号不存在";
        }

        //查询已配置的通道
        AccountChannelInfoValidator accountChannelInfoValidator = new AccountChannelInfoValidator();
        accountChannelInfoValidator.setConfigType(base.getData().getAccountChannelType());
        accountChannelInfoValidator.setAccountId(base.getData().getAccountId());
        ResponseData<List<AccountChannelInfoValidator>> data = accountChannelService.channelDetail(accountChannelInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return data.getMessage();
        }

        List<AccountChannelInfoValidator> list = data.getData();
        if (StringUtils.isEmpty(list) || list.size() <= 0) {
            return "无配置通道";
        }

        //取字典数据
        ServletContext context = request.getServletContext();
        Map<String, DictType> dictMap = (Map<String, DictType>) context.getAttribute("dict");
        DictType dictType  = dictMap.get("carrier");
        if("INTERNATIONAL".equals(base.getData().getCarrier())){
            dictType  = dictMap.get("internationalArea");
        }
        List<Dict> dictList = dictType.getDict();

        //封装已配置的通道数据
        StringBuilder configChannelName = new StringBuilder();
        for (int a = 0; a < list.size(); a++) {
            AccountChannelInfoValidator qo = list.get(a);
            String carrier = "";
            for (int i =0;i<dictList.size();i++) {
                Dict dict = dictList.get(i);
                if (qo.getCarrier().equals(dict.getFieldCode())) {
                    carrier += dict.getFieldName();
                    break;
                }
            }
            configChannelName.append(carrier+"："+qo.getChannelName() + "，权重 " + qo.getChannelWeight() + "；");
            if (a != list.size()-1) {
                configChannelName.append("@");
            }
        }

        return configChannelName.toString();
    }
}
