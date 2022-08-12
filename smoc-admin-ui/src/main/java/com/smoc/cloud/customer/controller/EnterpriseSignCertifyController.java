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
import com.smoc.cloud.properties.SmocProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("sign/certify")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class EnterpriseSignCertifyController {

    @Autowired
    private SmocProperties smocProperties;

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
        enterpriseSignCertifyValidator.setAuthorizeExpireDate(DateTimeUtils.getDateFormat(DateTimeUtils.dateAddYears(new Date(), 2)));
        enterpriseSignCertifyValidator.setPersonLiableCertificateType("居民身份证");
        enterpriseSignCertifyValidator.setPersonHandledCertificateType("居民身份证");
        enterpriseSignCertifyValidator.setPosition("阿里云服务器");

//        enterpriseSignCertifyValidator.setRegisterEnterpriseName("吉林省达维众成科技有限公司");
//        enterpriseSignCertifyValidator.setSocialCreditCode("91110108769914581E");
//
//        enterpriseSignCertifyValidator.setPersonLiableName("贾明");
//        enterpriseSignCertifyValidator.setPersonLiableCertificateNumber("32098119850129371X");
//
//        enterpriseSignCertifyValidator.setPersonHandledName("贾明");
//        enterpriseSignCertifyValidator.setPersonHandledCertificateNumber("32098119850129371X");

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
    public ModelAndView save(@ModelAttribute EnterpriseSignCertifyValidator enterpriseSignCertifyValidator, @PathVariable String op, @RequestPart("license") MultipartFile license, @RequestPart("liableCertificate") MultipartFile liableCertificate, @RequestPart("handledCertificate") MultipartFile handledCertificate, @RequestPart("authorizeCertificateFile") MultipartFile authorizeCertificateFile, @RequestPart("positionFile") MultipartFile positionFile, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("sign/certify/sign_certify_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

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

        //创建文件夹
        String certifyFileRootPath = smocProperties.getCertifyFileRootPath();
        String certifyFilePath = DateTimeUtils.getDateFormat(new Date());
        File fold = new File(certifyFilePath);
        while (!fold.exists()) {
            fold.mkdirs();
        }

        /**
         * 文件上传
         */
        try {
            //营业执照
            if(!ObjectUtils.isEmpty(license) && license.getSize()>0) {
                String businessLicense = this.generateFileName(license.getOriginalFilename());
                if ((license.getSize() / 1024) > 100) {
                    view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":文件大小超过了100K");
                    return view;
                }
                license.transferTo(new File(certifyFileRootPath + certifyFilePath + File.separator + businessLicense));
                enterpriseSignCertifyValidator.setBusinessLicense(certifyFilePath + File.separator + businessLicense);
            }else if("add".equals(op)){
                view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":附件不能为空");
                return view;
            }

            //责任人（含法人）证件
            if(!ObjectUtils.isEmpty(liableCertificate) && liableCertificate.getSize()>0) {
                String certificate = this.generateFileName(liableCertificate.getOriginalFilename());
                if ((liableCertificate.getSize() / 1024) > 100) {
                    view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":文件大小超过了100K");
                    return view;
                }
                liableCertificate.transferTo(new File(certifyFileRootPath + certifyFilePath + File.separator + certificate));
                enterpriseSignCertifyValidator.setPersonLiableCertificateUrl(certifyFilePath + File.separator + certificate);
            }else if("add".equals(op)){
                view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":附件不能为空");
                return view;
            }

            //经办人证件
            if(!ObjectUtils.isEmpty(handledCertificate) && handledCertificate.getSize()>0) {
                String handledCertificat = this.generateFileName(handledCertificate.getOriginalFilename());
                if ((handledCertificate.getSize() / 1024) > 100) {
                    view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":文件大小超过了100K");
                    return view;
                }
                handledCertificate.transferTo(new File(certifyFileRootPath + certifyFilePath + File.separator + handledCertificat));
                enterpriseSignCertifyValidator.setPersonHandledCertificateUrl(certifyFilePath + File.separator + handledCertificat);
            }else if("add".equals(op)){
                view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":附件不能为空");
                return view;
            }

            //授权书
            if(!ObjectUtils.isEmpty(authorizeCertificateFile) && authorizeCertificateFile.getSize()>0) {
                String authorizeCertificate = this.generateFileName(authorizeCertificateFile.getOriginalFilename());
                if ((authorizeCertificateFile.getSize() / 1024) > 100) {
                    view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":文件大小超过了100K");
                    return view;
                }
                authorizeCertificateFile.transferTo(new File(certifyFileRootPath + certifyFilePath + File.separator + authorizeCertificate));
                enterpriseSignCertifyValidator.setAuthorizeCertificate(certifyFilePath + File.separator + authorizeCertificate);
            }else if("add".equals(op)){
                view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":附件不能为空");
                return view;
            }

            //授权书
            if(!ObjectUtils.isEmpty(positionFile) && positionFile.getSize()>0) {
                String position = this.generateFileName(positionFile.getOriginalFilename());
                if ((positionFile.getSize() / 1024) > 100) {
                    view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":文件大小超过了100K");
                    return view;
                }
                positionFile.transferTo(new File(certifyFileRootPath + certifyFilePath + File.separator + position));
                enterpriseSignCertifyValidator.setOfficePhotos(certifyFilePath + File.separator + position);
            }else if("add".equals(op)){
                view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":附件不能为空");
                return view;
            }

        }catch (Exception e){
            e.printStackTrace();
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":"+e.getMessage());
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
     * 注销
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
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
            systemUserLogService.logsAsync("SIGN_CERTIFY", responseData.getData().getId(), user.getRealName(), "edit", "签名资质", JSON.toJSONString(responseData.getData()));
        }

        //记录日志
        log.info("[签名资质][delete][{}]数据:{}", user.getUserName(), JSON.toJSONString(responseData.getData()));

        view.setView(new RedirectView("/sign/certify/list", true, false));
        return view;

    }

    /**
     * 文件名称替换工具，将文件名称替换为随机名称
     * @param oldName
     * @return
     */
    public String generateFileName(String oldName){
        String suffix = oldName.substring(oldName.lastIndexOf("."));
        return UUID.uuid32()+suffix;
    }

}
