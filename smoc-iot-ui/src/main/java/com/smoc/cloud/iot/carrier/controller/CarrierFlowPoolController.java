package com.smoc.cloud.iot.carrier.controller;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.iot.validator.IotCarrierFlowPoolValidator;
import com.smoc.cloud.common.iot.validator.IotCarrierInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.iot.carrier.service.IotCarrierFlowPoolService;
import com.smoc.cloud.iot.carrier.service.IotCarrierInfoService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 运营商流量池
 */
@Slf4j
@Controller
@RequestMapping("/iot/carrier/pool")
public class CarrierFlowPoolController {

    @Autowired
    private IotCarrierInfoService iotCarrierInfoService;

    @Autowired
    private IotCarrierFlowPoolService iotCarrierFlowPoolService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("carrier/carrier_pool_list");

        //初始化数据
        PageParams<IotCarrierFlowPoolValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        IotCarrierFlowPoolValidator validator = new IotCarrierFlowPoolValidator();
        params.setParams(validator);

        //查询
        ResponseData<PageList<IotCarrierFlowPoolValidator>> data = iotCarrierFlowPoolService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }


        //初始化数据
        PageParams<IotCarrierInfoValidator> paramsCarrier = new PageParams<>();
        paramsCarrier.setPageSize(100);
        paramsCarrier.setCurrentPage(1);
        IotCarrierInfoValidator validatorCarrier = new IotCarrierInfoValidator();
        paramsCarrier.setParams(validatorCarrier);

        //查询
        ResponseData<PageList<IotCarrierInfoValidator>> dataCarrier = iotCarrierInfoService.page(paramsCarrier);

        Map<String, String> carrierMap = dataCarrier.getData().getList().stream().collect(Collectors.toMap(IotCarrierInfoValidator::getId, IotCarrierInfoValidator::getCarrierName));
        request.getSession().setAttribute("carrierMap", carrierMap);
        view.addObject("validator", validator);
        view.addObject("list", data.getData().getList());
        view.addObject("carriers", dataCarrier.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 列表分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute IotCarrierFlowPoolValidator validator, PageParams pageParams, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("carrier/carrier_pool_list");

        //分页查询
        pageParams.setParams(validator);

        ResponseData<PageList<IotCarrierFlowPoolValidator>> data = iotCarrierFlowPoolService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //初始化数据
        PageParams<IotCarrierInfoValidator> paramsCarrier = new PageParams<>();
        paramsCarrier.setPageSize(100);
        paramsCarrier.setCurrentPage(1);
        IotCarrierInfoValidator validatorCarrier = new IotCarrierInfoValidator();
        paramsCarrier.setParams(validatorCarrier);

        //查询
        ResponseData<PageList<IotCarrierInfoValidator>> dataCarrier = iotCarrierInfoService.page(paramsCarrier);

        log.info("[dataCarrier]:{}", new Gson().toJson(dataCarrier));
        log.info("[validator]:{}", new Gson().toJson(validator));

        Map<String, String> carrierMap = dataCarrier.getData().getList().stream().collect(Collectors.toMap(IotCarrierInfoValidator::getId, IotCarrierInfoValidator::getCarrierName));
        request.getSession().setAttribute("carrierMap", carrierMap);
        view.addObject("validator", validator);
        view.addObject("list", data.getData().getList());
        view.addObject("carriers", dataCarrier.getData().getList());
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
        ModelAndView view = new ModelAndView("carrier/carrier_pool_edit");

        //初始化数据
        PageParams<IotCarrierInfoValidator> paramsCarrier = new PageParams<>();
        paramsCarrier.setPageSize(100);
        paramsCarrier.setCurrentPage(1);
        IotCarrierInfoValidator validatorCarrier = new IotCarrierInfoValidator();
        paramsCarrier.setParams(validatorCarrier);
        //查询
        ResponseData<PageList<IotCarrierInfoValidator>> dataCarrier = iotCarrierInfoService.page(paramsCarrier);
        if (!ResponseCode.SUCCESS.getCode().equals(dataCarrier.getCode())) {
            view.addObject("error", dataCarrier.getCode() + ":" + dataCarrier.getMessage());
            return view;
        }

        IotCarrierFlowPoolValidator validator = new IotCarrierFlowPoolValidator();
        validator.setId(UUID.uuid32());

        view.addObject("carriers", dataCarrier.getData().getList());
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
        ModelAndView view = new ModelAndView("carrier/carrier_pool_edit");


        //初始化数据
        PageParams<IotCarrierInfoValidator> paramsCarrier = new PageParams<>();
        paramsCarrier.setPageSize(100);
        paramsCarrier.setCurrentPage(1);
        IotCarrierInfoValidator validatorCarrier = new IotCarrierInfoValidator();
        paramsCarrier.setParams(validatorCarrier);
        //查询
        ResponseData<PageList<IotCarrierInfoValidator>> dataCarrier = iotCarrierInfoService.page(paramsCarrier);
        if (!ResponseCode.SUCCESS.getCode().equals(dataCarrier.getCode())) {
            view.addObject("error", dataCarrier.getCode() + ":" + dataCarrier.getMessage());
            return view;
        }

        ResponseData<IotCarrierFlowPoolValidator> validatorData = iotCarrierFlowPoolService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(validatorData.getCode())) {
            view.addObject("error", validatorData.getCode() + ":" + validatorData.getMessage());
            return view;
        }

        view.addObject("carriers", dataCarrier.getData().getList());
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
    public ModelAndView save(@ModelAttribute @Validated IotCarrierFlowPoolValidator iotCarrierFlowPoolValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("carrier/carrier_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("validator", iotCarrierFlowPoolValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            iotCarrierFlowPoolValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            iotCarrierFlowPoolValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            iotCarrierFlowPoolValidator.setUpdatedTime(new Date());
            iotCarrierFlowPoolValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        ResponseData data = iotCarrierFlowPoolService.save(iotCarrierFlowPoolValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CARRIER_POOL", iotCarrierFlowPoolValidator.getId(), "add".equals(op) ? iotCarrierFlowPoolValidator.getCreatedBy() : iotCarrierFlowPoolValidator.getUpdatedBy(), op, "add".equals(op) ? "添加运营流量池" : "修改运营流量池", JSON.toJSONString(iotCarrierFlowPoolValidator));
        }

        //记录日志
        log.info("[运营商流量池][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(iotCarrierFlowPoolValidator));

        view.setView(new RedirectView("/iot/carrier/pool/list", true, false));
        return view;

    }

}
