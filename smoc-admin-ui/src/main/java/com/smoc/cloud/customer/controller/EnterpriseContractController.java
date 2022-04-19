package com.smoc.cloud.customer.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseContractInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.SystemAttachmentValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseContractService;
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
 * EC合同管理
 */
@Slf4j
@Controller
@RequestMapping("/ec/customer")
public class EnterpriseContractController {

    @Autowired
    private EnterpriseContractService enterpriseContractService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    /**
     * 查询EC合同
     *
     * @return
     */
    @RequestMapping(value = "/contract/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("customer/contract/customer_contract_list");

        //初始化数据
        PageParams<EnterpriseContractInfoValidator> params = new PageParams<EnterpriseContractInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        EnterpriseContractInfoValidator enterpriseContractInfoValidator = new EnterpriseContractInfoValidator();
        Date startDate = DateTimeUtils.getFirstMonth(18);
        enterpriseContractInfoValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        enterpriseContractInfoValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(enterpriseContractInfoValidator);

        //查询
        ResponseData<PageList<EnterpriseContractInfoValidator>> data = enterpriseContractService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseContractInfoValidator", enterpriseContractInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 分页查询EC合同
     *
     * @return
     */
    @RequestMapping(value = "/contract/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute EnterpriseContractInfoValidator enterpriseContractInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("customer/contract/customer_contract_list");

        //分页查询
        pageParams.setParams(enterpriseContractInfoValidator);
        //日期格式
        if (!StringUtils.isEmpty(enterpriseContractInfoValidator.getStartDate())) {
            String[] date = enterpriseContractInfoValidator.getStartDate().split(" - ");
            enterpriseContractInfoValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            enterpriseContractInfoValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        ResponseData<PageList<EnterpriseContractInfoValidator>> data = enterpriseContractService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseContractInfoValidator", enterpriseContractInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 添加
     * @return
     */
    @RequestMapping(value = "/contract/add/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String enterpriseId) {

        ModelAndView view = new ModelAndView("customer/contract/customer_contract_edit");

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

        //初始化参数
        EnterpriseContractInfoValidator enterpriseContractInfoValidator = new EnterpriseContractInfoValidator();
        enterpriseContractInfoValidator.setId(UUID.uuid32());
        enterpriseContractInfoValidator.setEnterpriseId(data.getData().getEnterpriseId());
        enterpriseContractInfoValidator.setContractStatus("1");//正常状态
        enterpriseContractInfoValidator.setEnterpriseName(data.getData().getEnterpriseName());
        enterpriseContractInfoValidator.setEnterpriseType(data.getData().getEnterpriseType());

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");
        view.addObject("enterpriseContractInfoValidator", enterpriseContractInfoValidator);

        return view;

    }

    /**
     * 编辑
     * @return
     */
    @RequestMapping(value = "/contract/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/contract/customer_contract_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询数据
        ResponseData<EnterpriseContractInfoValidator> data = enterpriseContractService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(data.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
        }

        EnterpriseContractInfoValidator enterpriseContractInfoValidator = data.getData();
        enterpriseContractInfoValidator.setEnterpriseName(enterpriseData.getData().getEnterpriseName());
        enterpriseContractInfoValidator.setEnterpriseType(enterpriseData.getData().getEnterpriseType());

        //查询附件
        ResponseData<List<SystemAttachmentValidator>> filesList = systemAttachmentService.findByMoudleId(enterpriseContractInfoValidator.getId());

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        view.addObject("enterpriseContractInfoValidator", enterpriseContractInfoValidator);
        view.addObject("filesList", filesList.getData());
        return view;

    }

    /**
     * 保存合同
     * @param enterpriseContractInfoValidator
     * @param result
     * @param op
     * @param request
     * @return
     */
    @RequestMapping(value = "/contract/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated EnterpriseContractInfoValidator enterpriseContractInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/contract/customer_contract_edit");

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
            view.addObject("enterpriseContractInfoValidator", enterpriseContractInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            enterpriseContractInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            enterpriseContractInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            enterpriseContractInfoValidator.setUpdatedTime(new Date());
            enterpriseContractInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(enterpriseContractInfoValidator.getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
        }

        //保存数据
        ResponseData data = enterpriseContractService.save(enterpriseContractInfoValidator, file, enterpriseData.getData().getEnterpriseName(),op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("EC_CONTRACT", enterpriseContractInfoValidator.getId(), "add".equals(op) ? enterpriseContractInfoValidator.getCreatedBy() : enterpriseContractInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加EC合同" : "修改EC合同", JSON.toJSONString(enterpriseContractInfoValidator));
        }

        //记录日志
        log.info("[EC合同管理][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(enterpriseContractInfoValidator));

        view.setView(new RedirectView("/ec/customer/contract/list", true, false));
        return view;

    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/contract/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("customer/contract/customer_contract_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<EnterpriseContractInfoValidator> infoDate = enterpriseContractService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        //删除操作
        ResponseData data = enterpriseContractService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("EC_CONTRACT", infoDate.getData().getId(), user.getRealName(), "delete", "删除EC合同" , JSON.toJSONString(infoDate.getData()));
        }

        //记录日志
        log.info("[EC合同管理][{}][{}]数据:{}", "delete", user.getUserName(), JSON.toJSONString(infoDate.getData()));

        view.setView(new RedirectView("/ec/customer/contract/list", true, false));
        return view;
    }

    /**
     * 显示详情
     * @return
     */
    @RequestMapping(value = "/contract/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("customer/contract/customer_contract_detail");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<EnterpriseContractInfoValidator> infoDate = enterpriseContractService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> enterpriseData = enterpriseService.findById(infoDate.getData().getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(enterpriseData.getCode())) {
            view.addObject("error", enterpriseData.getCode() + ":" + enterpriseData.getMessage());
        }

        EnterpriseContractInfoValidator enterpriseContractInfoValidator = infoDate.getData();
        enterpriseContractInfoValidator.setEnterpriseName(enterpriseData.getData().getEnterpriseName());
        enterpriseContractInfoValidator.setEnterpriseType(enterpriseData.getData().getEnterpriseType());

        //查询附件
        ResponseData<List<SystemAttachmentValidator>> filesList = systemAttachmentService.findByMoudleId(enterpriseContractInfoValidator.getId());

        //查询操作日志
        querySysUserLog(view,infoDate.getData().getId(),"EC_CONTRACT");

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        view.addObject("enterpriseContractInfoValidator", enterpriseContractInfoValidator);
        view.addObject("filesList", filesList.getData());

        return view;

    }

    private void querySysUserLog(ModelAndView view,String moduleId,String module) {
        SystemUserLogValidator systemUserLogValidator = new SystemUserLogValidator();
        systemUserLogValidator.setModuleId(moduleId);
        systemUserLogValidator.setModule(module);

        //查询数据
        PageParams<SystemUserLogValidator> params = new PageParams<>();
        params.setPageSize(20);
        params.setCurrentPage(1);
        params.setParams(systemUserLogValidator);

        //分页查询
        ResponseData<PageList<SystemUserLogValidator>> responseData = systemUserLogService.page(params);
        if (ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("list", responseData.getData().getList());
        }

    }

    /**
     * EC中心查询EC合同
     *
     * @return
     */
    @RequestMapping(value = "/center/contract/list/{enterpriseId}", method = RequestMethod.GET)
    public ModelAndView customer_list(@PathVariable String enterpriseId ) {
        ModelAndView view = new ModelAndView("customer/contract/customer_center_contract_list");

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> basic = enterpriseService.findById(enterpriseId);
        if (!ResponseCode.SUCCESS.getCode().equals(basic.getCode())) {
            view.addObject("error", basic.getCode() + ":" + basic.getMessage());
        }

        //初始化数据
        PageParams<EnterpriseContractInfoValidator> params = new PageParams<EnterpriseContractInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        EnterpriseContractInfoValidator enterpriseContractInfoValidator = new EnterpriseContractInfoValidator();
        enterpriseContractInfoValidator.setEnterpriseId(enterpriseId);
        params.setParams(enterpriseContractInfoValidator);

        //查询
        ResponseData<PageList<EnterpriseContractInfoValidator>> data = enterpriseContractService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseContractInfoValidator", enterpriseContractInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 分页查询EC合同
     *
     * @return
     */
    @RequestMapping(value = "/center/contract/page", method = RequestMethod.POST)
    public ModelAndView customer_page(@ModelAttribute EnterpriseContractInfoValidator enterpriseContractInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("customer/contract/customer_center_contract_list");

        //查询企业数据
        ResponseData<EnterpriseBasicInfoValidator> basic = enterpriseService.findById(enterpriseContractInfoValidator.getEnterpriseId());
        if (!ResponseCode.SUCCESS.getCode().equals(basic.getCode())) {
            view.addObject("error", basic.getCode() + ":" + basic.getMessage());
        }

        //分页查询
        pageParams.setParams(enterpriseContractInfoValidator);

        ResponseData<PageList<EnterpriseContractInfoValidator>> data = enterpriseContractService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseContractInfoValidator", enterpriseContractInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

}
