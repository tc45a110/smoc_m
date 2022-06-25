package com.smoc.cloud.customer.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.AccountChannelInfoQo;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountFinanceInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountInterfaceInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.*;
import com.smoc.cloud.properties.SmocProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EC业务账号管理
 */
@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private AccountFinanceService accountFinanceService;

    @Autowired
    private AccountInterfaceService accountInterfaceService;

    @Autowired
    private AccountChannelService accountChannelService;

    @Autowired
    private SmocProperties smocProperties;

    /**
     * 业务账号列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/account/account_list");

        //初始化数据
        PageParams<AccountBasicInfoValidator> params = new PageParams<AccountBasicInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
        params.setParams(accountBasicInfoValidator);

        //查询
        ResponseData<PageList<AccountBasicInfoValidator>> data = businessAccountService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountBasicInfoValidator", accountBasicInfoValidator);
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
    public ModelAndView page(@ModelAttribute AccountBasicInfoValidator accountBasicInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("customer/account/account_list");

        //分页查询
        pageParams.setParams(accountBasicInfoValidator);

        ResponseData<PageList<AccountBasicInfoValidator>> data = businessAccountService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountBasicInfoValidator", accountBasicInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 查询EC列表
     *
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView search() {
        ModelAndView view = new ModelAndView("customer/account/account_search");

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
     * EC业务账号配置中心
     *
     * @return
     */
    @RequestMapping(value = "/edit/center/{flag}/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_center(@PathVariable String flag, @PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_center");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询账号配置的是通道组还是通道
        if("base".equals(flag) || "international".equals(flag) ){
            view.addObject("enterpriseId", accountId);
            view.addObject("flag", flag);
            view.addObject("accountId", accountId);
            return view;
        }

        //查询账号
        ResponseData<AccountBasicInfoValidator> info = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }

        view.addObject("accountChannelType", info.getData().getAccountChannelType());
        view.addObject("enterpriseId", info.getData().getEnterpriseId());
        view.addObject("flag", flag);
        view.addObject("accountId", accountId);

        return view;

    }

    /**
     * 业务账号基本信息
     *
     * @return
     */
    @RequestMapping(value = "/edit/base/{flag}/{accountId}", method = RequestMethod.GET)
    public ModelAndView edit_base(@PathVariable String flag, @PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_base");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        /**
         * 如果flag值为base,那么accountId为企业id
         */
        if ("base".equals(flag) || "international".equals(flag)) {
            AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
            accountBasicInfoValidator.setEnterpriseId(accountId);
            accountBasicInfoValidator.setAccountStatus("2");
            accountBasicInfoValidator.setRandomExtendCodeLength(0);
            accountBasicInfoValidator.setAccountProcess("10000");
            accountBasicInfoValidator.setBusinessType("TEXT_SMS");
            accountBasicInfoValidator.setIndustryType("001");//行业类型：默认金融
            accountBasicInfoValidator.setTransferType("0");//是否支持携号转网：默认否
            accountBasicInfoValidator.setAccountPriority("2");//账号优先级：默认中
            accountBasicInfoValidator.setAccountChannelType("ACCOUNT_CHANNEL_GROUP");//设置通道方式:默认通道组
            accountBasicInfoValidator.setRepairStatus("0");//默认不补发

            //查询企业数据
            ResponseData<EnterpriseBasicInfoValidator> data = enterpriseService.findById(accountId);
            if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                view.addObject("error", data.getCode() + ":" + data.getMessage());
            }

            //生成业务账号
            ResponseData<String> bussinessAccountId = businessAccountService.createAccountId(data.getData().getEnterpriseFlag());
            accountBasicInfoValidator.setAccountId(data.getData().getEnterpriseFlag()+bussinessAccountId.getMessage());

            //国际账号
            if("international".equals(flag)){
                accountBasicInfoValidator.setCarrier("INTL");
                accountBasicInfoValidator.setBusinessType("INTERNATIONAL_SMS");
                accountBasicInfoValidator.setTransferType("0");
                accountBasicInfoValidator.setRepairStatus("0");//默认不补发
                view.setViewName("customer/account/international/account_international_edit_base");
            }

            //op操作标记，add表示添加，edit表示修改
            view.addObject("op", "add");
            view.addObject("accountBasicInfoValidator", accountBasicInfoValidator);
            view.addObject("enterpriseBasicInfoValidator", data.getData());
            return view;
        }

        /**
         * 修改:查询数据
         */
        ResponseData<AccountBasicInfoValidator> info = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> data = enterpriseService.findById(info.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        view.addObject("op", "edit");
        view.addObject("accountBasicInfoValidator", info.getData());
        view.addObject("enterpriseBasicInfoValidator", data.getData());

        if("INTL".equals(info.getData().getCarrier())){
            view.setViewName("customer/account/international/account_international_edit_base");
        }

        return view;

    }

    /**
     * 业务账号基本信息提交
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated AccountBasicInfoValidator accountBasicInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_base");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //国际
        if("INTL".equals(accountBasicInfoValidator.getCarrier())){
            view.setViewName("customer/account/international/account_international_edit_base");
        }

        String[] carriers= accountBasicInfoValidator.getCarrier().split(",");
        if(carriers.length==1 && StringUtils.isEmpty(accountBasicInfoValidator.getTransferType())){
            FieldError err = new FieldError("是否支持携号转网", "transferType", "是否支持携号转网不能为空");
            result.addError(err);
        }
        //如果正常状态并且账号进度未完善，
        if ("1".equals(accountBasicInfoValidator.getAccountStatus()) && Integer.parseInt(accountBasicInfoValidator.getAccountProcess())<=11100) {
            FieldError err = new FieldError("账号状态", "accountStatus", "正常状态下需要完善账号配置信息");
            result.addError(err);
        }

        //多网：默认支持携号转网
        if(carriers.length>1){
            accountBasicInfoValidator.setTransferType("1");
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> enterdata = enterpriseService.findById(accountBasicInfoValidator.getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterdata.getCode())) {
            view.addObject("error", enterdata.getCode() + ":" + enterdata.getMessage());
        }
        String accountId = accountBasicInfoValidator.getAccountId();
        if(!org.apache.commons.lang3.StringUtils.isNumeric(accountId)){
            if(!enterdata.getData().getEnterpriseFlag().equals(accountId.substring(0,3))){
                FieldError err = new FieldError("业务账号", "errorAccount", "企业标识：三位字母不能修改");
                result.addError(err);
            }
        }


        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("accountBasicInfoValidator", accountBasicInfoValidator);
            view.addObject("op", op);
            ResponseData<EnterpriseBasicInfoValidator> data = enterpriseService.findById(accountBasicInfoValidator.getEnterpriseId());
            if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                view.addObject("error", data.getCode() + ":" + data.getMessage());
            }
            view.addObject("enterpriseBasicInfoValidator", data.getData());
            return view;
        }

        String accountChannelType = "";

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            accountBasicInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            accountBasicInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            accountBasicInfoValidator.setUpdatedTime(new Date());
            accountBasicInfoValidator.setUpdatedBy(user.getRealName());
            ResponseData<AccountBasicInfoValidator> info = businessAccountService.findById(accountBasicInfoValidator.getAccountId());
            accountChannelType = info.getData().getAccountChannelType();
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = businessAccountService.save(accountBasicInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("BUSINESS_ACCOUNT", accountBasicInfoValidator.getAccountId(), "add".equals(op) ? accountBasicInfoValidator.getCreatedBy() : accountBasicInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加业务账号基本信息" : "修改业务账号基本信息", JSON.toJSONString(accountBasicInfoValidator));
        }

        //记录日志
        log.info("[EC业务账号管理][业务账号基本信息][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(accountBasicInfoValidator));

        ResponseData<AccountBasicInfoValidator> accountBaseInfo = businessAccountService.findById(accountBasicInfoValidator.getAccountId());

        //保存成功之后，重新加载页面，iframe刷新标识,只有添加或者是修改了通道方式才会刷新
        if ("add".equals(op) || (!accountBasicInfoValidator.getAccountChannelType().equals(accountChannelType))) {
            view.addObject("refreshFlag", "refreshFlag");
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(accountBaseInfo.getData().getEnterpriseId());
        view.addObject("enterpriseBasicInfoValidator", enterpriseData.getData());

        view.addObject("accountBasicInfoValidator", accountBaseInfo.getData());
        view.addObject("op", "edit");

        return view;
    }

    /**
     * 注销、启用账号
     *
     * @return
     */
    @RequestMapping(value = "/forbiddenAccountById/{id}/{status}", method = RequestMethod.GET)
    public ModelAndView forbiddenAccountById(@PathVariable String id,@PathVariable String status, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询账号
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(id);
        if (!StringUtils.isEmpty(data) && !ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //注销、启用账号
        ResponseData webData = businessAccountService.forbiddenAccountById(id,status);

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(webData.getCode())) {
            systemUserLogService.logsAsync("BUSINESS_ACCOUNT", data.getData().getEnterpriseId(), user.getRealName(), "edit", ("1".equals(status) || "2".equals(status)) ? "注销EC业务账号":"启用EC业务账号账号", JSON.toJSONString(data.getData()));
        }

        //记录日志
        log.info("[EC业务账号管理][{}][{}][{}]数据:{}", ("1".equals(status) || "2".equals(status)) ? "注销EC业务账号":"启用EC业务账号账号","edit" , user.getUserName(), JSON.toJSONString(data.getData()));
        view.setView(new RedirectView("/account/list", true, false));
        return view;

    }


    /**
     * EC业务账号中心
     *
     * @return
     */
    @RequestMapping(value = "/center/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String enterpriseId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_account_center");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(enterpriseId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> data = enterpriseService.findById(enterpriseId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //查询企业所有的业务账号
        ResponseData<List<AccountBasicInfoValidator>> info = businessAccountService.findBusinessAccountByEnterpriseId(data.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }

        /**
         * 账号数量
         */
        int TEXT_SMS = 0;
        int MULTI_SMS = 0;
        int MMS = 0;
        int SMS_5G = 0;
        int INTERNATIONAL_SMS = 0;
        int BLACK_SERVICE = 0;

        if(!StringUtils.isEmpty(info.getData()) && info.getData().size()>0){
            for(int i=0;i<info.getData().size();i++){
                AccountBasicInfoValidator accountBasicInfoValidator = info.getData().get(i);
                if("TEXT_SMS".equals(accountBasicInfoValidator.getBusinessType())){
                    TEXT_SMS+=1;
                }else if("MULTI_SMS".equals(accountBasicInfoValidator.getBusinessType())){
                    MULTI_SMS+=1;
                }else if("MMS".equals(accountBasicInfoValidator.getBusinessType())){
                    MMS+=1;
                }else if("5G_SMS".equals(accountBasicInfoValidator.getBusinessType())){
                    SMS_5G+=1;
                }else if("INTERNATIONAL_SMS".equals(accountBasicInfoValidator.getBusinessType())){
                    INTERNATIONAL_SMS+=1;
                }else if("BLACK_SERVICE".equals(accountBasicInfoValidator.getBusinessType())){
                    BLACK_SERVICE+=1;
                }
            }
        }

        view.addObject("enterpriseBasicInfoValidator", data.getData());
        view.addObject("list", info.getData());
        view.addObject("TEXT_SMS", TEXT_SMS);
        view.addObject("MULTI_SMS", MULTI_SMS);
        view.addObject("MMS", MMS);
        view.addObject("SMS_5G", SMS_5G);
        view.addObject("INTERNATIONAL_SMS", INTERNATIONAL_SMS);
        view.addObject("BLACK_SERVICE", BLACK_SERVICE);

        return view;

    }

    /**
     * EC业务账号查看中心
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{accountId}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_view_center");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> info = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }

        view.addObject("accountId", info.getData().getAccountId());
        view.addObject("enterpriseId", info.getData().getEnterpriseId());
        return view;

    }

    /**
     * EC业务账号查看中心
     *
     * @return
     */
    @RequestMapping(value = "/view/base/{accountId}", method = RequestMethod.GET)
    public ModelAndView view_base(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_view_base");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        /**
         * 查询账号信息
         */
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        /**
         * 查询财务信息
         */
        AccountFinanceInfoValidator accountFinanceInfoValidator = new AccountFinanceInfoValidator();
        accountFinanceInfoValidator.setAccountId(data.getData().getAccountId());
        accountFinanceInfoValidator.setCarrier(data.getData().getCarrier());
        if("INTL".equals(data.getData().getCarrier())){
            accountFinanceInfoValidator.setCarrier(data.getData().getCountryCode());
        }
        ResponseData<Map<String, BigDecimal>> map = accountFinanceService.editCarrierPrice(accountFinanceInfoValidator);
        view.addObject("list", map.getData());
        //查询账号配置的运营商价格
        ResponseData<List<AccountFinanceInfoValidator>> list = accountFinanceService.findByAccountId(accountFinanceInfoValidator);
        if(!StringUtils.isEmpty(list.getData()) && list.getData().size()>0){
            accountFinanceInfoValidator = list.getData().get(0);
        }

        /**
         * 查询接口信息
         */
        ResponseData<AccountInterfaceInfoValidator> accountInterfaceInfoValidator = accountInterfaceService.findById(data.getData().getAccountId());
        if(StringUtils.isEmpty(accountInterfaceInfoValidator.getData())){
            view.addObject("accountInterfaceInfoValidator", new AccountInterfaceInfoValidator());
        }else{
            view.addObject("accountInterfaceInfoValidator",  accountInterfaceInfoValidator.getData());
        }

        /**
         * 查询通道
         */
        AccountChannelInfoQo accountChannelInfoQo = new AccountChannelInfoQo();
        accountChannelInfoQo.setAccountId(data.getData().getAccountId());
        accountChannelInfoQo.setCarrier(data.getData().getCarrier());
        accountChannelInfoQo.setAccountChannelType(data.getData().getAccountChannelType());
        ResponseData<Map<String, AccountChannelInfoQo>> channelData = accountChannelService.findAccountChannelConfig(accountChannelInfoQo);
        if (!ResponseCode.SUCCESS.getCode().equals(channelData.getCode())) {
            view.addObject("error", channelData.getCode() + ":" + channelData.getMessage());
            return view;
        }

        view.addObject("accountBasicInfoValidator", data.getData());
        view.addObject("accountFinanceInfoValidator", accountFinanceInfoValidator);
        view.addObject("channelData", channelData.getData());

        return view;

    }

    /**
     * EC业务账号查看发送量统计
     *
     * @return
     */
    @RequestMapping(value = "/view/statisticSend/{accountId}", method = RequestMethod.GET)
    public ModelAndView statisticSend(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_view_statistic_send");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        /**
         * 查询账号信息
         */
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountId", accountId);

        return view;
    }

    /**
     * EC业务账号查看发送量统计
     *
     * @return
     */
    @RequestMapping(value = "/view/statisticSendMonth/{accountId}/{type}", method = RequestMethod.GET)
    public AccountStatisticSendData statisticSendMonth(@PathVariable String accountId, @PathVariable String type,HttpServletRequest request) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            return new AccountStatisticSendData();
        }

        /**
         * 查询账号信息
         */
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return new AccountStatisticSendData();
        }

        AccountStatisticSendData statisticSendData = new AccountStatisticSendData();
        statisticSendData.setAccountId(accountId);
        statisticSendData.setDimension(type);

        statisticSendData = businessAccountService.statisticAccountSendNumber(statisticSendData);

        return statisticSendData;
    }

    /**
     * EC业务账号投诉率统计
     *
     * @return
     */
    @RequestMapping(value = "/view/statisticComplaint/{accountId}", method = RequestMethod.GET)
    public ModelAndView statisticComplaint(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_view_statistic_complaint");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        /**
         * 查询账号信息
         */
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountId", accountId);

        return view;
    }

    /**
     * EC业务账号投诉率统计
     *
     * @return
     */
    @RequestMapping(value = "/view/statisticComplaintMonth/{accountId}", method = RequestMethod.GET)
    public AccountStatisticComplaintData statisticComplaintMonth(@PathVariable String accountId, HttpServletRequest request) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            return new AccountStatisticComplaintData();
        }

        /**
         * 查询账号信息
         */
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return new AccountStatisticComplaintData();
        }

        AccountStatisticComplaintData statisticComplaintData = new AccountStatisticComplaintData();
        statisticComplaintData.setAccountId(accountId);

        statisticComplaintData = businessAccountService.statisticComplaintMonth(statisticComplaintData);

        return statisticComplaintData;
    }

    /**
     * 生成excel：导出账号信息
     *
     * @return
     */
    @RequestMapping(value = "/excelAccountInfo/{accountId}", method = RequestMethod.GET)
    public void excelAccountInfo(@PathVariable String accountId, HttpServletRequest request, HttpServletResponse response) {

        /**
         * 查询账号信息
         */
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return ;
        }

        //账号信息
        AccountBasicInfoValidator accountBasicInfoValidator = data.getData();

        //查询导出数据信息
        Map<String, Object> buildMap = businessAccountService.buildExcelMap(accountBasicInfoValidator,request);

        OutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            //模板路径
            ClassPathResource classPathResource = new ClassPathResource("static/files/templates/" + "account.xlsx");
            InputStream fis = classPathResource.getInputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode( accountId + "账号信息.xlsx", "utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

            out = response.getOutputStream();
            bos = new BufferedOutputStream(out);

            //读取Excel
            ExcelWriter excelWriter = EasyExcel.write(bos).withTemplate(fis).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(0).build();
            WriteSheet writeSheet1 = EasyExcel.writerSheet(1).build();

            Map<String, Object> accountMap = new HashMap<>();
            accountMap.put("account", accountBasicInfoValidator.getAccountId());
            accountMap.put("webUrl", smocProperties.getWebUrl());
            excelWriter.fill(buildMap.get("messageMap"), writeSheet1);
            excelWriter.fill(buildMap.get("messageFiltersList"), writeSheet1);
            excelWriter.fill(accountMap, writeSheet);
            excelWriter.fill(buildMap.get("finance"), writeSheet);
            excelWriter.fill(buildMap.get("interface"), writeSheet);
            excelWriter.fill(buildMap.get("interfaceList"), writeSheet);
            excelWriter.fill(buildMap.get("webList"), writeSheet);

            excelWriter.finish();
            bos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 业务账号复制中心
     *
     * @return
     */
    @RequestMapping(value = "/center/accountCopy/{accountId}", method = RequestMethod.GET)
    public ModelAndView accountCopy(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_center_copy");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询账号
        ResponseData<AccountBasicInfoValidator> info = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }

        view.addObject("accountChannelType", info.getData().getAccountChannelType());
        view.addObject("enterpriseId", info.getData().getEnterpriseId());
        if("INTL".equals(info.getData().getCarrier())){
            view.addObject("flag", "international");
        }else{
            view.addObject("flag", "base");
        }
        view.addObject("accountId", accountId);

        return view;

    }

    /**
     * 业务账号复制
     *
     * @return
     */
    @RequestMapping(value = "/edit/copy/{accountId}", method = RequestMethod.GET)
    public ModelAndView copy(@PathVariable String accountId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_base");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        /**
         * 修改:查询数据
         */
        ResponseData<AccountBasicInfoValidator> info = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> data = enterpriseService.findById(info.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //生成业务账号
        ResponseData<String> bussinessAccountId = businessAccountService.createAccountId(data.getData().getEnterpriseFlag());

        //初始化数据
        AccountBasicInfoValidator accountBasicInfoValidator = info.getData();
        accountBasicInfoValidator.setAccountStatus("2");
        accountBasicInfoValidator.setAccountProcess("10000");
        accountBasicInfoValidator.setAccountId(data.getData().getEnterpriseFlag()+bussinessAccountId.getMessage());
        accountBasicInfoValidator.setAccountCopyId(accountId);

        view.addObject("op", "add");
        view.addObject("accountBasicInfoValidator", info.getData());
        view.addObject("enterpriseBasicInfoValidator", data.getData());

        if("INTL".equals(info.getData().getCarrier())){
            view.setViewName("customer/account/international/account_international_edit_base");
        }

        return view;

    }
}
