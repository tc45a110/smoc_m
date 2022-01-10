package com.smoc.cloud.customer.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseExpressInfoValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseExpressService;
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
import java.util.Date;


@Slf4j
@Controller
@RequestMapping("/enterprise/express")
public class EnterpriseExpressController {

    @Autowired
    private EnterpriseExpressService enterpriseExpressService;

    @Autowired
    private SystemUserLogService systemUserLogService;


    /**
     * 保存邮寄信息
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated EnterpriseExpressInfoValidator enterpriseExpressInfoValidator, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/enterprise/enterprise_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        String op = "add";
        if(!StringUtils.isEmpty(enterpriseExpressInfoValidator.getExpressId())){
            enterpriseExpressInfoValidator.setId(enterpriseExpressInfoValidator.getExpressId());
            op = "edit";
        }

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(enterpriseExpressInfoValidator));
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            enterpriseExpressInfoValidator.setId(UUID.uuid32());
            enterpriseExpressInfoValidator.setPostStatus("1");
            enterpriseExpressInfoValidator.setCreatedTime(new Date());
            enterpriseExpressInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            enterpriseExpressInfoValidator.setUpdatedTime(new Date());
            enterpriseExpressInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = enterpriseExpressService.save(enterpriseExpressInfoValidator, op);
        if (!StringUtils.isEmpty(data) && !ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("ENTERPRISE_EXPRESS", enterpriseExpressInfoValidator.getEnterpriseId(), "add".equals(op) ? enterpriseExpressInfoValidator.getCreatedBy() : enterpriseExpressInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加邮寄信息-"+enterpriseExpressInfoValidator.getPostContacts():"修改邮寄信息-"+enterpriseExpressInfoValidator.getPostContacts() , JSON.toJSONString(enterpriseExpressInfoValidator));
        }

        //记录日志
        log.info("[企业接入][{}][{}][{}]数据:{}", "add".equals(op) ? "添加邮寄信息-"+enterpriseExpressInfoValidator.getPostContacts():"修改邮寄信息-"+enterpriseExpressInfoValidator.getPostContacts(),op, user.getUserName(), JSON.toJSONString(enterpriseExpressInfoValidator));

        view.setView(new RedirectView("/enterprise/center/"+enterpriseExpressInfoValidator.getEnterpriseId(), true, false));
        return view;

    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/enterprise/enterprise_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询邮寄信息
        ResponseData<EnterpriseExpressInfoValidator> expressData = enterpriseExpressService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(expressData.getCode())) {
            view.addObject("error", expressData.getCode() + ":" + expressData.getMessage());
            return view;
        }

        //删除操作
        ResponseData data = enterpriseExpressService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("ENTERPRISE_EXPRESS", expressData.getData().getEnterpriseId(), user.getRealName(), "delete", "删除邮寄信息-"+expressData.getData().getPostContacts() , JSON.toJSONString(expressData.getData()));
        }

        //记录日志
        log.info("[企业接入][{}][{}][{}]数据:{}", "删除邮寄信息-"+expressData.getData().getPostContacts(),"delete", user.getUserName(), JSON.toJSONString(expressData.getData()));

        view.setView(new RedirectView("/enterprise/center/"+expressData.getData().getEnterpriseId(), true, false));
        return view;
    }
}
