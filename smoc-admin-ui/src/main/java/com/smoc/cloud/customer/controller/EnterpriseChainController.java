package com.smoc.cloud.customer.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseChainInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseDocumentInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.SystemAttachmentValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseChainService;
import com.smoc.cloud.customer.service.EnterpriseDocumentService;
import com.smoc.cloud.customer.service.EnterpriseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 签名合同链管理
 */
@Slf4j
@RestController
@RequestMapping("/ec/customer")
public class EnterpriseChainController {

    @Autowired
    private EnterpriseDocumentService enterpriseDocumentService;

    @Autowired
    private EnterpriseChainService enterpriseChainService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private SystemUserLogService systemUserLogService;


    /**
     * 合同链列表
     * @return
     */
    @RequestMapping(value = "/material/chain/list/{id}", method = RequestMethod.GET)
    public ModelAndView chainList(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/material/customer_material_chain_list");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询资质信息
        ResponseData<EnterpriseDocumentInfoValidator> infoDate = enterpriseDocumentService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        EnterpriseChainInfoValidator enterpriseChainInfoValidator = new EnterpriseChainInfoValidator();
        enterpriseChainInfoValidator.setDocumentId(infoDate.getData().getId());

        //查询
        ResponseData<List<EnterpriseChainInfoValidator>> data = enterpriseChainService.page(enterpriseChainInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("list", data.getData());
        view.addObject("enterpriseDocumentInfoValidator", infoDate.getData());

        return view;

    }

    /**
     * 新建合同链
     * @return
     */
    @RequestMapping(value = "/material/chain/add/{id}", method = RequestMethod.GET)
    public ModelAndView addChain(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/material/customer_material_chain_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询资质信息
        ResponseData<EnterpriseDocumentInfoValidator> infoDate = enterpriseDocumentService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(infoDate.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
        }

        //初始化数据
        EnterpriseChainInfoValidator enterpriseChainInfoValidator = new EnterpriseChainInfoValidator();
        enterpriseChainInfoValidator.setDocumentId(infoDate.getData().getId());
        enterpriseChainInfoValidator.setId(UUID.uuid32());
        enterpriseChainInfoValidator.setSignChainStatus("1");

        view.addObject("op", "add");
        view.addObject("enterpriseChainInfoValidator", enterpriseChainInfoValidator);
        view.addObject("enterpriseDocumentInfoValidator", infoDate.getData());
        view.addObject("enterpriseBasicInfoValidator", enterpriseData.getData());

        return view;

    }

    /**
     * 编辑合同链
     * @return
     */
    @RequestMapping(value = "/material/chain/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editChain(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/material/customer_material_chain_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<EnterpriseChainInfoValidator> data = enterpriseChainService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询资质信息
        ResponseData<EnterpriseDocumentInfoValidator> infoDate = enterpriseDocumentService.findById(data.getData().getDocumentId());
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(infoDate.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
        }

        view.addObject("op", "edit");
        view.addObject("enterpriseChainInfoValidator", data.getData());
        view.addObject("enterpriseDocumentInfoValidator", infoDate.getData());
        view.addObject("enterpriseBasicInfoValidator", enterpriseData.getData());

        return view;

    }

    /**
     * 保存
     * @param enterpriseChainInfoValidator
     * @param result
     * @param op
     * @param request
     * @return
     */
    @RequestMapping(value = "/material/chain/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated EnterpriseChainInfoValidator enterpriseChainInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/material/customer_material_chain_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            //查询资质信息
            ResponseData<EnterpriseDocumentInfoValidator> infoDate = enterpriseDocumentService.findById(enterpriseChainInfoValidator.getDocumentId());
            if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
                view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
                return view;
            }
            //查询企业数据
            ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(infoDate.getData().getEnterpriseId());
            if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
                view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
            }
            view.addObject("enterpriseDocumentInfoValidator", enterpriseChainInfoValidator);
            view.addObject("enterpriseDocumentInfoValidator", infoDate.getData());
            view.addObject("enterpriseBasicInfoValidator", enterpriseData.getData());
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            enterpriseChainInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            enterpriseChainInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            enterpriseChainInfoValidator.setUpdatedTime(new Date());
            enterpriseChainInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = enterpriseChainService.save(enterpriseChainInfoValidator,op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("EC_DOCUMENT", enterpriseChainInfoValidator.getDocumentId(), "add".equals(op) ? enterpriseChainInfoValidator.getCreatedBy() : enterpriseChainInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加签名合同链" : "修改签名合同链", JSON.toJSONString(enterpriseChainInfoValidator));
        }

        //记录日志
        log.info("[签名合同链管理][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(enterpriseChainInfoValidator));

        view.setView(new RedirectView("/ec/customer/material/chain/list/"+enterpriseChainInfoValidator.getDocumentId(), true, false));
        return view;

    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/material/chain/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/material/customer_material_chain_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<EnterpriseChainInfoValidator> infoDate = enterpriseChainService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        //删除操作
        ResponseData data = enterpriseChainService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("EC_DOCUMENT", infoDate.getData().getDocumentId(), user.getRealName(), "delete", "删除签名合同链" , JSON.toJSONString(infoDate.getData()));
        }

        //记录日志
        log.info("[签名合同链管理][{}][{}]数据:{}", "delete", user.getUserName(), JSON.toJSONString(infoDate.getData()));

        view.setView(new RedirectView("/ec/customer/material/chain/list/"+infoDate.getData().getDocumentId(), true, false));
        return view;
    }

    /**
     * 查询签名合同链
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/material/chain/chainView/{id}", produces = "text/html;charset=utf-8", method = RequestMethod.GET)
    public String findByModelId(@PathVariable String id, HttpServletRequest request) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return MpmValidatorUtil.validateMessage(validator);
        }

        EnterpriseChainInfoValidator enterpriseChainInfoValidator = new EnterpriseChainInfoValidator();
        enterpriseChainInfoValidator.setDocumentId(id);

        //查询
        ResponseData<List<EnterpriseChainInfoValidator>> data = enterpriseChainService.page(enterpriseChainInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return "无签名合同链";
        }

        //查询附件
        StringBuilder chain = new StringBuilder();
        if (!StringUtils.isEmpty(data.getData()) && data.getData().size() > 0) {
            List<EnterpriseChainInfoValidator> list = data.getData();
            for (int a=0;a<list.size();a++) {
                EnterpriseChainInfoValidator info = list.get(a);
                chain.append(info.getSignChain()+","+info.getSignDate()+" - "+info.getSignExpireDate());
                if (a != list.size()-1) {
                    chain.append("@");
                }
            }
            return chain.toString();
        }

        return "无签名合同链";
    }
}
