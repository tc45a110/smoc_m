package com.smoc.cloud.reconciliation.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.gson.Gson;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationAccountModel;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationEnterpriseModel;
import com.smoc.cloud.customer.service.EnterpriseService;
import com.smoc.cloud.reconciliation.service.ReconciliationAccountService;
import com.smoc.cloud.reconciliation.service.ReconciliationPeriodService;
import com.smoc.cloud.utils.TestFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        ResponseData<Map<String, String>> existPeriod = reconciliationPeriodService.findAccountPeriod();
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

        ResponseData<Map<String, String>> existPeriod = reconciliationPeriodService.findAccountPeriod();
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
     * 导出excel 结账单
     *
     * @return
     */
    @RequestMapping(value = "/ec/excel/{enterpriseId}/{accountPeriod}", method = RequestMethod.GET)
    public void excel(@PathVariable String enterpriseId, @PathVariable String accountPeriod, HttpServletResponse response, HttpServletRequest request) {

        ResponseData<EnterpriseBasicInfoValidator> enterpriseResponseDate = enterpriseService.findById(enterpriseId);

        //运营商
        Map<String,String>  carrierMap = this.carrier(request);
        //对接主体公司
        Map<String,String> corporationMap = this.corporation(request);
        //业务类型
        Map<String,String> businessTypeMap = this.businessType(request);

        //初始化数据
        PageParams<ReconciliationEnterpriseModel> params = new PageParams<ReconciliationEnterpriseModel>();
        params.setPageSize(1);
        params.setCurrentPage(1);
        ReconciliationEnterpriseModel reconciliationEnterpriseModel = new ReconciliationEnterpriseModel();
        reconciliationEnterpriseModel.setEnterpriseId(enterpriseId);
        reconciliationEnterpriseModel.setAccountingPeriod(accountPeriod);
        params.setParams(reconciliationEnterpriseModel);

        //查询
        ResponseData<PageList<ReconciliationEnterpriseModel>> responseData = reconciliationAccountService.page(params);

        BigDecimal total = new BigDecimal("0.00");
        List<Map<String, Object>> list = new ArrayList<>();
        if (null != responseData.getData() && responseData.getData().getList().size() > 0) {
            ReconciliationEnterpriseModel enterpriseModel = responseData.getData().getList().get(0);
            total = new BigDecimal(enterpriseModel.getSum());
            if (null != enterpriseModel && null != enterpriseModel.getAccounts() && enterpriseModel.getAccounts().size() > 0)
                for (ReconciliationAccountModel model : enterpriseModel.getAccounts()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("period", model.getAccountingPeriod());
                    map.put("account", model.getAccount());
                    map.put("carrier", carrierMap.get(model.getCarrier()));
                    map.put("type",businessTypeMap.get(model.getBusinessType()));
                    map.put("quantity", model.getSendSum());
                    map.put("price", model.getPrice());
                    map.put("amount", model.getTotalSum());
                    list.add(map);
                }
        }

        OutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("static/files/templates/" + "reconciliation.xlsx");
            InputStream fis = classPathResource.getInputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(enterpriseResponseDate.getData().getEnterpriseName() + "-" + accountPeriod + "-结算单.xls", "utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

            out = response.getOutputStream();
            bos = new BufferedOutputStream(out);

            //读取Excel
            ExcelWriter excelWriter = EasyExcel.write(bos).withTemplate(fis).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();

            Map<String, Object> map = new HashMap<>();
            map.put("partA", enterpriseResponseDate.getData().getEnterpriseName());
            map.put("partB", corporationMap.get(enterpriseResponseDate.getData().getAccessCorporation()));
            map.put("date", accountPeriod);
            map.put("total", total);
            excelWriter.fill(list, writeSheet);
            excelWriter.fill(map, writeSheet);
            excelWriter.finish();
            bos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 运营商和 国际区域合并
     */
    private Map<String,String> carrier(HttpServletRequest request) {
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //运营商
        DictType carrier = dictMap.get("carrier");
        //国际区域
        DictType internationalArea = dictMap.get("internationalArea");

        Map<String,String> dictValueMap = new HashMap<>();
        for (Dict dict : carrier.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        for (Dict dict : internationalArea.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        return dictValueMap;
    } //业务类型


    /**
     * 对接主体公司
     */
    private Map<String,String> corporation(HttpServletRequest request) {
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //对接主体公司
        DictType corporation = dictMap.get("corporation");

        Map<String,String> dictValueMap = new HashMap<>();
        for (Dict dict : corporation.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        return dictValueMap;
    }

    /**
     * 业务类型
     */
    private Map<String,String> businessType(HttpServletRequest request) {
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //对接主体公司
        DictType businessType = dictMap.get("businessType");

        Map<String,String> dictValueMap = new HashMap<>();
        for (Dict dict : businessType.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }
        return dictValueMap;
    }


}
