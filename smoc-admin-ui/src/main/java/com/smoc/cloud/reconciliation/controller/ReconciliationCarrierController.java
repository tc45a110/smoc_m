package com.smoc.cloud.reconciliation.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationCarrierItemsValidator;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationAccountModel;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationChannelCarrierModel;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationEnterpriseModel;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.reconciliation.service.ReconciliationCarrierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * 运营商对账
 */
@Slf4j
@Controller
@RequestMapping("/finance/reconciliation/carrier")
public class ReconciliationCarrierController {

    @Autowired
    private ReconciliationCarrierService reconciliationCarrierService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 运营商对账列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("reconciliation/finance_reconciliation_carrier_list");

        //初始化数据
        PageParams<ReconciliationChannelCarrierModel> params = new PageParams<ReconciliationChannelCarrierModel>();
        params.setPageSize(20);
        params.setCurrentPage(1);
        ReconciliationChannelCarrierModel reconciliationChannelCarrierModel = new ReconciliationChannelCarrierModel();
        reconciliationChannelCarrierModel.setChannelPeriod(DateTimeUtils.getMonth());
        params.setParams(reconciliationChannelCarrierModel);

        //查询
        ResponseData<PageList<ReconciliationChannelCarrierModel>> data = reconciliationCarrierService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("reconciliationChannelCarrierModel", reconciliationChannelCarrierModel);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 运营商对账列表分页
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ReconciliationChannelCarrierModel reconciliationChannelCarrierModel, PageParams pageParams) {
        ModelAndView view = new ModelAndView("reconciliation/finance_reconciliation_carrier_list");

        //分页查询
        pageParams.setParams(reconciliationChannelCarrierModel);

        ResponseData<PageList<ReconciliationChannelCarrierModel>> data = reconciliationCarrierService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("reconciliationChannelCarrierModel", reconciliationChannelCarrierModel);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     * 运营商对账中心
     *
     * @return
     */
    @RequestMapping(value = "/reconciliation/center/{channelProvder}/{startDate}", method = RequestMethod.GET)
    public ModelAndView reconciliation_center(@PathVariable String channelProvder,@PathVariable String startDate, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("reconciliation/finance_reconciliation_carrier_reconciliation_center");

        view.addObject("channelProvder", channelProvder);
        view.addObject("startDate", startDate);

        return view;
    }

    /**
     * 运营商对账
     *
     * @return
     */
    @RequestMapping(value = "/reconciliation/{channelProvder}/{startDate}", method = RequestMethod.GET)
    public ModelAndView reconciliation(@PathVariable String channelProvder,@PathVariable String startDate, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("reconciliation/finance_reconciliation_carrier_reconciliation");

        //查询数据
        ResponseData<List<ReconciliationCarrierItemsValidator>> data = reconciliationCarrierService.findReconciliationCarrier(startDate,channelProvder);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("op", "add");

        if(data.getData().get(0).getCarrierTotalSendQuantity()>0){
            view.addObject("op", "edit");
        }

        ReconciliationChannelCarrierModel reconciliationChannelCarrierModel = new ReconciliationChannelCarrierModel();
        reconciliationChannelCarrierModel.setChannelPeriod(startDate);
        reconciliationChannelCarrierModel.setChannelProvder(channelProvder);

        view.addObject("list", data.getData());
        view.addObject("channelProvder", channelProvder);
        view.addObject("startDate", startDate);
        view.addObject("rowspan", data.getData().size());
        view.addObject("reconciliationChannelCarrierModel", reconciliationChannelCarrierModel);

        return view;
    }

    /**
     * 保存对账
     * @param reconciliationChannelCarrierModel
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute ReconciliationChannelCarrierModel reconciliationChannelCarrierModel, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("reconciliation/finance_reconciliation_carrier_reconciliation");

        //查询数据
        ResponseData<List<ReconciliationCarrierItemsValidator>> itemsData = reconciliationCarrierService.findReconciliationCarrier(reconciliationChannelCarrierModel.getChannelPeriod(),reconciliationChannelCarrierModel.getChannelProvder());
        if (!ResponseCode.SUCCESS.getCode().equals(itemsData.getCode())) {
            view.addObject("error", itemsData.getCode() + ":" + itemsData.getMessage());
            return view;
        }

        List<ReconciliationCarrierItemsValidator> list = itemsData.getData();
        for(int i=0;i<list.size();i++){
            ReconciliationCarrierItemsValidator info = list.get(i);
            info.setChannelPeriod(reconciliationChannelCarrierModel.getChannelPeriod());
            info.setChannelProvder(reconciliationChannelCarrierModel.getChannelProvder());
            info.setCarrierTotalAmount(new BigDecimal(request.getParameter("carrierAccount"+i)));
            info.setCarrierTotalSendQuantity(Long.parseLong(request.getParameter("carrierMessage"+i)));
            info.setCreatedBy(user.getRealName());
            info.setChannelPeriodStatus("2");
        }

        reconciliationChannelCarrierModel.setCarrierList(list);

        //记录日志
        log.info("[运营商对账][{}][{}]数据:{}",op, user.getUserName(), JSON.toJSONString(reconciliationChannelCarrierModel));

        //保存操作
        ResponseData data = reconciliationCarrierService.save(reconciliationChannelCarrierModel);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("RECONEILIATION_CARRIER", reconciliationChannelCarrierModel.getChannelProvder(), "add".equals(op) ? user.getUserName() : user.getUserName(), op, "add".equals(op) ? "添加运营商对账信息" : "修改运营商对账信息", JSON.toJSONString(reconciliationChannelCarrierModel));
        }

        ResponseData<List<ReconciliationCarrierItemsValidator>> items = reconciliationCarrierService.findReconciliationCarrier(reconciliationChannelCarrierModel.getChannelPeriod(),reconciliationChannelCarrierModel.getChannelProvder());

        view.addObject("list", items.getData());
        view.addObject("channelProvder", reconciliationChannelCarrierModel.getChannelProvder());
        view.addObject("startDate", reconciliationChannelCarrierModel.getChannelPeriod());
        view.addObject("rowspan", list.size());
        view.addObject("reconciliationChannelCarrierModel", reconciliationChannelCarrierModel);
        view.addObject("op", "edit");
        return view;
    }

    /**
     * 运营商对账记录
     *
     * @return
     */
    @RequestMapping(value = "/reconciliation/record/{channelProvder}/{startDate}", method = RequestMethod.GET)
    public ModelAndView reconciliation_record(@PathVariable String channelProvder, @PathVariable String startDate,  HttpServletRequest request) {
        ModelAndView view = new ModelAndView("reconciliation/finance_reconciliation_carrier_reconciliation_record");

        //初始化数据
        PageParams<ReconciliationChannelCarrierModel> params = new PageParams<ReconciliationChannelCarrierModel>();
        params.setPageSize(3);
        params.setCurrentPage(1);
        ReconciliationChannelCarrierModel reconciliationChannelCarrierModel = new ReconciliationChannelCarrierModel();
        reconciliationChannelCarrierModel.setChannelPeriod(startDate);
        reconciliationChannelCarrierModel.setChannelProvder(channelProvder);
        params.setParams(reconciliationChannelCarrierModel);

        //查询
        ResponseData<PageList<ReconciliationChannelCarrierModel>> data = reconciliationCarrierService.reconciliationCarrierRecord(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("list", data.getData().getList());
        view.addObject("startDate", startDate);
        view.addObject("channelProvder", channelProvder);
        return view;

    }

    /**
     * 运营商对账列表
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{channelProvder}/{startDate}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String channelProvder, @PathVariable String startDate, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("reconciliation/finance_reconciliation_carrier_view_center");

        view.addObject("startDate", startDate);
        view.addObject("channelProvder", channelProvder);

        return view;
    }

    /**
     * 运营商对账记录
     *
     * @return
     */
    @RequestMapping(value = "/view/{channelProvder}/{startDate}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String channelProvder, @PathVariable String startDate, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("reconciliation/finance_reconciliation_carrier_view");

        //初始化数据
        PageParams<ReconciliationChannelCarrierModel> params = new PageParams<ReconciliationChannelCarrierModel>();
        params.setPageSize(1);
        params.setCurrentPage(1);
        ReconciliationChannelCarrierModel reconciliationChannelCarrierModel = new ReconciliationChannelCarrierModel();
        reconciliationChannelCarrierModel.setChannelPeriod(startDate);
        reconciliationChannelCarrierModel.setChannelProvder(channelProvder);
        params.setParams(reconciliationChannelCarrierModel);

        //查询
        ResponseData<PageList<ReconciliationChannelCarrierModel>> data = reconciliationCarrierService.reconciliationCarrierRecord(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("list", data.getData().getList());
        view.addObject("startDate", startDate);
        view.addObject("channelProvder", channelProvder);
        return view;
    }

    /**
     * 导出excel 结账单
     *
     * @return
     */
    @RequestMapping(value = "/excel/{channelProvder}/{startDate}", method = RequestMethod.GET)
    public void excel(@PathVariable String channelProvder, @PathVariable String startDate, HttpServletResponse response, HttpServletRequest request) {

        //运营商
        Map<String,String>  carrierMap = this.carrier(request);

        //初始化数据
        PageParams<ReconciliationChannelCarrierModel> params = new PageParams<ReconciliationChannelCarrierModel>();
        params.setPageSize(1);
        params.setCurrentPage(1);
        ReconciliationChannelCarrierModel reconciliationChannelCarrierModel = new ReconciliationChannelCarrierModel();
        reconciliationChannelCarrierModel.setChannelPeriod(startDate);
        reconciliationChannelCarrierModel.setChannelProvder(channelProvder);
        params.setParams(reconciliationChannelCarrierModel);

        //查询
        ResponseData<PageList<ReconciliationChannelCarrierModel>> data = reconciliationCarrierService.reconciliationCarrierRecord(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return ;
        }

        List<Map<String, Object>> list = new ArrayList<>();
        if (null != data.getData() && data.getData().getList().size() > 0) {
            List<ReconciliationCarrierItemsValidator> itemsList = data.getData().getList().get(0).getCarrierList();
            if (null != itemsList && itemsList.size() > 0)
                for (ReconciliationCarrierItemsValidator model : itemsList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("channel", model.getSrcId()+"("+model.getChannelId()+")");
                    map.put("totalSend", model.getTotalSendQuantity());
                    map.put("price", model.getPrice());
                    map.put("totalSum", model.getTotalAmount());
                    map.put("carrierTotalSum",model.getCarrierTotalAmount());
                    map.put("carrierTotalsend", model.getCarrierTotalSendQuantity());
                    map.put("sendDifference", model.getQuantityDifference());
                    map.put("amountDifference", model.getAmountDifference());
                    list.add(map);
                }
        }

        OutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("static/files/templates/" + "reconciliation_carrier.xlsx");
            InputStream fis = classPathResource.getInputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(carrierMap.get(channelProvder) + "-" + startDate + "-结算单.xls", "utf-8");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

            out = response.getOutputStream();
            bos = new BufferedOutputStream(out);

            //读取Excel
            ExcelWriter excelWriter = EasyExcel.write(bos).withTemplate(fis).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();

            Map<String, Object> map = new HashMap<>();
            map.put("date", carrierMap.get(channelProvder) + "-" + startDate);
            excelWriter.fill(list, writeSheet);
            excelWriter.fill(map, writeSheet);
            excelWriter.finish();
            bos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  通道供应商
     */
    private Map<String,String> carrier(HttpServletRequest request) {
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //运营商
        DictType carrier = dictMap.get("specificProvder");

        Map<String,String> dictValueMap = new HashMap<>();
        for (Dict dict : carrier.getDict()) {
            dictValueMap.put(dict.getFieldCode(),dict.getFieldName());
        }

        return dictValueMap;
    }

}
