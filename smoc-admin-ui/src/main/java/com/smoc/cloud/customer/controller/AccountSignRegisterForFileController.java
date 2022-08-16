package com.smoc.cloud.customer.controller;


import com.google.gson.Gson;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.CarrierCount;
import com.smoc.cloud.common.smoc.customer.qo.Export;
import com.smoc.cloud.common.smoc.customer.qo.ExportModel;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterForFileValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.service.AccountSignRegisterForFileService;
import com.smoc.cloud.properties.SmocProperties;
import com.smoc.cloud.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("sign/register/file")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AccountSignRegisterForFileController {

    @Autowired
    private SmocProperties smocProperties;

    @Autowired
    private AccountSignRegisterForFileService accountSignRegisterForFileService;

    /**
     * 查询
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("sign/register/sign_register_file_list");

        //初始化数据
        PageParams<AccountSignRegisterForFileValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        AccountSignRegisterForFileValidator accountSignRegisterForFileValidator = new AccountSignRegisterForFileValidator();
        params.setParams(accountSignRegisterForFileValidator);

        //查询
        ResponseData<PageList<AccountSignRegisterForFileValidator>> data = accountSignRegisterForFileService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountSignRegisterForFileValidator", accountSignRegisterForFileValidator);
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
    public ModelAndView page(@ModelAttribute AccountSignRegisterForFileValidator accountSignRegisterForFileValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("sign/register/sign_register_file_list");

        //分页查询
        pageParams.setParams(accountSignRegisterForFileValidator);

        ResponseData<PageList<AccountSignRegisterForFileValidator>> data = accountSignRegisterForFileService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("accountSignRegisterForFileValidator", accountSignRegisterForFileValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 进入导出页面
     *
     * @return
     */
    @RequestMapping(value = "/toExport", method = RequestMethod.GET)
    public ModelAndView toExport() {
        ModelAndView view = new ModelAndView("sign/register/sign_register_file_export");
        ResponseData<List<CarrierCount>> responseData = this.accountSignRegisterForFileService.countByCarrier();
        view.addObject("carrierCounts", responseData.getData());

        Export export = new Export();
        view.addObject("export", export);
        return view;
    }

    /**
     * 进入导出页面
     *
     * @return
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public ModelAndView export(@ModelAttribute Export export) {
        ModelAndView view = new ModelAndView("sign/register/sign_register_file_export");
        ExportModel queryExportModel = new ExportModel();
        queryExportModel.setRegisterCarrier(export.getCarrier());
        //初始化数据
        PageParams<ExportModel> params = new PageParams<>();
        params.setPageSize(1000);
        params.setCurrentPage(1);
        params.setParams(queryExportModel);
        ResponseData<PageList<ExportModel>> exportPage = this.accountSignRegisterForFileService.export(params);
        if (!ResponseCode.SUCCESS.getCode().equals(exportPage.getCode())) {
            view.addObject("error", exportPage.getCode() + ":" + exportPage.getMessage());
            return view;
        }

        if (!(null != exportPage.getData() && null != exportPage.getData().getList() && exportPage.getData().getList().size() > 0)) {
            view.addObject("error", exportPage.getCode() + ":导出失败，数据为空");
            return view;
        }

        //签名报备，生成文件根目录
        String rootPath = smocProperties.getSignRegisterRootPath();
        //企业文件根目录
        String certifyFileRootPath = smocProperties.getCertifyFileRootPath();

        //移动导出
        if ("CMCC".equals(export.getCarrier())) {
            //创建生成文件的文件夹
            String currentFold = "CMCC" + File.separator + DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS");
            String attachmentPath = rootPath + currentFold + File.separator + "attachment";
            File fold = new File(attachmentPath);
            while (!fold.exists()) {
                fold.mkdirs();
            }
            try {
                Set<String> files = new HashSet<>();
                for (ExportModel exportModel : exportPage.getData().getList()) {
                    //复制营业执照/组织机构代码证
                    String licenseFileName = getFileNames(exportModel.getBusinessLicense());
                    String licenseFilePath = attachmentPath + File.separator + licenseFileName;
                    files.add(licenseFilePath);
                    FileUtils.copyFile(certifyFileRootPath + exportModel.getBusinessLicense(), licenseFilePath);

                    //复制责任人（含法人）证件
                    String liableCertFileName = getFileNames(exportModel.getLiableCertUrl());
                    String liableCertFilePath = attachmentPath + File.separator + liableCertFileName;
                    files.add(liableCertFilePath);
                    FileUtils.copyFile(certifyFileRootPath + exportModel.getLiableCertUrl(), liableCertFilePath);

                    //复制经办人证件
                    String handledCertFileName = getFileNames(exportModel.getHandledCertUrl());
                    String handledCertFilePath = attachmentPath + File.separator + handledCertFileName;
                    files.add(handledCertFilePath);
                    FileUtils.copyFile(certifyFileRootPath + exportModel.getHandledCertUrl(), handledCertFilePath);

                    //复制授权书
                    String authorizeCertFileName = getFileNames(exportModel.getAuthorizeCert());
                    String authorizeCertFilePath = attachmentPath + File.separator + authorizeCertFileName;
                    files.add(authorizeCertFilePath);
                    FileUtils.copyFile(certifyFileRootPath + exportModel.getAuthorizeCert(), authorizeCertFilePath);

                    //复制授权书
                    String officePhotosFileName = getFileNames(exportModel.getOfficePhotos());
                    String officePhotosFilePath = attachmentPath + File.separator + officePhotosFileName;
                    files.add(officePhotosFilePath);
                    FileUtils.copyFile(certifyFileRootPath + exportModel.getOfficePhotos(), officePhotosFilePath);
                }

                log.info("[files]:{}",new Gson().toJson(files));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    public String getFileNames(String filePath) {
        return filePath.substring(11, filePath.length());
    }
}
