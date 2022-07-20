package com.smoc.cloud.iot.account.controller;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.iot.validator.*;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.system.SystemAccountInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.iot.account.service.IotUserProductInfoService;
import com.smoc.cloud.iot.product.service.IotProductInfoService;
import com.smoc.cloud.iot.system.service.EnterpriseService;
import com.smoc.cloud.iot.system.service.FinanceAccountService;
import com.smoc.cloud.iot.system.service.SystemAccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/iot/account/product")
public class AccountProductController {

    @Autowired
    private IotUserProductInfoService iotUserProductInfoService;

    @Autowired
    private IotProductInfoService iotProductInfoService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private SystemAccountInfoService systemAccountInfoService;

    private String businessType = "IOT_ACCOUNT";

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("account/account_product_list");

        //初始化数据
        PageParams<SystemAccountInfoValidator> params = new PageParams<SystemAccountInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        SystemAccountInfoValidator systemAccountInfoValidator = new SystemAccountInfoValidator();
        systemAccountInfoValidator.setBusinessType(this.businessType);
        params.setParams(systemAccountInfoValidator);

        //查询
        ResponseData<PageList<SystemAccountInfoValidator>> data = systemAccountInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemAccountInfoValidator", systemAccountInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }


    /**
     * 列表分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute SystemAccountInfoValidator systemAccountInfoValidator, PageParams pageParams, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("account/account_product_list");

        //分页查询
        systemAccountInfoValidator.setBusinessType(this.businessType);
        pageParams.setParams(systemAccountInfoValidator);

        ResponseData<PageList<SystemAccountInfoValidator>> data = systemAccountInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemAccountInfoValidator", systemAccountInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;
    }

    /**
     * 新建
     *
     * @return
     */
    @RequestMapping(value = "/edit/{account}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String account) {

        ModelAndView view = new ModelAndView("account/account_product_edit");

        //查询
        ResponseData<List<IotProductInfoValidator>> productData = iotUserProductInfoService.list(account);
        if (!ResponseCode.SUCCESS.getCode().equals(productData.getCode())) {
            view.addObject("error", productData.getCode() + ":" + productData.getMessage());
            return view;
        }

        AccountProduct accountProduct = new AccountProduct();
        accountProduct.setAccount(account);
        view.addObject("products", productData.getData());
        view.addObject("accountProduct", accountProduct);
        return view;
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated AccountProduct accountProduct, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("account/account_product_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");


        //保存数据
        ResponseData data = iotUserProductInfoService.save(accountProduct);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("ACCOUNT_PRODUCT", accountProduct.getAccount(), user.getRealName(), "config", "用户产品配置", JSON.toJSONString(accountProduct));
        }

        //记录日志
        log.info("[用户产品配置][{}]数据:{}", user.getUserName(), JSON.toJSONString(accountProduct));

        view.setView(new RedirectView("/iot/account/product/list", true, false));
        return view;

    }


    /**
     * @return
     */
    @RequestMapping(value = "/center", method = RequestMethod.GET)
    public ModelAndView center() {
        ModelAndView view = new ModelAndView("account/account_product_view_center");
        return view;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView view() {
        ModelAndView view = new ModelAndView("account/account_product_view");
        return view;
    }
}
