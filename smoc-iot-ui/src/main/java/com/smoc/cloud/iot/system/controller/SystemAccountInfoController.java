package com.smoc.cloud.iot.system.controller;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.smoc.system.SystemAccountInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.StringRandom;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.iot.system.model.AccountExcelModel;
import com.smoc.cloud.iot.utils.ExcelUtils;
import com.smoc.cloud.iot.sequence.service.SequenceService;
import com.smoc.cloud.iot.system.service.EnterpriseService;
import com.smoc.cloud.iot.system.service.FinanceAccountService;
import com.smoc.cloud.iot.system.service.SystemAccountInfoService;
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
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 账号管理
 */
@Slf4j
@Controller
@RequestMapping("/iot/account")
public class SystemAccountInfoController {

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private SystemAccountInfoService systemAccountInfoService;

    //通过这个字段，区分 变更账号类型;此处为智能短信账号
    private String businessType = "IOT_ACCOUNT";

    /**
     * 账号管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("system/account/account_list");

        //初始化数据
        PageParams<SystemAccountInfoValidator> params = new PageParams<SystemAccountInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        SystemAccountInfoValidator systemAccountInfoValidator = new SystemAccountInfoValidator();
        systemAccountInfoValidator.setBusinessType(this.businessType);
        params.setParams(systemAccountInfoValidator);

        //查询
        ResponseData<PageList<SystemAccountInfoValidator>> data = systemAccountInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemAccountInfoValidator", systemAccountInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 账号管理列表分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute SystemAccountInfoValidator systemAccountInfoValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("system/account/account_list");
        //分页查询
        systemAccountInfoValidator.setBusinessType(this.businessType);
        pageParams.setParams(systemAccountInfoValidator);

        ResponseData<PageList<SystemAccountInfoValidator>> data = systemAccountInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemAccountInfoValidator", systemAccountInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 账号管理查询列表
     *
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView search_list() {
        ModelAndView view = new ModelAndView("system/account/account_search_list");

        //初始化数据
        PageParams<SystemAccountInfoValidator> params = new PageParams<SystemAccountInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        SystemAccountInfoValidator systemAccountInfoValidator = new SystemAccountInfoValidator();
        systemAccountInfoValidator.setBusinessType(this.businessType);
        params.setParams(systemAccountInfoValidator);

        //查询
        ResponseData<PageList<SystemAccountInfoValidator>> data = systemAccountInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemAccountInfoValidator", systemAccountInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 账号管理查询列表
     *
     * @return
     */
    @RequestMapping(value = "/search/page", method = RequestMethod.POST)
    public ModelAndView search_page(@ModelAttribute SystemAccountInfoValidator systemAccountInfoValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("system/account/account_search_list");
        //分页查询
        systemAccountInfoValidator.setBusinessType(this.businessType);
        pageParams.setParams(systemAccountInfoValidator);

        ResponseData<PageList<SystemAccountInfoValidator>> data = systemAccountInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemAccountInfoValidator", systemAccountInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 账号开通
     *
     * @return
     */
    @RequestMapping(value = "/add/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String enterpriseId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("system/account/account_edit");

        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(enterpriseId);
        SystemAccountInfoValidator systemAccountInfoValidator = new SystemAccountInfoValidator();

        systemAccountInfoValidator.setEnterpriseId(enterpriseData.getData().getEnterpriseId());
        systemAccountInfoValidator.setEnterpriseName(enterpriseData.getData().getEnterpriseName());
        //默认有效状态
        systemAccountInfoValidator.setAccountStatus("1");
        //默认测试账号
        systemAccountInfoValidator.setAccountType("1");

        //自动生成账号
        String account = "IOT" + sequenceService.findSequence("BUSINESS_ACCOUNT");
        systemAccountInfoValidator.setId(account);
        systemAccountInfoValidator.setAccount(account);

        //自动生成md5Hmac密钥
        String md5HmacKey = StringRandom.getStringRandom(32);
        systemAccountInfoValidator.setMd5HmacKey(md5HmacKey);

        //自动生成AES密钥
        String aesKey = StringRandom.getStringRandom(32);
        systemAccountInfoValidator.setAesKey(aesKey);

        systemAccountInfoValidator.setSubmitLimiter(5);

        //自动生成AESIV
        String aesIv = StringRandom.getStringRandom(16);
        systemAccountInfoValidator.setAesIv(aesIv);

        systemAccountInfoValidator.setPrice(new BigDecimal("0.00"));
        systemAccountInfoValidator.setSecondPrice(new BigDecimal("0.00"));
        systemAccountInfoValidator.setThirdPrice(new BigDecimal("0.00"));
        systemAccountInfoValidator.setGrantingCredit(new BigDecimal("0.00"));
        systemAccountInfoValidator.setIsGateway("1");
        systemAccountInfoValidator.setBusinessType(this.businessType);

        view.addObject("systemAccountInfoValidator", systemAccountInfoValidator);
        view.addObject("op", "add");

        return view;

    }

    /**
     * 保存账号
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated SystemAccountInfoValidator systemAccountInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("system/account/account_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("systemAccountInfoValidator", systemAccountInfoValidator);
            view.addObject("op", op);
            return view;
        }

        systemAccountInfoValidator.setBusinessType(this.businessType);
        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            systemAccountInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            systemAccountInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            systemAccountInfoValidator.setUpdatedTime(new Date());
            systemAccountInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }


        //保存数据
        ResponseData data = systemAccountInfoService.save(systemAccountInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync(businessType, systemAccountInfoValidator.getId(), user.getRealName(), op, "add".equals(op) ? "账号开通" : "修改账号", JSON.toJSONString(systemAccountInfoValidator));
        }

        //记录日志
        log.info("[系统账户][账户开户信息][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(systemAccountInfoValidator));

        view.setView(new RedirectView("/iot/account/list", true, false));
        return view;

    }

    /**
     * 账号修改
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("system/account/account_edit");
        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //修改:查询数据
        ResponseData<SystemAccountInfoValidator> data = systemAccountInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        SystemAccountInfoValidator systemAccountInfoValidator = data.getData();
        systemAccountInfoValidator.setBusinessType(this.businessType);

        view.addObject("systemAccountInfoValidator", systemAccountInfoValidator);
        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        return view;

    }

    /**
     * 账号产看
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{id}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("system/account/account_view_center");

        //查询账户数据
        ResponseData<SystemAccountInfoValidator> data = systemAccountInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //查询财务账户数据
        ResponseData<FinanceAccountValidator> finance = financeAccountService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(finance.getCode())) {
            view.addObject("error", finance.getCode() + ":" + finance.getMessage());
        }

        ResponseData<EnterpriseBasicInfoValidator> enterprise = enterpriseService.findById(data.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterprise.getCode())) {
            view.addObject("error", enterprise.getCode() + ":" + enterprise.getMessage());
        }

        view.addObject("enterprise", enterprise.getData());
        view.addObject("financeAccountValidator", finance.getData());
        view.addObject("systemAccountInfoValidator", data.getData());

        return view;
    }


    /**
     * 生成excel
     *
     * @return
     */
    @RequestMapping(value = "/excel/{id}", method = RequestMethod.GET)
    public void view(@PathVariable String id, HttpServletResponse response) {

        //查询认证账户数据
        ResponseData<SystemAccountInfoValidator> data = systemAccountInfoService.findById(id);
        SystemAccountInfoValidator systemAccountInfoValidator = data.getData();

        CopyOnWriteArrayList<AccountExcelModel> list = new CopyOnWriteArrayList<>();
        AccountExcelModel excelModel = new AccountExcelModel();
        excelModel.setKey("账号");
        excelModel.setValue(systemAccountInfoValidator.getAccount());
        list.add(excelModel);
        AccountExcelModel excelModel1 = new AccountExcelModel();
        excelModel1.setKey("MD5-HMAC-KEY密钥");
        excelModel1.setValue(systemAccountInfoValidator.getMd5HmacKey());
        list.add(excelModel1);
//        AccountExcelModel excelModel2 = new AccountExcelModel();
//        excelModel2.setKey("AES(256)-KEY密钥");
//        excelModel2.setValue(systemAccountInfoValidator.getAesKey());
//        list.add(excelModel2);
//        AccountExcelModel excelModel3 = new AccountExcelModel();
//        excelModel3.setKey("AES(256)-IV偏移量");
//        excelModel3.setValue(systemAccountInfoValidator.getAesIv());
//        list.add(excelModel3);

        AccountExcelModel excelModel4 = new AccountExcelModel();
        excelModel4.setKey("提交速率（次/秒）");
        excelModel4.setValue(systemAccountInfoValidator.getSubmitLimiter() + "");
        list.add(excelModel4);

        AccountExcelModel excelModel8 = new AccountExcelModel();
        excelModel8.setKey("IP限制");
        excelModel8.setValue(systemAccountInfoValidator.getIdentifyIp());
        list.add(excelModel8);

        AccountExcelModel excelModel5 = new AccountExcelModel();
        excelModel5.setKey("技术对接说明");
//        excelModel5.setValue("用MD5-HMAC签名,AES(256)对敏感数据身份证号进行加密,AES(256)加密模式:AES/CBC/PKCS7Padding,AES_NAME为AES");
        excelModel5.setValue("用MD5-HMAC签名");
        list.add(excelModel5);

        AccountExcelModel excelModel6 = new AccountExcelModel();
        excelModel6.setKey("友好提示");
        excelModel6.setValue("请妥善报关该资料，请无泄露，如有泄露，自行负责");
        list.add(excelModel6);

        String fileName = systemAccountInfoValidator.getAccount();
        try {
            ExcelUtils.writeExcel(fileName, AccountExcelModel.class, response, list);
        } catch (Exception e) {
            log.error("导出excel表格失败:", e);
        }
    }

    /**
     * 认账账号注销
     *
     * @return
     */
    @RequestMapping(value = "/logout/{id}", method = RequestMethod.GET)
    public ModelAndView logout(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("system/account/account_list");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        ResponseData data = systemAccountInfoService.logout(id);

        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync(businessType, id, user.getRealName(), "logout", "注销账户", id);
        }

        view.setView(new RedirectView("/iot/account/list", true, false));
        return view;

    }

}
