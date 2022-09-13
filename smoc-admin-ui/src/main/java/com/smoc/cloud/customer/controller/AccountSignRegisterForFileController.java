package com.smoc.cloud.customer.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.google.gson.Gson;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.CarrierCount;
import com.smoc.cloud.common.smoc.customer.qo.Export;
import com.smoc.cloud.common.smoc.customer.qo.ExportModel;
import com.smoc.cloud.common.smoc.customer.qo.ExportRegisterModel;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterExportRecordValidator;
import com.smoc.cloud.common.smoc.customer.validator.AccountSignRegisterForFileValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.customer.service.AccountSignRegisterForFileService;
import com.smoc.cloud.properties.SmocProperties;
import com.smoc.cloud.utils.CompressUtil;
import com.smoc.cloud.utils.Utils;
import com.smoc.cloud.utils.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

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

        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
            return view;
        }

        Map<String,Integer> carrier = new HashMap<>();
        carrier.put("CMCC",0);
        carrier.put("UNIC",0);
        carrier.put("TELC",0);
        if(null != responseData.getData() && responseData.getData().size()>0){
           for(CarrierCount carrierCount: responseData.getData()){
               carrier.put(carrierCount.getCarrier(),carrierCount.getCount());
           }
        }
        view.addObject("carrier", carrier);
        //log.info("[carrier]:{}",new Gson().toJson(carrier));
        Export export = new Export();
        export.setNumber(1000);
        view.addObject("export", export);
        return view;
    }


    /**
     * 进入根据报备订单号，导出页面
     *
     * @return
     */
    @RequestMapping(value = "/register_button", method = RequestMethod.GET)
    public ModelAndView register_button() {
        ModelAndView view = new ModelAndView("sign/register/sign_register_file_register_button");


        return view;
    }

    /**
     * 入根据报备订单号，导出
     *
     * @return
     */
    @RequestMapping(value = "/register_query/{registerOrderNo}", method = RequestMethod.GET)
    public void register_query(@PathVariable String registerOrderNo, HttpServletRequest request, HttpServletResponse response) {

        ResponseData<AccountSignRegisterExportRecordValidator> record = this.accountSignRegisterForFileService.findByRegisterOrderNo(registerOrderNo);
        if (!ResponseCode.SUCCESS.getCode().equals(record.getCode())) {
            return;
        }

        String carrier = record.getData().getCarrier();

        //初始化数据
        PageParams params = new PageParams<>();
        params.setPageSize(1000);
        params.setCurrentPage(1);
        ResponseData<PageList<ExportModel>> exportPage = this.accountSignRegisterForFileService.query(params,registerOrderNo);
        log.info("[exportPage]：{}",new Gson().toJson(exportPage));
        if (!ResponseCode.SUCCESS.getCode().equals(exportPage.getCode())) {
            return;
        }

        if (!(null != exportPage.getData() && null != exportPage.getData().getList() && exportPage.getData().getList().size() > 0)) {
            return;
        }

        //省份字典
        Map<String, String> provinces = this.provinces(request);

        //签名报备，生成文件根目录
        String rootPath = smocProperties.getSignRegisterRootPath();
        //企业文件根目录
        String certifyFileRootPath = smocProperties.getCertifyFileRootPath();

        //报备单号
        String exportOrderNo = DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(4);

        //移动导出
        if ("CMCC".equals(carrier)) {
            //创建生成文件的文件夹
            String currentFold;
            try {
                Set<String> files = new HashSet<>();
                Integer size = exportPage.getData().getList().size();
                List<ExportModel> models = new ArrayList<>();
                for (int i = 0; i < size; i++) {

                    ExportModel exportModel = exportPage.getData().getList().get(i);

                    //相同：说明用的是导入功能，图片在import下
                    if(exportModel.getCertifyId().equals(exportModel.getSocialCreditCode())){
                        //复制营业执照/组织机构代码证
                        files.add(certifyFileRootPath + "import/" + exportModel.getBusinessLicense());

                        //复制责任人（含法人）证件
                        files.add(certifyFileRootPath + "import/" + exportModel.getLiableCertUrl());

                        //复制经办人证件
                        files.add(certifyFileRootPath + "import/" + exportModel.getHandledCertUrl());

                        //复制授权书
                        if(!StringUtils.isEmpty(exportModel.getAuthorizeCert())){
                            files.add(certifyFileRootPath + "import/" + exportModel.getAuthorizeCert());
                        }

                        //复制授权书
                        files.add(certifyFileRootPath + "import/" + exportModel.getOfficePhotos());
                    }else{
                        //复制营业执照/组织机构代码证
                        files.add(certifyFileRootPath + exportModel.getCertifyId() + "/" + exportModel.getBusinessLicense());

                        //复制责任人（含法人）证件
                        files.add(certifyFileRootPath + exportModel.getCertifyId() + "/" + exportModel.getLiableCertUrl());

                        //复制经办人证件
                        files.add(certifyFileRootPath + exportModel.getCertifyId() + "/" + exportModel.getHandledCertUrl());

                        //复制授权书
                        if(!StringUtils.isEmpty(exportModel.getAuthorizeCert())){
                            files.add(certifyFileRootPath + exportModel.getCertifyId() + "/" + exportModel.getAuthorizeCert());
                        }

                        //复制授权书
                        files.add(certifyFileRootPath + exportModel.getCertifyId() + "/" + exportModel.getOfficePhotos());
                    }


                    exportModel.setAccessProvince(provinces.get(exportModel.getAccessProvince()));
                    exportModel.setMainApplication(exportModel.getServiceType());
                    exportModel.setOperate("新增");
                    exportModel.setServiceType("账号注册,账号登录,广告促销,通知提醒,公共服务");
                    exportModel.setPosition("阿里云服务器");
                    models.add(exportModel);
                    if (((i + 1) % 100 == 0 || i == (size - 1))) {

                        //创建生成文件的文件夹
                        String orderNo = DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(4);
                        currentFold = "移动签名报备" + "/" + exportOrderNo + "/" + orderNo;
                        File fold = new File(rootPath + currentFold);
                        while (!fold.exists()) {
                            fold.mkdirs();
                        }

                        //生成图片的压缩文件
                        String zipFile = rootPath + currentFold + "/" + "附件.zip";
                        CompressUtil.compress(files, zipFile, false);

                        //生成excel文件
                        String excelFile = rootPath + currentFold + "/" + orderNo + ".xlsx";
                        writerExcelFile(excelFile, models, "cmcc.xlsx");
                        //log.info("[files]:{}", new Gson().toJson(files));
                        files = new HashSet<>();
                        models = new ArrayList<>();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            String tempPath = rootPath + "移动签名报备" + "/" + "temp";
            File temp = new File(tempPath);
            while (!temp.exists()) {
                temp.mkdirs();
            }
            String zipFilePath = temp + "/" + exportOrderNo + ".zip";

            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(new File(zipFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // 调用工具类压缩文件夹
            ZipUtils.toZip(rootPath + "移动签名报备" + "/" + exportOrderNo, fileOutputStream, true);

            // 调用工具类设置响应格式
            try {
                this.downLoadFile(request, response, "移动-" + exportOrderNo + ".zip", zipFilePath);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("导出失败~");
            }

        }

        //联通导出
        if ("UNIC".equals(carrier)) {
            //创建生成文件的文件夹
            String currentFold;
            try {
                Integer size = exportPage.getData().getList().size();
                List<ExportModel> models = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    ExportModel exportModel = exportPage.getData().getList().get(i);
                    exportModel.setLiableCertType("居民身份证" + exportModel.getLiableCertNum().length() + "位");
                    exportModel.setHandledCertType("居民身份证" + exportModel.getHandledCertNum().length() + "位");
                    exportModel.setAccessProvince(provinces.get(exportModel.getAccessProvince()));
                    exportModel.setOperate("新增");
                    models.add(exportModel);
                }

                //创建生成文件的文件夹
                String orderNo = DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(4);
                currentFold = "联通签名报备" + "/" + exportOrderNo + "/" + orderNo;
                File fold = new File(rootPath + currentFold);
                while (!fold.exists()) {
                    fold.mkdirs();
                }

                //生成excel文件
                String excelFile = rootPath + currentFold + "/" + orderNo + ".xlsx";
                writerExcelFile(excelFile, models, "unic.xlsx");

            } catch (Exception e) {
                e.printStackTrace();
            }

            String tempPath = rootPath + "联通签名报备" + "/" + "temp";
            File temp = new File(tempPath);
            while (!temp.exists()) {
                temp.mkdirs();
            }
            String zipFilePath = temp + "/" + exportOrderNo + ".zip";

            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(new File(zipFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // 调用工具类压缩文件夹
            ZipUtils.toZip(rootPath + "联通签名报备" + "/" + exportOrderNo, fileOutputStream, true);

            // 调用工具类设置响应格式
            try {
                this.downLoadFile(request, response, "联通-" + exportOrderNo + ".zip", zipFilePath);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("导出失败~");
            }

        }

        //电信导出
        if ("TELC".equals(carrier)) {
            //创建生成文件的文件夹
            String currentFold;
            try {
                Integer size = exportPage.getData().getList().size();
                List<ExportModel> models = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    ExportModel exportModel = exportPage.getData().getList().get(i);
                    exportModel.setServiceType(exportModel.getServiceType().replace(",", "&"));
                    exportModel.setIsAuthorize("有");
                    exportModel.setLiableCertType("身份证");
                    exportModel.setHandledCertType("身份证");
                    exportModel.setIsSign("是");
                    exportModel.setIsGreen("否");
                    exportModel.setBlackList("黑名单");
                    exportModel.setAccessProvince(provinces.get(exportModel.getAccessProvince()));
                    exportModel.setOperate("新增");
                    models.add(exportModel);
                }

                //创建生成文件的文件夹
                String orderNo = DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(4);
                currentFold = "电信签名报备" + "/" + exportOrderNo + "/" + orderNo;
                File fold = new File(rootPath + currentFold);
                while (!fold.exists()) {
                    fold.mkdirs();
                }

                //生成excel文件
                String excelFile = rootPath + currentFold + "/" + orderNo + ".xlsx";
                writerExcelFile(excelFile, models, "telc.xlsx");

            } catch (Exception e) {
                e.printStackTrace();
            }

            String tempPath = rootPath + "电信签名报备" + "/" + "temp";
            File temp = new File(tempPath);
            while (!temp.exists()) {
                temp.mkdirs();
            }
            String zipFilePath = temp + "/" + exportOrderNo + ".zip";

            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(new File(zipFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // 调用工具类压缩文件夹
            ZipUtils.toZip(rootPath + "电信签名报备" + "/" + exportOrderNo, fileOutputStream, true);

            // 调用工具类设置响应格式
            try {
                this.downLoadFile(request, response, "电信-" + exportOrderNo + ".zip", zipFilePath);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("导出失败~");
            }

        }
    }

    /**
     * 进入根据
     *
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register() {
        ModelAndView view = new ModelAndView("sign/register/sign_register_file_register");
        return view;
    }

    /**
     * 进入根据
     *
     * @return
     */
    @RequestMapping(value = "/register_op/{registerOrderNo}", method = RequestMethod.GET)
    public ModelAndView register_op(@PathVariable String registerOrderNo) {
        ModelAndView view = new ModelAndView("sign/register/sign_register_file_register");

        ResponseData<AccountSignRegisterExportRecordValidator> record = this.accountSignRegisterForFileService.findByRegisterOrderNo(registerOrderNo);
        if (!ResponseCode.SUCCESS.getCode().equals(record.getCode())) {
            view.addObject("error", record.getCode() + ":" + record.getMessage());
            return view;
        }

        AccountSignRegisterExportRecordValidator recordValidator = record.getData();
        if("3".equals(recordValidator.getRegisterStatus())){
            view.addObject("error", "该订单号，已经报备！");
            return view;
        }

        ResponseData responseData = this.accountSignRegisterForFileService.updateRegisterStatusByOrderNo("3",registerOrderNo);
        //log.info("[responseData]:{}",new Gson().toJson(responseData));
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
            return view;
        }

        view.addObject("error", "报备成功");
        return view;
    }

    /**
     * 进入导出页面
     *
     * @return
     */
    @RequestMapping(value = "/export_button", method = RequestMethod.GET)
    public ModelAndView export_button() {
        ModelAndView view = new ModelAndView("sign/register/sign_register_file_export_button");

        ResponseData<List<CarrierCount>> responseData = this.accountSignRegisterForFileService.countByCarrier();

        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
            return view;
        }

        Map<String,Integer> carrier = new HashMap<>();
        carrier.put("CMCC",0);
        carrier.put("UNIC",0);
        carrier.put("TELC",0);
        if(null != responseData.getData() && responseData.getData().size()>0){
            for(CarrierCount carrierCount: responseData.getData()){
                carrier.put(carrierCount.getCarrier(),carrierCount.getCount());
            }
        }
        view.addObject("carrier", carrier);
        return view;
    }


    /**
     * 导出文件
     *
     * @return
     */
    @RequestMapping(value = "/export/{carrier}", method = RequestMethod.GET)
    public void export(@PathVariable String carrier, HttpServletRequest request, HttpServletResponse response) {

        if (!("CMCC".equals(carrier) || "UNIC".equals(carrier) || "TELC".equals(carrier))) {
            return;
        }
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        List<String> ids = new ArrayList<>();

        ExportModel queryExportModel = new ExportModel();
        queryExportModel.setRegisterCarrier(carrier);
        //初始化数据
        PageParams<ExportModel> params = new PageParams<>();
        params.setPageSize(1000);
        params.setCurrentPage(1);
        params.setParams(queryExportModel);
        ResponseData<PageList<ExportModel>> exportPage = this.accountSignRegisterForFileService.export(params);
        log.info("[exportPage]：{}",new Gson().toJson(exportPage));
        if (!ResponseCode.SUCCESS.getCode().equals(exportPage.getCode())) {
            return;
        }

        if (!(null != exportPage.getData() && null != exportPage.getData().getList() && exportPage.getData().getList().size() > 0)) {
            return;
        }

        //省份字典
        Map<String, String> provinces = this.provinces(request);

        //签名报备，生成文件根目录
        String rootPath = smocProperties.getSignRegisterRootPath();
        //企业文件根目录
        String certifyFileRootPath = smocProperties.getCertifyFileRootPath();

        //报备单号
        String exportOrderNo = DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(4);

        //移动导出
        if ("CMCC".equals(carrier)) {
            //创建生成文件的文件夹
            String currentFold;
            try {
                Set<String> files = new HashSet<>();
                Integer size = exportPage.getData().getList().size();
                List<ExportModel> models = new ArrayList<>();
                for (int i = 0; i < size; i++) {

                    ExportModel exportModel = exportPage.getData().getList().get(i);
                    ids.add(exportModel.getId());
                    //相同：说明用的是导入功能
                    if(exportModel.getCertifyId().equals(exportModel.getSocialCreditCode())){
                        //复制营业执照/组织机构代码证
                        files.add(certifyFileRootPath + "import/" + exportModel.getBusinessLicense());

                        //复制责任人（含法人）证件
                        files.add(certifyFileRootPath + "import/" + exportModel.getLiableCertUrl());

                        //复制经办人证件
                        files.add(certifyFileRootPath + "import/" + exportModel.getHandledCertUrl());

                        //复制授权书
                        if(!StringUtils.isEmpty(exportModel.getAuthorizeCert())){
                            files.add(certifyFileRootPath + "import/" + exportModel.getAuthorizeCert());
                        }

                        //复制授权书
                        files.add(certifyFileRootPath + "import/" + exportModel.getOfficePhotos());
                    }else{
                        //复制营业执照/组织机构代码证
                        files.add(certifyFileRootPath + exportModel.getCertifyId() + "/" + exportModel.getBusinessLicense());

                        //复制责任人（含法人）证件
                        files.add(certifyFileRootPath + exportModel.getCertifyId() + "/" + exportModel.getLiableCertUrl());

                        //复制经办人证件
                        files.add(certifyFileRootPath + exportModel.getCertifyId() + "/" + exportModel.getHandledCertUrl());

                        //复制授权书
                        if(!StringUtils.isEmpty(exportModel.getAuthorizeCert())){
                            files.add(certifyFileRootPath + exportModel.getCertifyId() + "/" + exportModel.getAuthorizeCert());
                        }

                        //复制授权书
                        files.add(certifyFileRootPath + exportModel.getCertifyId() + "/" + exportModel.getOfficePhotos());
                    }

                    exportModel.setAccessProvince(provinces.get(exportModel.getAccessProvince()));
                    exportModel.setMainApplication(exportModel.getServiceType());
                    exportModel.setOperate("新增");
                    exportModel.setServiceType("账号注册,账号登录,广告促销,通知提醒,公共服务");
                    exportModel.setPosition("阿里云服务器");
                    models.add(exportModel);
                    if (((i + 1) % 100 == 0 || i == (size - 1))) {

                        //创建生成文件的文件夹
                        String orderNo = DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(4);
                        currentFold = "移动签名报备" + "/" + exportOrderNo + "/" + orderNo;
                        File fold = new File(rootPath + currentFold);
                        while (!fold.exists()) {
                            fold.mkdirs();
                        }

                        //生成图片的压缩文件
                        String zipFile = rootPath + currentFold + "/" + "附件.zip";
                        CompressUtil.compress(files, zipFile, false);

                        //生成excel文件
                        String excelFile = rootPath + currentFold + "/" + orderNo + ".xlsx";
                        writerExcelFile(excelFile, models, "cmcc.xlsx");
                        //log.info("[files]:{}", new Gson().toJson(files));
                        files = new HashSet<>();
                        models = new ArrayList<>();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            String tempPath = rootPath + "移动签名报备" + "/" + "temp";
            File temp = new File(tempPath);
            while (!temp.exists()) {
                temp.mkdirs();
            }
            String zipFilePath = temp + "/" + exportOrderNo + ".zip";

            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(new File(zipFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // 调用工具类压缩文件夹
            ZipUtils.toZip(rootPath + "移动签名报备" + "/" + exportOrderNo, fileOutputStream, true);

            // 调用工具类设置响应格式
            try {
                ExportRegisterModel exportRegisterModel = new ExportRegisterModel();
                exportRegisterModel.setRegisterOrderNo(exportOrderNo);
                exportRegisterModel.setCarrier(carrier);
                exportRegisterModel.setIds(ids);
                exportRegisterModel.setCreatedBy(user.getRealName());
                this.accountSignRegisterForFileService.register(exportRegisterModel);
                this.downLoadFile(request, response, "移动-" + exportOrderNo + ".zip", zipFilePath);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("导出失败~");
            }

        }

        //联通导出
        if ("UNIC".equals(carrier)) {
            //创建生成文件的文件夹
            String currentFold;
            try {
                Integer size = exportPage.getData().getList().size();
                List<ExportModel> models = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    ExportModel exportModel = exportPage.getData().getList().get(i);
                    ids.add(exportModel.getId());
                    exportModel.setLiableCertType("居民身份证" + exportModel.getLiableCertNum().length() + "位");
                    exportModel.setHandledCertType("居民身份证" + exportModel.getHandledCertNum().length() + "位");
                    exportModel.setAccessProvince(provinces.get(exportModel.getAccessProvince()));
                    exportModel.setOperate("新增");
                    models.add(exportModel);
                }

                //创建生成文件的文件夹
                String orderNo = DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(4);
                currentFold = "联通签名报备" + "/" + exportOrderNo + "/" + orderNo;
                File fold = new File(rootPath + currentFold);
                while (!fold.exists()) {
                    fold.mkdirs();
                }

                //生成excel文件
                String excelFile = rootPath + currentFold + "/" + orderNo + ".xlsx";
                writerExcelFile(excelFile, models, "unic.xlsx");

            } catch (Exception e) {
                e.printStackTrace();
            }

            String tempPath = rootPath + "联通签名报备" + "/" + "temp";
            File temp = new File(tempPath);
            while (!temp.exists()) {
                temp.mkdirs();
            }
            String zipFilePath = temp + "/" + exportOrderNo + ".zip";

            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(new File(zipFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // 调用工具类压缩文件夹
            ZipUtils.toZip(rootPath + "联通签名报备" + "/" + exportOrderNo, fileOutputStream, true);

            // 调用工具类设置响应格式
            try {
                ExportRegisterModel exportRegisterModel = new ExportRegisterModel();
                exportRegisterModel.setRegisterOrderNo(exportOrderNo);
                exportRegisterModel.setCarrier(carrier);
                exportRegisterModel.setIds(ids);
                exportRegisterModel.setCreatedBy(user.getRealName());
                this.accountSignRegisterForFileService.register(exportRegisterModel);
                this.downLoadFile(request, response, "联通-" + exportOrderNo + ".zip", zipFilePath);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("导出失败~");
            }

        }

        //电信导出
        if ("TELC".equals(carrier)) {
            //创建生成文件的文件夹
            String currentFold;
            try {
                Integer size = exportPage.getData().getList().size();
                List<ExportModel> models = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    ExportModel exportModel = exportPage.getData().getList().get(i);
                    ids.add(exportModel.getId());
                    exportModel.setServiceType(exportModel.getServiceType().replace(",", "&"));
                    exportModel.setIsAuthorize("有");
                    exportModel.setLiableCertType("身份证");
                    exportModel.setHandledCertType("身份证");
                    exportModel.setIsSign("是");
                    exportModel.setIsGreen("否");
                    exportModel.setBlackList("黑名单");
                    exportModel.setAccessProvince(provinces.get(exportModel.getAccessProvince()));
                    exportModel.setOperate("新增");
                    models.add(exportModel);
                }

                //创建生成文件的文件夹
                String orderNo = DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(4);
                currentFold = "电信签名报备" + "/" + exportOrderNo + "/" + orderNo;
                File fold = new File(rootPath + currentFold);
                while (!fold.exists()) {
                    fold.mkdirs();
                }

                //生成excel文件
                String excelFile = rootPath + currentFold + "/" + orderNo + ".xlsx";
                writerExcelFile(excelFile, models, "telc.xlsx");

            } catch (Exception e) {
                e.printStackTrace();
            }

            String tempPath = rootPath + "电信签名报备" + "/" + "temp";
            File temp = new File(tempPath);
            while (!temp.exists()) {
                temp.mkdirs();
            }
            String zipFilePath = temp + "/" + exportOrderNo + ".zip";

            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(new File(zipFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // 调用工具类压缩文件夹
            ZipUtils.toZip(rootPath + "电信签名报备" + "/" + exportOrderNo, fileOutputStream, true);

            // 调用工具类设置响应格式
            try {
                ExportRegisterModel exportRegisterModel = new ExportRegisterModel();
                exportRegisterModel.setRegisterOrderNo(exportOrderNo);
                exportRegisterModel.setCarrier(carrier);
                exportRegisterModel.setIds(ids);
                exportRegisterModel.setCreatedBy(user.getRealName());
                this.accountSignRegisterForFileService.register(exportRegisterModel);
                this.downLoadFile(request, response, "电信-" + exportOrderNo + ".zip", zipFilePath);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("导出失败~");
            }

        }
    }

    /**
     * @param resultFile 结果文件，省去了根据模板文件生成的步骤
     */
    public void writerExcelFile(String resultFile, List<ExportModel> registerData, String fileType) {

        try {

            ClassPathResource classPathResource = new ClassPathResource("static/files/register/" + fileType);
            InputStream fis = classPathResource.getInputStream();
            // 根据模板文件生成目标文件
            ExcelWriter excelWriter = EasyExcel.write(resultFile).withTemplate(fis).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            // 每次都会重新生成新的一行，而不是使用下面的空行
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            //占位符替换，这里定义了 hisData
            excelWriter.fill(new FillWrapper("rd", registerData), fillConfig, writeSheet);
            excelWriter.finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通道供应商
     */
    private Map<String, String> provinces(HttpServletRequest request) {
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //运营商
        DictType provinces = dictMap.get("provices");

        Map<String, String> provincesMap = new HashMap<>();
        for (Dict dict : provinces.getDict()) {
            provincesMap.put(dict.getFieldCode(), dict.getFieldName());
        }

        return provincesMap;
    }

    /**
     * 设置导出zip的响应格式
     *
     * @param request
     * @param response
     * @param fileZip  zip的名字
     * @param filePath zip的路径
     * @throws UnsupportedEncodingException
     */
    public static void downLoadFile(HttpServletRequest request, HttpServletResponse response, String fileZip, String filePath) throws UnsupportedEncodingException {

        //进行浏览器下载
        final String userAgent = request.getHeader("USER-AGENT");
        //判断浏览器代理并分别设置响应给浏览器的编码格式
        String finalFileName = null;
        if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident")) {
            // IE浏览器
            finalFileName = URLEncoder.encode(fileZip, "UTF8");
            System.out.println("IE浏览器");
        } else if (StringUtils.contains(userAgent, "Mozilla")) {
            // google,火狐浏览器
            finalFileName = new String(fileZip.getBytes(), "ISO8859-1");
        } else {
            // 其他浏览器
            finalFileName = URLEncoder.encode(fileZip, "UTF8");
        }
        // 告知浏览器下载文件，而不是直接打开，浏览器默认为打开
        response.setContentType("application/x-download");
        // 下载文件的名称
        response.setHeader("Content-Disposition", "attachment;filename=\"" + finalFileName + "\"");

        ServletOutputStream servletOutputStream = null;
        try {
            servletOutputStream = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataOutputStream temps = new DataOutputStream(servletOutputStream);
        // 浏览器下载文件的路径
        DataInputStream in = null;
        try {
            in = new DataInputStream(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] b = new byte[2048];
        // 之后用来删除临时压缩文件
        File reportZip = new File(filePath);
        try {
            while ((in.read(b)) != -1) {
                temps.write(b);
            }
            temps.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (temps != null) {
                try {
                    temps.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reportZip != null) {
                // 删除服务器本地产生的临时压缩文件!
                reportZip.delete();
            }
            try {
                servletOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
