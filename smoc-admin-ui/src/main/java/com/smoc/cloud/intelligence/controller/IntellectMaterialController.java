package com.smoc.cloud.intelligence.controller;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialTypeValidator;
import com.smoc.cloud.common.smoc.intelligence.IntellectMaterialValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.BusinessAccountService;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.intelligence.service.IntellectMaterialService;
import com.smoc.cloud.intelligence.service.IntellectMaterialTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 智能短信素材
 */
@Slf4j
@RestController
@RequestMapping("intel/material")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IntellectMaterialController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private IntellectMaterialService intellectMaterialService;


    @Autowired
    private IntellectMaterialTypeService intellectMaterialTypeService;

    /**
     * 根据业务账号查询素材分类（页面）
     *
     * @return
     */
    @RequestMapping(value = "/getMaterial/{parentId}/{materialTypeId}", method = RequestMethod.GET)
    public ModelAndView getMaterial(@PathVariable String parentId, @PathVariable String materialTypeId) {
        ModelAndView view = new ModelAndView("intelligence/resource/resource_list");
        ResponseData<AccountBasicInfoValidator> accountData = businessAccountService.findById(parentId);

        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(accountData.getData().getEnterpriseId());

        ResponseData<IntellectMaterialTypeValidator> materialTypeData = intellectMaterialTypeService.findById(materialTypeId);
        if (!ResponseCode.SUCCESS.getCode().equals(materialTypeData.getCode())) {
            view.addObject("error", materialTypeData.getCode() + ":" + materialTypeData.getMessage());
            return view;
        }

        ResponseData<List<IntellectMaterialValidator>> resourceData = intellectMaterialService.getMaterial(materialTypeId);
        if (!ResponseCode.SUCCESS.getCode().equals(resourceData.getCode())) {
            view.addObject("error", resourceData.getCode() + ":" + resourceData.getMessage());
            return view;
        }

        view.addObject("enterpriseName", enterpriseData.getData().getEnterpriseName());
        view.addObject("enterpriseId", enterpriseData.getData().getEnterpriseId());
        view.addObject("accountName", accountData.getData().getAccountName());
        view.addObject("accountId", accountData.getData().getAccountId());
        view.addObject("materialTypeId", materialTypeData.getData().getId());
        view.addObject("materialTypeTitle", materialTypeData.getData().getTitle());
        view.addObject("list", resourceData.getData());
        return view;
    }

    /**
     * 新建素材类别
     *
     * @return
     */
    @RequestMapping(value = "/add/{parentId}/{materialTypeId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String parentId, @PathVariable String materialTypeId) {
        ModelAndView view = new ModelAndView("intelligence/resource/resource_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(materialTypeId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<AccountBasicInfoValidator> accountData = businessAccountService.findById(parentId);

        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(accountData.getData().getEnterpriseId());

        ResponseData<IntellectMaterialTypeValidator> materialTypeData = intellectMaterialTypeService.findById(materialTypeId);
        if (!ResponseCode.SUCCESS.getCode().equals(materialTypeData.getCode())) {
            view.addObject("error", materialTypeData.getCode() + ":" + materialTypeData.getMessage());
            return view;
        }

        //初始化参数
        IntellectMaterialValidator intellectMaterialValidator = new IntellectMaterialValidator();
        intellectMaterialValidator.setId(UUID.uuid32());
        intellectMaterialValidator.setParentId(parentId);
        intellectMaterialValidator.setMaterialTypeId(materialTypeId);
        intellectMaterialValidator.setBusiness(enterpriseData.getData().getEnterpriseId());
        intellectMaterialValidator.setStatus("3");

        view.addObject("enterpriseName", enterpriseData.getData().getEnterpriseName());
        view.addObject("enterpriseId", enterpriseData.getData().getEnterpriseId());
        view.addObject("accountName", accountData.getData().getAccountName());
        view.addObject("accountId", parentId);
        view.addObject("materialTypeId", materialTypeData.getData().getId());
        view.addObject("materialTypeTitle", materialTypeData.getData().getTitle());
        view.addObject("intellectMaterialValidator", intellectMaterialValidator);
        view.addObject("op", "add");

        return view;
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated IntellectMaterialValidator intellectMaterialValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("intelligence/resource/resource_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {

            view.addObject("intellectMaterialValidator", intellectMaterialValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            intellectMaterialValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            intellectMaterialValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            intellectMaterialValidator.setUpdatedTime(new Date());
            intellectMaterialValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> file = mRequest.getFiles("file[]");

        if("add".equals(op) && (null ==file || file.size()<1) ){
            // 提交前台错误提示
            FieldError err = new FieldError("上传附件", "contractFiles", "附件不能为空");
            result.addError(err);
            view.addObject("error", "附件不能为空！");
            return view;
        }

        //保存数据
        ResponseData data = intellectMaterialService.save(intellectMaterialValidator,file, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("RESOURCE", intellectMaterialValidator.getId(), "add".equals(op) ? intellectMaterialValidator.getCreatedBy() : intellectMaterialValidator.getUpdatedBy(), op, "add".equals(op) ? "添加" : "修改", JSON.toJSONString(intellectMaterialValidator));
        }

        //记录日志
        log.info("[智能短信][素材管理][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(intellectMaterialValidator));

        view.setView(new RedirectView("/intel/material/getMaterial/" + intellectMaterialValidator.getParentId() + "/" + intellectMaterialValidator.getMaterialTypeId(), true, false));
        return view;

    }

    /**
     * 修改素材类别
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id) {
        ModelAndView view = new ModelAndView("intelligence/resource/resource_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<IntellectMaterialValidator> materialData = intellectMaterialService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(materialData.getCode())) {
            view.addObject("error", materialData.getCode() + ":" + materialData.getMessage());
            return view;
        }

        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(materialData.getData().getBusiness());
        ResponseData<IntellectMaterialTypeValidator> materialTypeData = intellectMaterialTypeService.findById(materialData.getData().getMaterialTypeId());
        if (!ResponseCode.SUCCESS.getCode().equals(materialTypeData.getCode())) {
            view.addObject("error", materialTypeData.getCode() + ":" + materialTypeData.getMessage());
            return view;
        }
        view.addObject("enterpriseName", enterpriseData.getData().getEnterpriseName());
        view.addObject("enterpriseId", enterpriseData.getData().getEnterpriseId());
        //view.addObject("accountName", accountData.getData().getAccountName());
        view.addObject("accountId", materialData.getData().getParentId());
        view.addObject("materialTypeId", materialTypeData.getData().getId());
        view.addObject("materialTypeTitle", materialTypeData.getData().getTitle());
        view.addObject("intellectMaterialValidator", materialData.getData());
        view.addObject("op", "edit");

        return view;
    }

    /**
     * 修改素材类别
     *
     * @return
     */
    @RequestMapping(value = "/cancel/{id}", method = RequestMethod.GET)
    public ModelAndView cancel(@PathVariable String id) {
        ModelAndView view = new ModelAndView("intelligence/resource/resource_type_list");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }
      return view;
    }
}
