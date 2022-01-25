package com.smoc.cloud.customer.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountInterfaceInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.utils.DES;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.AccountInterfaceService;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.customer.service.EnterpriseService;
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
 * 业务账号接口配置
 **/
@Slf4j
@RestController
@RequestMapping("/account")
public class AccountInterfaceController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private AccountInterfaceService accountInterfaceService;

    @Autowired
    private SystemUserLogService systemUserLogService;


    /**
     * 查询接口配置
     *
     * @return
     */
    @RequestMapping(value = "/edit/interface/{accountId}", method = RequestMethod.GET)
    public ModelAndView finance(@PathVariable String accountId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/account/account_edit_interface");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询业务账号
        ResponseData<AccountBasicInfoValidator> data = businessAccountService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(data.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
        }

        AccountInterfaceInfoValidator accountInterfaceInfoValidator = new AccountInterfaceInfoValidator();
        accountInterfaceInfoValidator.setAccountId(accountId);

        //查询接口信息
        ResponseData<AccountInterfaceInfoValidator> interfaceData = accountInterfaceService.findById(data.getData().getAccountId());
        if (ResponseCode.SUCCESS.getCode().equals(interfaceData.getCode()) && !StringUtils.isEmpty(interfaceData.getData())) {
            accountInterfaceInfoValidator = interfaceData.getData();
            view.addObject("op", "edit");
        }else{
            view.addObject("op", "add");
        }

        view.addObject("accountInterfaceInfoValidator", accountInterfaceInfoValidator);
        view.addObject("enterpriseBasicInfoValidator", enterpriseData.getData());

        return view;
    }

    /**
     * 保存接口信息
     * @param accountInterfaceInfoValidator
     * @param result
     * @param op
     * @param request
     * @return
     */
    @RequestMapping(value = "/interface/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated AccountInterfaceInfoValidator accountInterfaceInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/account/account_edit_interface");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("accountBasicInfoValidator", accountInterfaceInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            accountInterfaceInfoValidator.setAccountId(accountInterfaceInfoValidator.getAccountId());
            accountInterfaceInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            accountInterfaceInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            accountInterfaceInfoValidator.setUpdatedTime(new Date());
            accountInterfaceInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = accountInterfaceService.save(accountInterfaceInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("ACCOUNT_INTERFACE", accountInterfaceInfoValidator.getAccountId(), "add".equals(op) ? accountInterfaceInfoValidator.getCreatedBy() : accountInterfaceInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加业务账号接口信息" : "修改业务账号接口信息", JSON.toJSONString(accountInterfaceInfoValidator));
        }

        //记录日志
        log.info("[EC业务账号管理][业务账号接口信息][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(accountInterfaceInfoValidator));

        view.setView(new RedirectView("/account/edit/interface/"+accountInterfaceInfoValidator.getAccountId(), true, false));

        return view;
    }

    /**
     * 查询配置运营商价格
     *
     * @return
     */
    @RequestMapping(value = "/interface/lookPassword/{accountId}", method = RequestMethod.GET)
    public String lookPassword(@PathVariable String accountId) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(accountId);
        if (!MpmValidatorUtil.validate(validator)) {
            return MpmValidatorUtil.validateMessage(validator);
        }

        //查询接口信息
        ResponseData<AccountInterfaceInfoValidator> interfaceData = accountInterfaceService.findById(accountId);
        if (!ResponseCode.SUCCESS.getCode().equals(interfaceData.getCode())) {
            return interfaceData.getMessage();
        }

        return DES.decrypt(interfaceData.getData().getAccountPassword());//解密

    }
}
