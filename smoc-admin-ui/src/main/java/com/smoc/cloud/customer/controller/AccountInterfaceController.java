package com.smoc.cloud.customer.controller;

import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountInterfaceInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.AccountInterfaceService;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.customer.service.EnterpriseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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


    /**
     * 查询配置运营商价格
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
        /*ResponseData<AccountInterfaceInfoValidator> interfaceData = accountInterfaceService.findByAccountId(data.getData().getAccountId());
        if (ResponseCode.SUCCESS.getCode().equals(interfaceData.getCode()) && !StringUtils.isEmpty(interfaceData.getData())) {
            accountInterfaceInfoValidator = interfaceData.getData();
            view.addObject("op", "edit");
        }else{
            view.addObject("op", "add");
        }*/

        view.addObject("accountInterfaceInfoValidator", accountInterfaceInfoValidator);
        view.addObject("accountBasicInfoValidator", data.getData());
        view.addObject("enterpriseBasicInfoValidator", enterpriseData.getData());

        return view;
    }

}
