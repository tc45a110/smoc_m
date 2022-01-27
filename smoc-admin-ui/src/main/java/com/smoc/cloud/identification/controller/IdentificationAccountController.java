package com.smoc.cloud.identification.controller;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationAccountInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.StringRandom;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.finance.service.FinanceAccountService;
import com.smoc.cloud.identification.model.AccountExcelModel;
import com.smoc.cloud.identification.service.IdentificationAccountInfoService;
import com.smoc.cloud.sequence.service.SequenceService;
import com.smoc.cloud.utils.ExcelUtils;
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
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 认账账号管理
 */
@Slf4j
@Controller
@RequestMapping("/identification/account")
public class IdentificationAccountController {

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private IdentificationAccountInfoService identificationAccountInfoService;

    /**
     * 认账账号管理列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("identification/account/identification_account_list");

        //初始化数据
        PageParams<IdentificationAccountInfoValidator> params = new PageParams<IdentificationAccountInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        IdentificationAccountInfoValidator identificationAccountInfoValidator = new IdentificationAccountInfoValidator();
        params.setParams(identificationAccountInfoValidator);

        //查询
        ResponseData<PageList<IdentificationAccountInfoValidator>> data = identificationAccountInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("identificationAccountInfoValidator", identificationAccountInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 认账账号管理列表分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute IdentificationAccountInfoValidator identificationAccountInfoValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("identification/account/identification_account_list");
        //分页查询
        pageParams.setParams(identificationAccountInfoValidator);

        ResponseData<PageList<IdentificationAccountInfoValidator>> data = identificationAccountInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("identificationAccountInfoValidator", identificationAccountInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 认账账号开通
     *
     * @return
     */
    @RequestMapping(value = "/add/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String enterpriseId, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("identification/account/identification_account_edit");

        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(enterpriseId);
        IdentificationAccountInfoValidator identificationAccountInfoValidator = new IdentificationAccountInfoValidator();

        identificationAccountInfoValidator.setEnterpriseId(enterpriseData.getData().getEnterpriseId());
        identificationAccountInfoValidator.setEnterpriseName(enterpriseData.getData().getEnterpriseName());
        identificationAccountInfoValidator.setAccountStatus("001");
        identificationAccountInfoValidator.setAccountType("1");

        //自动生成认证账号
        String identificationAccount = "XYIA"+sequenceService.findSequence("BUSINESS_ACCOUNT");
        identificationAccountInfoValidator.setId(identificationAccount);
        identificationAccountInfoValidator.setIdentificationAccount(identificationAccount);

        //自动生成md5Hmac密钥
        String md5HmacKey = StringRandom.getStringRandom(32);
        identificationAccountInfoValidator.setMd5HmacKey(md5HmacKey);

        //自动生成AES密钥
        String aesKey = StringRandom.getStringRandom(32);
        identificationAccountInfoValidator.setAesKey(aesKey);

        //自动生成AESIV
        String aesIv = StringRandom.getStringRandom(16);
        identificationAccountInfoValidator.setAesIv(aesIv);

        identificationAccountInfoValidator.setGrantingCredit(new BigDecimal("0.00"));

        view.addObject("identificationAccountInfoValidator", identificationAccountInfoValidator);
        view.addObject("op", "add");

        return view;

    }

    /**
     * 保存认账账号
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated IdentificationAccountInfoValidator identificationAccountInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("identification/account/identification_account_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            //查询销售人员
            view.addObject("identificationAccountInfoValidator", identificationAccountInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            identificationAccountInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            identificationAccountInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            identificationAccountInfoValidator.setUpdatedTime(new Date());
            identificationAccountInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }


        //保存数据
        ResponseData data = identificationAccountInfoService.save(identificationAccountInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("IDENTIFICATION_ACCOUNT", identificationAccountInfoValidator.getId(), user.getUserName(), op, "add".equals(op) ? "认证账号开通" : "修改认证账号", JSON.toJSONString(identificationAccountInfoValidator));
        }

        //记录日志
        log.info("[认证账户][认证账户开户信息][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(identificationAccountInfoValidator));

        view.setView(new RedirectView("/identification/account/list", true, false));
        return view;

    }

    /**
     * 认账账号修改
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("identification/account/identification_account_edit");
        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //修改:查询数据
        ResponseData<IdentificationAccountInfoValidator> data = identificationAccountInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        IdentificationAccountInfoValidator identificationAccountInfoValidator = data.getData();

        view.addObject("identificationAccountInfoValidator", identificationAccountInfoValidator);
        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        return view;

    }

    /**
     * 认账账号产看
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{id}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("identification/account/identification_account_view_center");

        //查询认证账户数据
        ResponseData<IdentificationAccountInfoValidator> data = identificationAccountInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //查询认证财务账户数据
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
        view.addObject("identificationAccountInfoValidator", data.getData());

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
        ResponseData<IdentificationAccountInfoValidator> data = identificationAccountInfoService.findById(id);
        IdentificationAccountInfoValidator identificationAccountInfoValidator = data.getData();

        CopyOnWriteArrayList<AccountExcelModel> list = new CopyOnWriteArrayList<>();
        AccountExcelModel excelModel = new AccountExcelModel();
        excelModel.setKey("身份认证账号");
        excelModel.setValue(identificationAccountInfoValidator.getIdentificationAccount());
        list.add(excelModel);
        AccountExcelModel excelModel1 = new AccountExcelModel();
        excelModel1.setKey("MD5-HMAC-KEY密钥");
        excelModel1.setValue(identificationAccountInfoValidator.getMd5HmacKey());
        list.add(excelModel1);
        AccountExcelModel excelModel2 = new AccountExcelModel();
        excelModel2.setKey("AES-KEY密钥");
        excelModel2.setValue(identificationAccountInfoValidator.getAesKey());
        list.add(excelModel2);
        AccountExcelModel excelModel3 = new AccountExcelModel();
        excelModel3.setKey("AES-IV偏移量");
        excelModel3.setValue(identificationAccountInfoValidator.getAesIv());
        list.add(excelModel3);

        AccountExcelModel excelModel4 = new AccountExcelModel();
        excelModel4.setKey("技术对接说明");
        excelModel4.setValue("用MD5-HMAC签名；AES-256对敏感数据身份证号进行加密；AES加密模式:AES/CBC/PKCS7Padding；AES_NAME为AES");
        list.add(excelModel4);

        String fileName = identificationAccountInfoValidator.getIdentificationAccount();
        try {
            ExcelUtils.writeExcel(fileName, AccountExcelModel.class ,response,list);
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
        ModelAndView view = new ModelAndView("identification/account/identification_account_list");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        ResponseData data = identificationAccountInfoService.logout(id);

        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("IDENTIFICATION_ACCOUNT", id, user.getUserName(), "logout", "注销认证账户", id);
        }

        view.setView(new RedirectView("/identification/account/list", true, false));
        return view;

    }

}
