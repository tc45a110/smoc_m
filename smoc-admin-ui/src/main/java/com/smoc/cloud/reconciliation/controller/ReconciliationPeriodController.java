package com.smoc.cloud.reconciliation.controller;

import com.google.gson.Gson;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.finance.validator.FinanceAccountValidator;
import com.smoc.cloud.common.smoc.reconciliation.ReconciliationPeriodValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.reconciliation.service.ReconciliationPeriodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 业务账号对账
 */
@Slf4j
@Controller
@RequestMapping("/reconciliation/account")
public class ReconciliationPeriodController {

    @Autowired
    private ReconciliationPeriodService reconciliationPeriodService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("reconciliation/account/reconciliation_account_list");

        //初始化数据
        PageParams<ReconciliationPeriodValidator> params = new PageParams<ReconciliationPeriodValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        ReconciliationPeriodValidator reconciliationPeriodValidator = new ReconciliationPeriodValidator();
        reconciliationPeriodValidator.setStatus("1");
        params.setParams(reconciliationPeriodValidator);

        //查询
        ResponseData<PageList<ReconciliationPeriodValidator>> data = reconciliationPeriodService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //log.info("[page]:{}", new Gson().toJson(data));

        view.addObject("reconciliationPeriodValidator", reconciliationPeriodValidator);
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
    public ModelAndView page(@ModelAttribute ReconciliationPeriodValidator reconciliationPeriodValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("reconciliation/account/reconciliation_account_list");

        reconciliationPeriodValidator.setStatus("1");
        //分页查询
        pageParams.setParams(reconciliationPeriodValidator);

        ResponseData<PageList<FinanceAccountValidator>> data = reconciliationPeriodService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("reconciliationPeriodValidator", reconciliationPeriodValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;
    }

    /**
     * 出账
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add() {

        ModelAndView view = new ModelAndView("reconciliation/account/reconciliation_account_edit");


        ResponseData<Map<String,String>> existPeriod = reconciliationPeriodService.findAccountPeriod();
        if (!ResponseCode.SUCCESS.getCode().equals(existPeriod.getCode())) {
            view.addObject("error", existPeriod.getCode() + ":" + existPeriod.getMessage());
            return view;
        }

        log.info("[]:{}",new Gson().toJson(existPeriod.getData()));
        List<String> dateList = new ArrayList<>();
        for(int i=1;i<7;i++){
            try {
                String date = DateTimeUtils.dateFormat(DateTimeUtils.dateAddMonths(new Date(),-i), "yyyy-MM");
                if(null == existPeriod.getData() || null == existPeriod.getData().get(date)) {
                    dateList.add(date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        log.info("[existPeriod.getData()]:{}",new Gson().toJson(existPeriod.getData()));
        log.info("[dateList]:{}",new Gson().toJson(dateList));

        ReconciliationPeriodValidator reconciliationPeriodValidator = new ReconciliationPeriodValidator();
        view.addObject("dateList",dateList);
        view.addObject("reconciliationPeriodValidator", reconciliationPeriodValidator);

        return view;
    }

    /**
     * 保存企业开户
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute ReconciliationPeriodValidator reconciliationPeriodValidator, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("reconciliation/account/reconciliation_account_edit");
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        reconciliationPeriodValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
        reconciliationPeriodValidator.setCreatedBy(user.getRealName());
        reconciliationPeriodValidator.setAccountPeriodType("1");
        reconciliationPeriodValidator.setAccountPeriodStatus("1");
        reconciliationPeriodValidator.setBusinessType("ACCOUNT");
        reconciliationPeriodValidator.setStatus("1");

        //保存数据
        ResponseData data = reconciliationPeriodService.buildAccountPeriod(reconciliationPeriodValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/reconciliation/account/list", true, false));
        return view;

    }

    /**
     * 出账
     *
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable String id) {

        ModelAndView view = new ModelAndView("reconciliation/account/reconciliation_account_list");

        ResponseData delete = reconciliationPeriodService.deleteAccountPeriod(id);
        if (!ResponseCode.SUCCESS.getCode().equals(delete.getCode())) {
            view.addObject("error", delete.getCode() + ":" + delete.getMessage());
            return view;
        }

        view.setView(new RedirectView("/reconciliation/account/list", true, false));
        return view;
    }



}
