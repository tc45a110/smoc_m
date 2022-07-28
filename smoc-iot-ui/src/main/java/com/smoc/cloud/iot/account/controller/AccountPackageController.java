package com.smoc.cloud.iot.account.controller;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.iot.validator.*;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.system.SystemAccountInfoValidator;
import com.smoc.cloud.iot.account.service.IotAccountPackageInfoService;
import com.smoc.cloud.iot.carrier.service.IotCarrierFlowPoolService;
import com.smoc.cloud.iot.packages.service.IotPackageInfoService;
import com.smoc.cloud.iot.system.service.SystemAccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/iot/account/package")
public class AccountPackageController {

    @Autowired
    private IotAccountPackageInfoService iotAccountPackageInfoService;

    @Autowired
    private IotPackageInfoService iotPackageInfoService;

    @Autowired
    private IotCarrierFlowPoolService iotCarrierFlowPoolService;

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
        ModelAndView view = new ModelAndView("account/account_package_list");

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

        //查询账号配置的套餐信息
        Map<String, List> packagesMap = new HashMap<>();
        if (null != data.getData() && null != data.getData().getList() && data.getData().getList().size() > 0) {
            for (SystemAccountInfoValidator accountInfoValidator : data.getData().getList()) {
                String account = accountInfoValidator.getAccount();
                ResponseData<List<IotPackageInfoValidator>> listData = iotAccountPackageInfoService.listAccountPackages(account);
                packagesMap.put(account, listData.getData());
            }
        }

        view.addObject("packagesMap", packagesMap);
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
        ModelAndView view = new ModelAndView("account/account_package_list");

        //分页查询
        systemAccountInfoValidator.setBusinessType(this.businessType);
        pageParams.setParams(systemAccountInfoValidator);

        ResponseData<PageList<SystemAccountInfoValidator>> data = systemAccountInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询账号配置的套餐信息
        Map<String, List> packagesMap = new HashMap<>();
        if (null != data.getData() && null != data.getData().getList() && data.getData().getList().size() > 0) {
            for (SystemAccountInfoValidator accountInfoValidator : data.getData().getList()) {
                String account = accountInfoValidator.getAccount();
                ResponseData<List<IotPackageInfoValidator>> listData = iotAccountPackageInfoService.listAccountPackages(account);
                packagesMap.put(account, listData.getData());
            }
        }

        view.addObject("packagesMap", packagesMap);

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

        ModelAndView view = new ModelAndView("account/account_package_edit");

        //查询
        ResponseData<List<IotPackageInfoValidator>> packageData = iotAccountPackageInfoService.list(account);
        if (!ResponseCode.SUCCESS.getCode().equals(packageData.getCode())) {
            view.addObject("error", packageData.getCode() + ":" + packageData.getMessage());
            return view;
        }

        AccountPackage accountPackage = new AccountPackage();
        accountPackage.setAccount(account);
        view.addObject("packages", packageData.getData());
        view.addObject("accountPackage", accountPackage);
        return view;
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated AccountPackage accountPackage, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("account/account_package_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //保存数据
        ResponseData data = iotAccountPackageInfoService.save(accountPackage);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("ACCOUNT_PACKAGE", accountPackage.getAccount(), user.getRealName(), "config", "用户套餐配置", JSON.toJSONString(accountPackage));
        }

        //记录日志
        log.info("[用户套餐配置][{}]数据:{}", user.getUserName(), JSON.toJSONString(accountPackage));

        view.setView(new RedirectView("/iot/account/package/list", true, false));
        return view;

    }


    /**
     * @return
     */
    @RequestMapping(value = "/center/{account}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String account) {
        ModelAndView view = new ModelAndView("account/account_package_view_center");

        ResponseData<List<IotPackageInfoValidator>> listData = iotAccountPackageInfoService.listAccountPackages(account);
        if (!ResponseCode.SUCCESS.getCode().equals(listData.getCode())) {
            view.addObject("error", listData.getCode() + ":" + listData.getMessage());
            return view;
        }


        view.addObject("packages", listData.getData());
        view.addObject("account", account);
        return view;
    }

    /**
     * 根据套餐id，查询套餐绑定的物联网卡
     *
     * @param account
     * @param packageId
     * @return
     */
    @RequestMapping(value = "/view/{account}/{packageId}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String account, @PathVariable String packageId) {
        ModelAndView view = new ModelAndView("account/account_package_view");


        //根据套餐id，查询套餐绑定的物联网卡
        ResponseData<List<IotFlowCardsPrimaryInfoValidator>> listData = iotAccountPackageInfoService.listCardsByPackageId(account,packageId);
        if (!ResponseCode.SUCCESS.getCode().equals(listData.getCode())) {
            view.addObject("error", listData.getCode() + ":" + listData.getMessage());
            return view;
        }


        //初始化运营商流量池数据
        PageParams<IotCarrierFlowPoolValidator> paramsPoos = new PageParams<>();
        paramsPoos.setPageSize(100);
        paramsPoos.setCurrentPage(1);
        IotCarrierFlowPoolValidator validatorPool = new IotCarrierFlowPoolValidator();
        paramsPoos.setParams(validatorPool);
        //查询
        ResponseData<PageList<IotCarrierFlowPoolValidator>> dataPool = iotCarrierFlowPoolService.page(paramsPoos);
        if (!ResponseCode.SUCCESS.getCode().equals(dataPool.getCode())) {
            view.addObject("error", dataPool.getCode() + ":" + dataPool.getMessage());
            return view;
        }
        Map<String, String> poolMap = dataPool.getData().getList().stream().collect(Collectors.toMap(IotCarrierFlowPoolValidator::getId, IotCarrierFlowPoolValidator::getPoolName));
        view.addObject("poolMap", poolMap);

        view.addObject("cards", listData.getData());
        return view;
    }
}
