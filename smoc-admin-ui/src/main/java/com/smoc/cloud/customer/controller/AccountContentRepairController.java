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
import com.smoc.cloud.common.smoc.customer.qo.AccountContentRepairQo;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.ConfigContentRepairRuleValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.channel.service.ChannelRepairService;
import com.smoc.cloud.customer.service.AccountContentRepairService;
import com.smoc.cloud.customer.service.BusinessAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * EC业务账号内容失败补发管理
 */
@Slf4j
@RestController
@RequestMapping("/account/content/repair")
public class AccountContentRepairController {

    @Autowired
    private AccountContentRepairService accountContentRepairService;

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private ChannelRepairService channelRepairService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 内容失败补发列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/account/account_content_repair/repair_list");

        //初始化数据
        PageParams<ConfigContentRepairRuleValidator> params = new PageParams<ConfigContentRepairRuleValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        ConfigContentRepairRuleValidator configContentRepairRuleValidator = new ConfigContentRepairRuleValidator();
        params.setParams(configContentRepairRuleValidator);

        //查询
        ResponseData<PageList<ConfigContentRepairRuleValidator>> data = accountContentRepairService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("configContentRepairRuleValidator", configContentRepairRuleValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 内容失败补发列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ConfigContentRepairRuleValidator configContentRepairRuleValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("customer/account/account_content_repair/repair_list");

        //分页查询
        pageParams.setParams(configContentRepairRuleValidator);

        ResponseData<PageList<ConfigContentRepairRuleValidator>> data = accountContentRepairService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("configContentRepairRuleValidator", configContentRepairRuleValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 业务账号列表
     *
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView search() {
        ModelAndView view = new ModelAndView("customer/account/account_content_repair/account_search_list");

        //初始化数据
        PageParams<AccountContentRepairQo> params = new PageParams<AccountContentRepairQo>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountContentRepairQo accountContentRepairQo = new AccountContentRepairQo();
        params.setParams(accountContentRepairQo);

        //查询
        ResponseData<PageList<AccountContentRepairQo>> data = accountContentRepairService.accountList(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountContentRepairQo", accountContentRepairQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 业务账号列表查询
     *
     * @return
     */
    @RequestMapping(value = "/search/page", method = RequestMethod.POST)
    public ModelAndView searchPage(@ModelAttribute AccountContentRepairQo accountContentRepairQo, PageParams pageParams) {
        ModelAndView view = new ModelAndView("customer/account/account_content_repair/account_search_list");

        //分页查询
        pageParams.setParams(accountContentRepairQo);

        ResponseData<PageList<AccountContentRepairQo>> data = accountContentRepairService.accountList(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountContentRepairQo", accountContentRepairQo);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 失败补发页面
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/add/{id}/{carrier}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id,@PathVariable String carrier,  HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/account/account_content_repair/repair_edit");

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

        //根据运营商符合要求的备用通道
        ConfigChannelRepairValidator configChannelRepairValidator = new ConfigChannelRepairValidator();
        configChannelRepairValidator.setBusinessType(data.getData().getBusinessType());
        configChannelRepairValidator.setCarrier(data.getData().getCarrier());
        configChannelRepairValidator.setFlag("ACCOUNT");
        configChannelRepairValidator.setChannelId(data.getData().getAccountId());
        ResponseData<List<ConfigChannelRepairValidator>> channelList = channelRepairService.findSpareChannel(configChannelRepairValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(channelList.getCode())) {
            view.addObject("error", channelList.getCode() + ":" + channelList.getMessage());
            return view;
        }

        //初始化数据
        ConfigContentRepairRuleValidator configContentRepairRuleValidator = new ConfigContentRepairRuleValidator();
        configContentRepairRuleValidator.setId(UUID.uuid32());
        configContentRepairRuleValidator.setAccountId(data.getData().getAccountId());
        configContentRepairRuleValidator.setCarrier(carrier);

        view.addObject("channelList", channelList.getData());
        view.addObject("configContentRepairRuleValidator", configContentRepairRuleValidator);
        view.addObject("accountBasicInfoValidator", data.getData());

        return view;
    }

    /**
     * 编辑
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/account/account_content_repair/repair_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<ConfigContentRepairRuleValidator> data = accountContentRepairService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询账号数据是否存在
        ResponseData<AccountBasicInfoValidator> accountData = businessAccountService.findById(data.getData().getAccountId());
        if (!ResponseCode.SUCCESS.getCode().equals(accountData.getCode())) {
            view.addObject("error", accountData.getCode() + ":" + accountData.getMessage());
            return view;
        }

        //根据运营商符合要求的备用通道
        ConfigChannelRepairValidator configChannelRepairValidator = new ConfigChannelRepairValidator();
        configChannelRepairValidator.setBusinessType(accountData.getData().getBusinessType());
        configChannelRepairValidator.setCarrier(data.getData().getCarrier());
        configChannelRepairValidator.setFlag("ACCOUNT");
        configChannelRepairValidator.setChannelId(data.getData().getAccountId());
        ResponseData<List<ConfigChannelRepairValidator>> channelList = channelRepairService.findSpareChannel(configChannelRepairValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(channelList.getCode())) {
            view.addObject("error", channelList.getCode() + ":" + channelList.getMessage());
            return view;
        }

        view.addObject("configContentRepairRuleValidator",data.getData());
        view.addObject("channelList", channelList.getData());
        view.addObject("accountBasicInfoValidator", accountData.getData());
        view.addObject("op","edit");

        return view;

    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated ConfigContentRepairRuleValidator configContentRepairRuleValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_content_repair/repair_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            //根据运营商符合要求的备用通道
            ConfigChannelRepairValidator configChannelRepairValidator = new ConfigChannelRepairValidator();
            configChannelRepairValidator.setBusinessType(configContentRepairRuleValidator.getBusinessType());
            configChannelRepairValidator.setCarrier(configContentRepairRuleValidator.getCarrier());
            configChannelRepairValidator.setFlag("ACCOUNT");
            configChannelRepairValidator.setChannelId(configContentRepairRuleValidator.getAccountId());
            ResponseData<List<ConfigChannelRepairValidator>> channelList = channelRepairService.findSpareChannel(configChannelRepairValidator);
            view.addObject("configContentRepairRuleValidator", configContentRepairRuleValidator);
            view.addObject("op", op);
            view.addObject("channelList", channelList.getData());
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            configContentRepairRuleValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            configContentRepairRuleValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            configContentRepairRuleValidator.setUpdatedTime(new Date());
            configContentRepairRuleValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        configContentRepairRuleValidator.setBusinessId(configContentRepairRuleValidator.getAccountId());
        configContentRepairRuleValidator.setBusinessType("ACCOUNT");
        configContentRepairRuleValidator.setRepairStatus("1");
        ResponseData data = accountContentRepairService.save(configContentRepairRuleValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("BUSINESS_ACCOUNT", configContentRepairRuleValidator.getAccountId(), "add".equals(op) ? configContentRepairRuleValidator.getCreatedBy() : configContentRepairRuleValidator.getUpdatedBy(), op, "add".equals(op) ? "添加内容失败补发" : "修改内容失败补发", JSON.toJSONString(configContentRepairRuleValidator));
        }

        //记录日志
        log.info("[内容失败补发配置][补发配置][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(configContentRepairRuleValidator));

        view.setView(new RedirectView("/account/content/repair/list", true, false));
        return view;

    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_content_repair/repair_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<ConfigContentRepairRuleValidator> infoDate = accountContentRepairService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        //删除操作
        ResponseData data = accountContentRepairService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("BUSINESS_ACCOUNT", infoDate.getData().getBusinessId(), user.getRealName(), "delete", "删除内容失败补发" , JSON.toJSONString(infoDate.getData()));
        }

        //记录日志
        log.info("[内容失败补发配置][补发配置][{}]数据:{}", "delete", user.getUserName(), JSON.toJSONString(infoDate.getData()));

        view.setView(new RedirectView("/account/content/repair/list", true, false));
        return view;
    }
}
