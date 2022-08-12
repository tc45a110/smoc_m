package com.smoc.cloud.customer.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseSignCertifyValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.EnterpriseSignCertifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("sign/certify")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class EnterpriseSignCertifyController {

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private EnterpriseSignCertifyService enterpriseSignCertifyService;

    /**
     * 查询
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("sign/certify/sign_certify_list");

        //初始化数据
        PageParams<EnterpriseSignCertifyValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        EnterpriseSignCertifyValidator enterpriseSignCertifyValidator = new EnterpriseSignCertifyValidator();
        params.setParams(enterpriseSignCertifyValidator);

        //查询
        ResponseData<PageList<EnterpriseSignCertifyValidator>> data = enterpriseSignCertifyService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseSignCertifyValidator", enterpriseSignCertifyValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute EnterpriseSignCertifyValidator enterpriseSignCertifyValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("sign/certify/sign_certify_list");

        //分页查询
        pageParams.setParams(enterpriseSignCertifyValidator);

        ResponseData<PageList<EnterpriseSignCertifyValidator>> data = enterpriseSignCertifyService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseSignCertifyValidator", enterpriseSignCertifyValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 新建
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add() {
        ModelAndView view = new ModelAndView("sign/certify/sign_certify_edit");

        //初始化参数
        EnterpriseSignCertifyValidator enterpriseSignCertifyValidator = new EnterpriseSignCertifyValidator();
        enterpriseSignCertifyValidator.setId(UUID.uuid32());
        enterpriseSignCertifyValidator.setRegisterEnterpriseId(UUID.uuid32());
        enterpriseSignCertifyValidator.setCertifyStatus("1");
        enterpriseSignCertifyValidator.setAuthorizeStartDate(DateTimeUtils.getDateFormat(new Date()));
        enterpriseSignCertifyValidator.setAuthorizeExpireDate(DateTimeUtils.getDateFormat(DateTimeUtils.dateAddYears(new Date(),2)));
        enterpriseSignCertifyValidator.setPersonLiableCertificateType("居民身份证");
        enterpriseSignCertifyValidator.setPersonHandledCertificateType("居民身份证");
        enterpriseSignCertifyValidator.setPosition("阿里云服务器");

        view.addObject("enterpriseSignCertifyValidator", enterpriseSignCertifyValidator);
        view.addObject("op", "add");

        return view;
    }

    /**
     * 编辑
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id) {
        ModelAndView view = new ModelAndView("sign/certify/sign_certify_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //修改:查询数据
        ResponseData<EnterpriseSignCertifyValidator> data = enterpriseSignCertifyService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        view.addObject("enterpriseSignCertifyValidator", data.getData());
        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");

        return view;
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated EnterpriseSignCertifyValidator enterpriseSignCertifyValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("sign/certify/sign_certify_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("enterpriseSignCertifyValidator", enterpriseSignCertifyValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            enterpriseSignCertifyValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            enterpriseSignCertifyValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            enterpriseSignCertifyValidator.setUpdatedTime(new Date());
            enterpriseSignCertifyValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = enterpriseSignCertifyService.save(enterpriseSignCertifyValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("SIGN_CERTIFY", enterpriseSignCertifyValidator.getId(), "add".equals(op) ? enterpriseSignCertifyValidator.getCreatedBy() : enterpriseSignCertifyValidator.getUpdatedBy(), op, "add".equals(op) ? "添加企业开户" : "修改企业开户", JSON.toJSONString(enterpriseSignCertifyValidator));
        }

        //记录日志
        log.info("[签名资质][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(enterpriseSignCertifyValidator));

        view.setView(new RedirectView("/sign/certify/list", true, false));
        return view;

    }

    /**
     *  注销
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("sign/certify/sign_certify_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询企业信息
        ResponseData<EnterpriseSignCertifyValidator> responseData = enterpriseSignCertifyService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
        }

        //注销、启用企业业务
        ResponseData data = enterpriseSignCertifyService.deleteById(id);

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("SIGN_CERTIFY", responseData.getData().getId(), user.getRealName(), "edit", "签名资质" , JSON.toJSONString(responseData.getData()));
        }

        //记录日志
        log.info("[签名资质][delete][{}]数据:{}", user.getUserName(), JSON.toJSONString(responseData.getData()));

        view.setView(new RedirectView("/sign/certify/list", true, false));
        return view;

    }

}
