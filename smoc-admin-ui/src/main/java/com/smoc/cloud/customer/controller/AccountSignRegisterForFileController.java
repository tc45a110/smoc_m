package com.smoc.cloud.customer.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.google.gson.Gson;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
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
import com.smoc.cloud.utils.CompressUtil;
import com.smoc.cloud.utils.Utils;
import com.smoc.cloud.utils.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
        view.addObject("carrierCounts", responseData.getData());

        Export export = new Export();
        export.setNumber(1000);
        view.addObject("export", export);
        return view;
    }

    /**
     * 进入导出页面
     *
     * @return
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void export(@ModelAttribute Export export, HttpServletRequest request, HttpServletResponse response) {
        ExportModel queryExportModel = new ExportModel();
        queryExportModel.setRegisterCarrier(export.getCarrier());
        //初始化数据
        PageParams<ExportModel> params = new PageParams<>();
        params.setPageSize(1000);
        params.setCurrentPage(1);
        params.setParams(queryExportModel);
        ResponseData<PageList<ExportModel>> exportPage = this.accountSignRegisterForFileService.export(params);
        if (!ResponseCode.SUCCESS.getCode().equals(exportPage.getCode())) {
            return;
        }

        if (!(null != exportPage.getData() && null != exportPage.getData().getList() && exportPage.getData().getList().size() > 0)) {
            return;
        }

        //省份字典
        Map<String,String> provinces = this.provinces(request);

        //签名报备，生成文件根目录
        String rootPath = smocProperties.getSignRegisterRootPath();
        //企业文件根目录
        String certifyFileRootPath = smocProperties.getCertifyFileRootPath();

        //报备单号
        String exportOrderNo = DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(4);

        Set<String> zipFiles = new HashSet<>();

        //移动导出
        if ("CMCC".equals(export.getCarrier())) {
            //创建生成文件的文件夹
            String currentFold;
            try {
                Set<String> files = new HashSet<>();
                Integer size = exportPage.getData().getList().size();
                List<ExportModel> models = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    ExportModel exportModel = exportPage.getData().getList().get(i);
                    //复制营业执照/组织机构代码证
                    files.add(certifyFileRootPath+ File.separator+ exportModel.getId()+ File.separator + exportModel.getBusinessLicense());

                    //复制责任人（含法人）证件
                    files.add(certifyFileRootPath + File.separator+ exportModel.getId()+ File.separator + exportModel.getLiableCertUrl());

                    //复制经办人证件
                    files.add(certifyFileRootPath+ File.separator+ exportModel.getId()+ File.separator + exportModel.getHandledCertUrl());

                    //复制授权书
                    files.add(certifyFileRootPath + File.separator+ exportModel.getId()+ File.separator +  exportModel.getAuthorizeCert());

                    //复制授权书
                    files.add(certifyFileRootPath + File.separator+ exportModel.getId()+ File.separator +  exportModel.getOfficePhotos());


                    exportModel.setAccessProvince(provinces.get(exportModel.getAccessProvince()));
                    exportModel.setOperate("新增");
                    models.add(exportModel);
                    if (!(i == 0) && ((i + 1) % 100 == 0 || i == (size - 1))) {

                        //创建生成文件的文件夹
                        String orderNo = DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(4);
                        currentFold = "移动签名报备" + File.separator +exportOrderNo + File.separator +orderNo;
                        File fold = new File(rootPath + currentFold);
                        while (!fold.exists()) {
                            fold.mkdirs();
                        }

                        //生成图片的压缩文件
                        String zipFile = rootPath + currentFold + File.separator + "附件.zip";
                        CompressUtil.compress(files, zipFile, false);

                        //生成excel文件
                        String excelFile = rootPath + currentFold + File.separator + orderNo + ".xlsx";
                        writerExcelFile(excelFile, models);
                        log.info("[files]:{}", new Gson().toJson(files));
                        files = new HashSet<>();
                        models = new ArrayList<>();
                    }
                }





            } catch (Exception e) {
                e.printStackTrace();
            }

            String tempPath = rootPath+ File.separator +"移动签名报备" + File.separator +"temp";
            File temp = new File(tempPath);
            while (!temp.exists()) {
                temp.mkdirs();
            }
            String zipFilePath = temp+File.separator +exportOrderNo+".zip";

            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(new File(zipFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // 调用工具类压缩文件夹
            ZipUtils.toZip(rootPath+ File.separator +"移动签名报备" + File.separator +exportOrderNo, fileOutputStream, true);

            // 调用工具类设置响应格式
            try {
                this.downLoadFile(request, response, exportOrderNo+".zip", zipFilePath);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("导出失败~");
            }

        }
    }

    /**
     * @param resultFile 结果文件，省去了根据模板文件生成的步骤
     */
    public void writerExcelFile(String resultFile, List<ExportModel> registerData) {

//        ClassPathResource classPathResource = new ClassPathResource("static/files/register/CMCC.xlsx");
//        log.info("[filePath]:{}",classPathResource.getPath());
//        String path = this.getClass().getResource("/static/files/register/CMCC.xlsx").getPath();
//        log.info("[path]:{}",path);
//        // 模板文件
        String templateFile = "F:\\workbench\\smoc_cloud\\smoc-admin-ui\\src\\main\\resources\\static\\files\\register/CMCC.xlsx";
        try {
            // 根据模板文件生成目标文件
            ExcelWriter excelWriter = EasyExcel.write(resultFile).withTemplate(templateFile)
                    // 单独设置单元格格式
//                .registerWriteHandler(new CellStyleHandler())
                    .build();

            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            // 每次都会重新生成新的一行，而不是使用下面的空行
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.FALSE).build();

            //占位符替换，这里定义了 hisData
            excelWriter.fill(new FillWrapper("rd", registerData), fillConfig, writeSheet);
            excelWriter.finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  通道供应商
     */
    private Map<String,String> provinces(HttpServletRequest request) {
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //运营商
        DictType provinces = dictMap.get("provices");

        Map<String,String> provincesMap = new HashMap<>();
        for (Dict dict : provinces.getDict()) {
            provincesMap.put(dict.getFieldCode(),dict.getFieldName());
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
        DataOutputStream temps = new DataOutputStream(
                servletOutputStream);
        // 浏览器下载文件的路径
        DataInputStream in = null;
        try {
            in = new DataInputStream(
                    new FileInputStream(filePath));
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
