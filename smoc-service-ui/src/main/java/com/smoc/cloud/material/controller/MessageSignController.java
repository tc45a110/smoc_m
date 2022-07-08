package com.smoc.cloud.material.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.FlowApproveService;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.FlowApproveValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseDocumentInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.SystemAttachmentValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.material.service.MessageSignService;
import com.smoc.cloud.material.service.SystemAttachmentService;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信签名
 */
@Slf4j
@Controller
@RequestMapping("/sign")
public class MessageSignController {

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private MessageSignService messageSignService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Autowired
    private FlowApproveService flowApproveService;

    /**
     * 短信签名列表
     * @param signType
     * @param request
     * @return
     */
    @RequestMapping(value = "/list/{signType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String signType,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("sign/message_sign_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<EnterpriseDocumentInfoValidator> params = new PageParams<EnterpriseDocumentInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator = new EnterpriseDocumentInfoValidator();
        enterpriseDocumentInfoValidator.setEnterpriseId(user.getOrganization());
        enterpriseDocumentInfoValidator.setBusinessType(signType);
        params.setParams(enterpriseDocumentInfoValidator);

        //查询
        ResponseData<PageList<EnterpriseDocumentInfoValidator>> data = messageSignService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseDocumentInfoValidator", enterpriseDocumentInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("signType", signType);
        return view;
    }

    /**
     * 短信签名分页
     * @param signType
     * @return
     */
    @RequestMapping(value = "/page/{signType}", method = RequestMethod.POST)
    public ModelAndView page(@PathVariable String signType,@ModelAttribute EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator, PageParams pageParams,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("sign/message_sign_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        enterpriseDocumentInfoValidator.setEnterpriseId(user.getOrganization());
        enterpriseDocumentInfoValidator.setBusinessType(signType);
        pageParams.setParams(enterpriseDocumentInfoValidator);


        ResponseData<PageList<EnterpriseDocumentInfoValidator>> data = messageSignService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseDocumentInfoValidator", enterpriseDocumentInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("signType", signType);
        return view;
    }

    /**
     * 添加
     * @return
     */
    @RequestMapping(value = "/add/{signType}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String signType, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("sign/message_sign_edit");

        //初始化参数
        EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator = new EnterpriseDocumentInfoValidator();
        enterpriseDocumentInfoValidator.setId(UUID.uuid32());
        enterpriseDocumentInfoValidator.setBusinessType(signType);
        enterpriseDocumentInfoValidator.setDocStatus("3");
        enterpriseDocumentInfoValidator.setEnterpriseId(user.getOrganization());

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");
        view.addObject("enterpriseDocumentInfoValidator", enterpriseDocumentInfoValidator);
        view.addObject("signType", signType);

        return view;

    }

    /**
     * 编辑
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("sign/message_sign_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //修改
        ResponseData<EnterpriseDocumentInfoValidator> data = messageSignService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //查询附件
        ResponseData<List<SystemAttachmentValidator>> filesList = systemAttachmentService.findByMoudleId(data.getData().getId());

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        view.addObject("enterpriseDocumentInfoValidator", data.getData());
        view.addObject("signType", data.getData().getBusinessType());
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
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated EnterpriseDocumentInfoValidator enterpriseDocumentInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("sign/message_sign_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> file = mRequest.getFiles("file[]");

        Pattern p = Pattern.compile("\\【.*?\\】");
        Matcher m = p.matcher(enterpriseDocumentInfoValidator.getSignName());
        boolean match = m.find();
        if(match){
            // 提交前台错误提示
            FieldError err = new FieldError("签名", "signName", "无需在签名内容前后添加中括号");
            result.addError(err);
        }

        //完成参数规则验证
        if (result.hasErrors()) {
            //查询附件
            ResponseData<List<SystemAttachmentValidator>> filesList = systemAttachmentService.findByMoudleId(enterpriseDocumentInfoValidator.getId());

            //op操作标记，add表示添加，edit表示修改
            view.addObject("enterpriseDocumentInfoValidator", enterpriseDocumentInfoValidator);
            view.addObject("signType", enterpriseDocumentInfoValidator.getBusinessType());
            view.addObject("filesList", filesList.getData());
            view.addObject("op", op);
            return view;
        }

        //查询信息
        ResponseData<EnterpriseDocumentInfoValidator> infoDate = messageSignService.findById(enterpriseDocumentInfoValidator.getId());
        if (ResponseCode.SUCCESS.getCode().equals(infoDate.getCode()) && !StringUtils.isEmpty(infoDate.getData())) {
            if("2".equals(infoDate.getData().getDocStatus())){
                view.addObject("error", "已审核通过，不能进行修改！");
                return view;
            }
        }

        enterpriseDocumentInfoValidator.setEnterpriseId(user.getOrganization());
        enterpriseDocumentInfoValidator.setDocStatus("3");

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            enterpriseDocumentInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            enterpriseDocumentInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            enterpriseDocumentInfoValidator.setUpdatedTime(new Date());
            enterpriseDocumentInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        enterpriseDocumentInfoValidator.setSignSource("2");
        ResponseData data = messageSignService.save(enterpriseDocumentInfoValidator, file, user.getCorporation(),op,user.getId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("EC_DOCUMENT", enterpriseDocumentInfoValidator.getId(), "add".equals(op) ? enterpriseDocumentInfoValidator.getCreatedBy() : enterpriseDocumentInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加签名资质" : "修改签名资质", JSON.toJSONString(enterpriseDocumentInfoValidator));
        }

        //记录日志
        log.info("[签名管理][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(enterpriseDocumentInfoValidator));

        view.setView(new RedirectView("/sign/list/"+enterpriseDocumentInfoValidator.getBusinessType(), true, false));
        return view;

    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("sign/message_sign_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<EnterpriseDocumentInfoValidator> infoDate = messageSignService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        //查询信息
        if (ResponseCode.SUCCESS.getCode().equals(infoDate.getCode()) && !StringUtils.isEmpty(infoDate.getData())) {
            if("2".equals(infoDate.getData().getDocStatus())){
                view.addObject("error", "已审核通过，不能进行删除！");
                return view;
            }
        }

        //删除操作
        ResponseData data = messageSignService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("EC_DOCUMENT", infoDate.getData().getId(), user.getRealName(), "delete", "删除签名" , JSON.toJSONString(infoDate.getData()));
        }

        //记录日志
        log.info("[签名管理][{}][{}]数据:{}", "delete", user.getUserName(), JSON.toJSONString(infoDate.getData()));

        view.setView(new RedirectView("/sign/list/"+infoDate.getData().getBusinessType(), true, false));
        return view;
    }

    /**
     * 显示详情
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("sign/message_sign_detail");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<EnterpriseDocumentInfoValidator> infoDate = messageSignService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoDate.getCode())) {
            view.addObject("error", infoDate.getCode() + ":" + infoDate.getMessage());
            return view;
        }

        //查询审核记录
        ResponseData<List<FlowApproveValidator>> checkRecordData = flowApproveService.checkRecord(infoDate.getData().getId());
        if (!ResponseCode.SUCCESS.getCode().equals(checkRecordData.getCode())) {
            view.addObject("error", checkRecordData.getCode() + ":" + checkRecordData.getMessage());
            return view;
        }

        //查询附件
        ResponseData<List<SystemAttachmentValidator>> filesList = systemAttachmentService.findByMoudleId(infoDate.getData().getId());

        view.addObject("enterpriseDocumentInfoValidator", infoDate.getData());
        view.addObject("checkRecord", checkRecordData.getData());
        view.addObject("filesList", filesList.getData());

        return view;

    }
}
