package com.smoc.cloud.customer.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseExpressInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseInvoiceInfoValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseExpressService;
import com.smoc.cloud.customer.service.EnterpriseInvoiceService;
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
@RequestMapping("/enterprise/invoice")
public class EnterpriseInvoiceController {

    @Autowired
    private EnterpriseInvoiceService enterpriseInvoiceService;

    @Autowired
    private SystemUserLogService systemUserLogService;


    /**
     * 保存邮寄信息
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated EnterpriseInvoiceInfoValidator enterpriseInvoiceInfoValidator, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/enterprise/enterprise_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        String op = "add";
        if("1".equals(enterpriseInvoiceInfoValidator.getInvoiceType()) && !StringUtils.isEmpty(enterpriseInvoiceInfoValidator.getCommonId())){
            enterpriseInvoiceInfoValidator.setId(enterpriseInvoiceInfoValidator.getCommonId());
            op = "edit";
        }
        if("2".equals(enterpriseInvoiceInfoValidator.getInvoiceType()) && !StringUtils.isEmpty(enterpriseInvoiceInfoValidator.getSpecialId())){
            enterpriseInvoiceInfoValidator.setId(enterpriseInvoiceInfoValidator.getSpecialId());
            op = "edit";
        }

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(enterpriseInvoiceInfoValidator));
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            enterpriseInvoiceInfoValidator.setId(UUID.uuid32());
            enterpriseInvoiceInfoValidator.setInvoiceStatus("1");
            enterpriseInvoiceInfoValidator.setCreatedTime(new Date());
            enterpriseInvoiceInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            enterpriseInvoiceInfoValidator.setUpdatedTime(new Date());
            enterpriseInvoiceInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = enterpriseInvoiceService.save(enterpriseInvoiceInfoValidator, op);
        if (!StringUtils.isEmpty(data) && !ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            if("1".equals(enterpriseInvoiceInfoValidator.getInvoiceType())){
                systemUserLogService.logsAsync("ENTERPRISE_INVOICE", enterpriseInvoiceInfoValidator.getEnterpriseId(), "add".equals(op) ? enterpriseInvoiceInfoValidator.getCreatedBy() : enterpriseInvoiceInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加普通发票":"修改普通发票" , JSON.toJSONString(enterpriseInvoiceInfoValidator));
            }else{
                systemUserLogService.logsAsync("ENTERPRISE_INVOICE", enterpriseInvoiceInfoValidator.getEnterpriseId(), "add".equals(op) ? enterpriseInvoiceInfoValidator.getCreatedBy() : enterpriseInvoiceInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加专用发票":"修改专用发票" , JSON.toJSONString(enterpriseInvoiceInfoValidator));
            }
        }

        //记录日志
        if("1".equals(enterpriseInvoiceInfoValidator.getInvoiceType())){
            log.info("[企业接入][{}][{}][{}]数据:{}", "add".equals(op) ? "添加普通发票":"修改普通发票",op, user.getUserName(), JSON.toJSONString(enterpriseInvoiceInfoValidator));
        }else{
            log.info("[企业接入][{}][{}][{}]数据:{}", "add".equals(op) ? "添加专用发票":"修改专用发票",op, user.getUserName(), JSON.toJSONString(enterpriseInvoiceInfoValidator));
        }

        view.setView(new RedirectView("/enterprise/center/"+enterpriseInvoiceInfoValidator.getEnterpriseId(), true, false));
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
        ResponseData<EnterpriseInvoiceInfoValidator> invoiceData = enterpriseInvoiceService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(invoiceData.getCode())) {
            view.addObject("error", invoiceData.getCode() + ":" + invoiceData.getMessage());
            return view;
        }

        //删除操作
        ResponseData data = enterpriseInvoiceService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            if("1".equals(invoiceData.getData().getInvoiceType())){
                systemUserLogService.logsAsync("ENTERPRISE_INVOICE", invoiceData.getData().getEnterpriseId(), user.getRealName(), "delete", "删除普通发票" , JSON.toJSONString(invoiceData.getData()));
            }else{
                systemUserLogService.logsAsync("ENTERPRISE_INVOICE", invoiceData.getData().getEnterpriseId(), user.getRealName(), "delete", "删除专用发票" , JSON.toJSONString(invoiceData.getData()));
            }
        }

        //记录日志
        if("1".equals(invoiceData.getData().getInvoiceType())){
            log.info("[企业接入][{}][{}][{}]数据:{}", "删除普通发票","delete", user.getUserName(), JSON.toJSONString(invoiceData.getData()));
        }else{
            log.info("[企业接入][{}][{}][{}]数据:{}", "删除专用发票","delete", user.getUserName(), JSON.toJSONString(invoiceData.getData()));
        }

        view.setView(new RedirectView("/enterprise/center/"+invoiceData.getData().getEnterpriseId(), true, false));
        return view;
    }
}
