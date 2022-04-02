package com.smoc.cloud.reconciliation.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.gson.Gson;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationEnterpriseModel;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.reconciliation.service.ReconciliationAccountService;
import com.smoc.cloud.reconciliation.service.ReconciliationPeriodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 业务账号对账
 */
@Slf4j
@Controller
@RequestMapping("/reconciliation/account")
public class ReconciliationAccountController {

    @Autowired
    private EnterpriseService enterpriseService;

    @Autowired
    private ReconciliationAccountService reconciliationAccountService;

    @Autowired
    private ReconciliationPeriodService reconciliationPeriodService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/ec/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("reconciliation/account/reconciliation_account_list");

        //初始化数据
        PageParams<ReconciliationEnterpriseModel> params = new PageParams<ReconciliationEnterpriseModel>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        ReconciliationEnterpriseModel reconciliationEnterpriseModel = new ReconciliationEnterpriseModel();
        params.setParams(reconciliationEnterpriseModel);

        //查询
        ResponseData<PageList<ReconciliationEnterpriseModel>> data = reconciliationAccountService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String,String>> existPeriod = reconciliationPeriodService.findAccountPeriod();
        if (!ResponseCode.SUCCESS.getCode().equals(existPeriod.getCode())) {
            view.addObject("error", existPeriod.getCode() + ":" + existPeriod.getMessage());
            return view;
        }

        view.addObject("existPeriod", existPeriod.getData());
        view.addObject("reconciliationEnterpriseModel", reconciliationEnterpriseModel);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        String servletPath = request.getServletPath();
        String contextPath = request.getContextPath();
        log.info("[servletPath]:{}",servletPath);
        log.info("[contextPath]:{}",contextPath);

        //设置文件路径
//        ClassPathResource classPathResource = new ClassPathResource("static/files/模板.txt");
//        log.info("[getFilename]:{}",classPathResource.getFilename());
        //log.info("[getFilename]:{}",classPathResource.getURL().getPath());

        return view;
    }

    /**
     * 分页
     *
     * @return
     */
    @RequestMapping(value = "/ec/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ReconciliationEnterpriseModel reconciliationEnterpriseModel, PageParams pageParams) {
        ModelAndView view = new ModelAndView("reconciliation/account/reconciliation_account_list");

        //分页查询
        pageParams.setParams(reconciliationEnterpriseModel);

        ResponseData<PageList<ReconciliationEnterpriseModel>> data = reconciliationAccountService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<Map<String,String>> existPeriod = reconciliationPeriodService.findAccountPeriod();
        if (!ResponseCode.SUCCESS.getCode().equals(existPeriod.getCode())) {
            view.addObject("error", existPeriod.getCode() + ":" + existPeriod.getMessage());
            return view;
        }

        view.addObject("existPeriod", existPeriod.getData());
        view.addObject("reconciliationEnterpriseModel", reconciliationEnterpriseModel);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/ec/excel/{enterpriseId}/{accountPeriod}", method = RequestMethod.GET)
    public void excel(@PathVariable String enterpriseId,@PathVariable String accountPeriod, HttpServletResponse response, HttpServletRequest request) {

        ResponseData<EnterpriseBasicInfoValidator> enterpriseResponseDate = enterpriseService.findById(enterpriseId);
        ResponseData<Map<String, Object>> responseData = reconciliationAccountService.getEnterpriseBills(enterpriseId,accountPeriod);

        OutputStream out = null;
        BufferedOutputStream bos = null;
        try {

            String servletPath = request.getServletPath();

            String templateFileName = "templates" + File.separator + "模板.xls";

            //设置文件路径
            ClassPathResource classPathResource = new ClassPathResource("static/files/模板.txt");


            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("下载后的名称.xls", "utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

            out = response.getOutputStream();
            bos = new BufferedOutputStream(out);

            //读取Excel
            ExcelWriter excelWriter = EasyExcel.write(bos).withTemplate(templateFileName).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();

//            //list map 是查询并需导出的数据，并且里面的字段和excel需要导出的字段对应
//            // 直接写入Excel数据
//            List<Map> list = xxx;
//            Map<String,Object> map = yyy;
//            excelWriter.fill(list, writeSheet);
//            excelWriter.fill(map, writeSheet);
//            excelWriter.finish();
//            bos.flush();

        } catch (Exception e) {

        }
    }


}
