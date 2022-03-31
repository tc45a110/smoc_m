package com.smoc.cloud.reconciliation.controller;


import com.google.gson.Gson;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.reconciliation.model.ReconciliationEnterpriseModel;
import com.smoc.cloud.reconciliation.service.ReconciliationAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 业务账号对账
 */
@Slf4j
@Controller
@RequestMapping("/reconciliation/account")
public class ReconciliationAccountController {

    @Autowired
    private ReconciliationAccountService reconciliationAccountService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/ec/list", method = RequestMethod.GET)
    public ModelAndView list() {
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

        log.info("[page]:{}", new Gson().toJson(data.getData().getList()));

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

        view.addObject("reconciliationEnterpriseModel", reconciliationEnterpriseModel);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }
}
