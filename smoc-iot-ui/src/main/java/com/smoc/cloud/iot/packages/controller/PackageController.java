package com.smoc.cloud.iot.packages.controller;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.iot.validator.IotCarrierInfoValidator;
import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.iot.validator.IotPackageInfoValidator;
import com.smoc.cloud.common.iot.validator.PackageCards;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.iot.carrier.service.IotCarrierInfoService;
import com.smoc.cloud.iot.carrier.service.IotFlowCardsPrimaryInfoService;
import com.smoc.cloud.iot.packages.service.IotPackageInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 物联网卡套餐管理
 */
@Slf4j
@Controller
@RequestMapping("/iot/package")
public class PackageController {

    @Autowired
    private IotCarrierInfoService iotCarrierInfoService;

    @Autowired
    private IotPackageInfoService iotPackageInfoService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private IotFlowCardsPrimaryInfoService iotFlowCardsPrimaryInfoService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("package/package_list");

        //初始化数据
        PageParams<IotPackageInfoValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        IotPackageInfoValidator validator = new IotPackageInfoValidator();
        params.setParams(validator);

        //查询
        ResponseData<PageList<IotPackageInfoValidator>> data = iotPackageInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("validator", validator);
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
    public ModelAndView page(@ModelAttribute IotPackageInfoValidator validator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("package/package_list");

        //分页查询
        pageParams.setParams(validator);

        ResponseData<PageList<IotPackageInfoValidator>> data = iotPackageInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("validator", validator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 新建
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add() {
        ModelAndView view = new ModelAndView("package/package_edit");
        IotPackageInfoValidator validator = new IotPackageInfoValidator();
        validator.setId(UUID.uuid32());
        validator.setPackageStatus("1");
        validator.setUseStatus("01");
        validator.setAboveQuotaChanging(new BigDecimal("0"));
        validator.setPackageCardsNum(0);
        validator.setWarningLevel(90);
        validator.setPackageTempAmount(new BigDecimal("0"));
        validator.setCycleFunctionFee(new BigDecimal("0"));
        validator.setPackageTempAmountFee(new BigDecimal("0"));
        validator.setLastMonthCarryAmount(new BigDecimal("0"));
        validator.setThisMonthUsedAmount(new BigDecimal("0"));
        view.addObject("validator", validator);
        view.addObject("op", "add");

        return view;
    }


    /**
     * 修改
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id) {
        ModelAndView view = new ModelAndView("package/package_edit");
        ResponseData<IotPackageInfoValidator> validatorData = iotPackageInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(validatorData.getCode())) {
            view.addObject("error", validatorData.getCode() + ":" + validatorData.getMessage());
            return view;
        }
        view.addObject("validator", validatorData.getData());
        view.addObject("op", "edit");
        return view;
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated IotPackageInfoValidator iotPackageInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("package/package_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("validator", iotPackageInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            iotPackageInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            iotPackageInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            iotPackageInfoValidator.setUpdatedTime(new Date());
            iotPackageInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //下面字段给定初始值
        if (null == iotPackageInfoValidator.getPackageTempAmount()) {
            iotPackageInfoValidator.setPackageTempAmount(new BigDecimal("0"));
        }
        if (null == iotPackageInfoValidator.getCycleFunctionFee()) {
            iotPackageInfoValidator.setCycleFunctionFee(new BigDecimal("0"));
        }
        if (null == iotPackageInfoValidator.getPackageTempAmountFee()) {
            iotPackageInfoValidator.setPackageTempAmountFee(new BigDecimal("0"));
        }

        //保存数据
        ResponseData data = iotPackageInfoService.save(iotPackageInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("Package", iotPackageInfoValidator.getId(), "add".equals(op) ? iotPackageInfoValidator.getCreatedBy() : iotPackageInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "新建物联网套餐" : "修改运物联网套餐", JSON.toJSONString(iotPackageInfoValidator));
        }

        //记录日志
        log.info("[物联网套餐][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(iotPackageInfoValidator));

        view.setView(new RedirectView("/iot/package/list", true, false));
        return view;

    }

    /**
     * @param id          套餐id
     * @return
     */
    @RequestMapping(value = "/config/edit/{id}", method = RequestMethod.GET)
    public ModelAndView config(@PathVariable String id) {
        ModelAndView view = new ModelAndView("package/package_config");

        //初始化数据
        PageParams<IotFlowCardsPrimaryInfoValidator> params = new PageParams<>();
        params.setPageSize(100);
        params.setCurrentPage(1);
        IotFlowCardsPrimaryInfoValidator validator = new IotFlowCardsPrimaryInfoValidator();
        params.setParams(validator);
        //查询
        ResponseData<List<IotFlowCardsPrimaryInfoValidator>> data = iotPackageInfoService.list(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        ResponseData<IotPackageInfoValidator> validatorData = iotPackageInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(validatorData.getCode())) {
            view.addObject("error", validatorData.getCode() + ":" + validatorData.getMessage());
            return view;
        }

        //初始化数据
        PageParams<IotCarrierInfoValidator> paramsCarrier = new PageParams<>();
        paramsCarrier.setPageSize(100);
        paramsCarrier.setCurrentPage(1);
        IotCarrierInfoValidator validatorCarrier = new IotCarrierInfoValidator();
        paramsCarrier.setParams(validatorCarrier);
        ResponseData<PageList<IotCarrierInfoValidator>> dataCarrier = iotCarrierInfoService.page(paramsCarrier);
        Map<String, String> carrierMap = dataCarrier.getData().getList().stream().collect(Collectors.toMap(IotCarrierInfoValidator::getId, IotCarrierInfoValidator::getCarrierName));


        PackageCards PackageCards = new PackageCards();

        log.info("[Package]:{}", new Gson().toJson(validatorData.getData()));
        view.addObject("carrierMap", carrierMap);
        view.addObject("package", validatorData.getData());
        view.addObject("cards", data.getData());
        view.addObject("packageCards", PackageCards);
        return view;
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/config/save", method = RequestMethod.POST)
    public ModelAndView config_save(@ModelAttribute @Validated PackageCards PackageCards, BindingResult result, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("package/package_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //保存数据
        ResponseData data = iotPackageInfoService.saveConfig(PackageCards);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CARRIER", PackageCards.getPackageId(), user.getRealName(), "config", "套餐物联网卡配置", JSON.toJSONString(PackageCards));
        }

        //记录日志
        log.info("[套餐物联网卡配置][{}][{}]数据:{}", "config", user.getUserName(), JSON.toJSONString(PackageCards));

        view.setView(new RedirectView("/iot/package/list", true, false));
        return view;

    }

    /**
     * @return
     */
    @RequestMapping(value = "/center", method = RequestMethod.GET)
    public ModelAndView center() {
        ModelAndView view = new ModelAndView("package/package_view_center");
        return view;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView view() {
        ModelAndView view = new ModelAndView("package/package_view");
        return view;
    }

}
