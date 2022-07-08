package com.smoc.cloud.reconciliation.controller;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationChannelCarrierModel;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationEnterpriseModel;
import com.smoc.cloud.reconciliation.service.ReconciliationCarrierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
     * 运营商对账
     *
     * @return
     */
    @RequestMapping(value = "/reconciliation/{id}", method = RequestMethod.GET)
    public ModelAndView reconciliation(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("reconciliation/finance_reconciliation_carrier_reconciliation");

        //查询数据

        return view;
    }

    /**
     * 运营商对账中心
     *
     * @return
     */
    @RequestMapping(value = "/reconciliation/center/{id}", method = RequestMethod.GET)
    public ModelAndView reconciliation_center(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("reconciliation/finance_reconciliation_carrier_reconciliation_center");

        //查询数据

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
