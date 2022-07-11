package com.smoc.cloud.reconciliation.controller;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationCarrierItemsValidator;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationChannelCarrierModel;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationEnterpriseModel;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.reconciliation.service.ReconciliationCarrierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 运营商对账
 */
@Slf4j
@Controller
@RequestMapping("/finance/reconciliation/carrier")
public class ReconciliationCarrierController {

    @Autowired
    private ReconciliationCarrierService reconciliationCarrierService;

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
        params.setPageSize(3);
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

        ReconciliationChannelCarrierModel reconciliationChannelCarrierModel = new ReconciliationChannelCarrierModel();
        reconciliationChannelCarrierModel.setChannelPeriod(startDate);
        reconciliationChannelCarrierModel.setChannelProvder(channelProvder);

        view.addObject("list", data.getData());
        view.addObject("channelProvder", channelProvder);
        view.addObject("startDate", startDate);
        view.addObject("rowspan", data.getData().size());
        view.addObject("reconciliationChannelCarrierModel", reconciliationChannelCarrierModel);
        view.addObject("op", "add");
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
            info.setChannelPeriodStatus("1");
        }

        reconciliationChannelCarrierModel.setCarrierList(list);

        //记录日志
        log.info("[运营商对账][{}][{}]数据:{}","add", user.getUserName(), JSON.toJSONString(reconciliationChannelCarrierModel));

        //保存操作
        ResponseData data = reconciliationCarrierService.save(reconciliationChannelCarrierModel);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("list", list);
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
    @RequestMapping(value = "/reconciliation/record/{id}", method = RequestMethod.GET)
    public ModelAndView reconciliation_record(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("reconciliation/finance_reconciliation_carrier_reconciliation_record");

        //查询数据

        return view;
    }

    /**
     * 运营商对账列表
     *
     * @return
     */
    @RequestMapping(value = "/view/center/{id}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("reconciliation/finance_reconciliation_carrier_view_center");

        //查询数据

        return view;
    }

    /**
     * 运营商对账列表
     *
     * @return
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("reconciliation/finance_reconciliation_carrier_view");

        //查询数据

        return view;
    }
}
