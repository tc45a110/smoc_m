package com.smoc.cloud.customer.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.FlowApproveService;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.FlowApproveValidator;
import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseDocumentInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.SystemAttachmentValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseDocumentService;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.customer.service.SystemAttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * EC签名资质管理
 */
@Slf4j
@Controller
@RequestMapping("/ec/customer")
public class EnterpriseDocumentController {

    @Autowired
    private EnterpriseDocumentService enterpriseDocumentService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private FlowApproveService flowApproveService;

    /**
     * 查询EC资料
     *
     * @return
     */
    @RequestMapping(value = "/material/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/material/customer_material_list");

        //初始化数据
        PageParams<EnterpriseDocumentInfoValidator> params = new PageParams<EnterpriseDocumentInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator = new EnterpriseDocumentInfoValidator();
        Date startDate = DateTimeUtils.getFirstMonth(12);
        enterpriseDocumentInfoValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        enterpriseDocumentInfoValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(enterpriseDocumentInfoValidator);

        //查询
        ResponseData<PageList<EnterpriseDocumentInfoValidator>> data = enterpriseDocumentService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseDocumentInfoValidator", enterpriseDocumentInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 分页查询EC资料
     *
     * @return
     */
    @RequestMapping(value = "/material/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("customer/material/customer_material_list");
        //分页查询
        pageParams.setParams(enterpriseDocumentInfoValidator);
        //日期格式
        if (!StringUtils.isEmpty(enterpriseDocumentInfoValidator.getStartDate())) {
            String[] date = enterpriseDocumentInfoValidator.getStartDate().split(" - ");
            enterpriseDocumentInfoValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            enterpriseDocumentInfoValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        ResponseData<PageList<EnterpriseDocumentInfoValidator>> data = enterpriseDocumentService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseDocumentInfoValidator", enterpriseDocumentInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 添加
     * @return
     */
    @RequestMapping(value = "/material/edit/center/{flag}/{id}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String flag, @PathVariable String id) {

        ModelAndView view = new ModelAndView("customer/material/customer_material_edit_center");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //如果是base id传来的是企业id
        if("base".equals(flag)){
            //查询企业数据
            ResponseData<EnterpriseBasicInfoValidator> data = enterpriseService.findById(id);
            if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                view.addObject("error", data.getCode() + ":" + data.getMessage());
            }
            view.addObject("id", data.getData().getEnterpriseId());
            view.addObject("flag", flag);
            return view;
        }

        //查询数据
        ResponseData<EnterpriseDocumentInfoValidator> data = enterpriseDocumentService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        view.addObject("id", data.getData().getId());
        view.addObject("flag", flag);

        return view;

    }

    /**
     * 编辑
     * @return
     */
    @RequestMapping(value = "/material/edit/base/{flag}/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String flag,@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/material/customer_material_edit_base");

        //base：添加操作
        if("base".equals(flag)){
            //查询企业数据
            ResponseData<EnterpriseBasicInfoValidator> data = enterpriseService.findById(id);
            if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                view.addObject("error", data.getCode() + ":" + data.getMessage());
            }

            //初始化参数
            EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator = new EnterpriseDocumentInfoValidator();
            enterpriseDocumentInfoValidator.setId(UUID.uuid32());
            enterpriseDocumentInfoValidator.setEnterpriseId(data.getData().getEnterpriseId());
            enterpriseDocumentInfoValidator.setBusinessType("TEXT_SMS");
            enterpriseDocumentInfoValidator.setDocStatus("2");//正常状态
            enterpriseDocumentInfoValidator.setEnterpriseName(data.getData().getEnterpriseName());
            enterpriseDocumentInfoValidator.setEnterpriseType(data.getData().getEnterpriseType());

            //op操作标记，add表示添加，edit表示修改
            view.addObject("op", "add");
            view.addObject("enterpriseDocumentInfoValidator", enterpriseDocumentInfoValidator);
            return view;
        }

        //修改
        ResponseData<EnterpriseDocumentInfoValidator> data = enterpriseDocumentService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(data.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
        }

        EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator = data.getData();
        enterpriseDocumentInfoValidator.setEnterpriseName(enterpriseData.getData().getEnterpriseName());
        enterpriseDocumentInfoValidator.setEnterpriseType(enterpriseData.getData().getEnterpriseType());

        //查询附件
        ResponseData<List<SystemAttachmentValidator>> filesList = systemAttachmentService.findByMoudleId(enterpriseDocumentInfoValidator.getId());

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        view.addObject("enterpriseDocumentInfoValidator", enterpriseDocumentInfoValidator);
        view.addObject("filesList", filesList.getData());

        return view;

    }

    /**
     * 保存
     * @param enterpriseDocumentInfoValidator
     * @param result
     * @param op
     * @param request
     * @return
     */
    @RequestMapping(value = "/material/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/material/customer_material_edit_base");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> file = mRequest.getFiles("file[]");
        if("add".equals(op) && StringUtils.isEmpty(file.get(0).getOriginalFilename())){
            // 提交前台错误提示
            FieldError err = new FieldError("上传附件", "contractFiles", "附件不能为空");
            result.addError(err);
        }

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("enterpriseDocumentInfoValidator", enterpriseDocumentInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            enterpriseDocumentInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            enterpriseDocumentInfoValidator.setCreatedBy(user.getRealName());
            enterpriseDocumentInfoValidator.setCheckDate(enterpriseDocumentInfoValidator.getCreatedTime());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            enterpriseDocumentInfoValidator.setUpdatedTime(new Date());
            enterpriseDocumentInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(enterpriseDocumentInfoValidator.getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
        }

        //保存数据
        enterpriseDocumentInfoValidator.setSignSource("1");
        ResponseData data = enterpriseDocumentService.save(enterpriseDocumentInfoValidator, file, enterpriseData.getData().getEnterpriseName(),op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("EC_DOCUMENT", enterpriseDocumentInfoValidator.getId(), "add".equals(op) ? enterpriseDocumentInfoValidator.getCreatedBy() : enterpriseDocumentInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加签名资质" : "修改签名资质", JSON.toJSONString(enterpriseDocumentInfoValidator));
        }

        //记录日志
        log.info("[签名资质管理][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(enterpriseDocumentInfoValidator));

        //刷新标识
        if("add".equals(op)){
            view.addObject("refreshFlag", "refreshFlag");
            return view;
        }

        view.setView(new RedirectView("/ec/customer/material/edit/base/enterprise/"+enterpriseDocumentInfoValidator.getId(), true, false));
        return view;

    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/material/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/material/customer_material_edit_base");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<EnterpriseDocumentInfoValidator> infoDate = enterpriseDocumentService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        //删除操作
        ResponseData data = enterpriseDocumentService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("EC_DOCUMENT", infoDate.getData().getId(), user.getRealName(), "delete", "删除签名资质" , JSON.toJSONString(infoDate.getData()));
        }

        //记录日志
        log.info("[签名资质管理][{}][{}]数据:{}", "delete", user.getUserName(), JSON.toJSONString(infoDate.getData()));

        view.setView(new RedirectView("/ec/customer/material/list", true, false));
        return view;
    }

    /**
     * 显示详情
     * @return
     */
    @RequestMapping(value = "/material/view/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/material/customer_material_view_center");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<EnterpriseDocumentInfoValidator> infoDate = enterpriseDocumentService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        view.addObject("id", infoDate.getData().getId());

        return view;

    }

    /**
     * 显示详情
     * @return
     */
    @RequestMapping(value = "/material/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/material/customer_material_view_detail");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
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

        EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator = infoDate.getData();
        enterpriseDocumentInfoValidator.setEnterpriseName(enterpriseData.getData().getEnterpriseName());
        enterpriseDocumentInfoValidator.setEnterpriseType(enterpriseData.getData().getEnterpriseType());

        //查询附件
        ResponseData<List<SystemAttachmentValidator>> filesList = systemAttachmentService.findByMoudleId(enterpriseDocumentInfoValidator.getId());

        view.addObject("enterpriseDocumentInfoValidator", enterpriseDocumentInfoValidator);
        view.addObject("filesList", filesList.getData());

        return view;

    }

    @RequestMapping(value = "/material/check/list/{id}", method = RequestMethod.GET)
    public ModelAndView checkList(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/material/customer_material_view_check");

        ResponseData<List<FlowApproveValidator>> checkRecordData = flowApproveService.checkRecord(id);
        if (!ResponseCode.SUCCESS.getCode().equals(checkRecordData.getCode())) {
            view.addObject("error", checkRecordData.getCode() + ":" + checkRecordData.getMessage());
            return view;
        }
        view.addObject("checkRecord", checkRecordData.getData());
        return view;
    }

    /**
     * 显示审核
     * @return
     */
    @RequestMapping(value = "/material/sign/check/{id}", method = RequestMethod.GET)
    public ModelAndView check(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/material/customer_material_sign_check");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
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

        EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator = infoDate.getData();
        enterpriseDocumentInfoValidator.setEnterpriseName(enterpriseData.getData().getEnterpriseName());
        enterpriseDocumentInfoValidator.setEnterpriseType(enterpriseData.getData().getEnterpriseType());

        //查询附件
        ResponseData<List<SystemAttachmentValidator>> filesList = systemAttachmentService.findByMoudleId(enterpriseDocumentInfoValidator.getId());

        ResponseData<List<FlowApproveValidator>> checkRecordData = flowApproveService.checkRecord(infoDate.getData().getId());
        if (!ResponseCode.SUCCESS.getCode().equals(checkRecordData.getCode())) {
            view.addObject("error", checkRecordData.getCode() + ":" + checkRecordData.getMessage());
            return view;
        }

        view.addObject("enterpriseDocumentInfoValidator", enterpriseDocumentInfoValidator);
        view.addObject("filesList", filesList.getData());
        view.addObject("checkRecord", checkRecordData.getData());

        return view;

    }

    /**
     * 操作审核
     * @param enterpriseDocumentInfoValidator
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/material/sign/save", method = RequestMethod.POST)
    public ModelAndView checkSave(@ModelAttribute @Validated EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/material/customer_material_sign_check");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("enterpriseDocumentInfoValidator", enterpriseDocumentInfoValidator);
            return view;
        }

        //保存数据
        ResponseData data = enterpriseDocumentService.check(enterpriseDocumentInfoValidator,user);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("EC_DOCUMENT", enterpriseDocumentInfoValidator.getId(), user.getRealName(), "check","审核签名资质" , JSON.toJSONString(enterpriseDocumentInfoValidator));
        }

        //记录日志
        log.info("[签名资质审核][{}][{}]数据:{}", "check", user.getUserName(), JSON.toJSONString(enterpriseDocumentInfoValidator));

        view.setView(new RedirectView("/ec/customer/material/list", true, false));
        return view;

    }

    /**
     * EC中心查询EC资料
     *
     * @return
     */
    @RequestMapping(value = "/center/material/list/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView customer_data_list(@PathVariable String enterpriseId) {
        ModelAndView view = new ModelAndView("customer/material/customer_center_material_list");

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> basic = enterpriseService.findById(enterpriseId);
        if (!ResponseCode.SUCCESS.getCode().equals(basic.getCode())) {
            view.addObject("error", basic.getCode() + ":" + basic.getMessage());
        }

        //初始化数据
        PageParams<EnterpriseDocumentInfoValidator> params = new PageParams<EnterpriseDocumentInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator = new EnterpriseDocumentInfoValidator();
        enterpriseDocumentInfoValidator.setEnterpriseId(enterpriseId);
        params.setParams(enterpriseDocumentInfoValidator);

        //查询
        ResponseData<PageList<EnterpriseDocumentInfoValidator>> data = enterpriseDocumentService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseDocumentInfoValidator", enterpriseDocumentInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 分页查询EC资料
     *
     * @return
     */
    @RequestMapping(value = "/center/material/page", method = RequestMethod.POST)
    public ModelAndView customer_data_page(@ModelAttribute EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("customer/material/customer_center_material_list");

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> basic = enterpriseService.findById(enterpriseDocumentInfoValidator.getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(basic.getCode())) {
            view.addObject("error", basic.getCode() + ":" + basic.getMessage());
        }

        //分页查询
        pageParams.setParams(enterpriseDocumentInfoValidator);

        ResponseData<PageList<EnterpriseDocumentInfoValidator>> data = enterpriseDocumentService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseDocumentInfoValidator", enterpriseDocumentInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }



}
