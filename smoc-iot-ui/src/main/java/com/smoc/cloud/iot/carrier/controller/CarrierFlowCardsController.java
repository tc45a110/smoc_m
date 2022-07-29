package com.smoc.cloud.iot.carrier.controller;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.iot.validator.IotCarrierFlowPoolValidator;
import com.smoc.cloud.common.iot.validator.IotCarrierInfoValidator;
import com.smoc.cloud.common.iot.validator.IotFlowCardsInfo;
import com.smoc.cloud.common.iot.validator.IotFlowCardsPrimaryInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.utils.Utils;
import com.smoc.cloud.iot.carrier.service.IotCarrierFlowPoolService;
import com.smoc.cloud.iot.carrier.service.IotCarrierInfoService;
import com.smoc.cloud.iot.carrier.service.IotFlowCardsPrimaryInfoService;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 运营商物联网卡
 */
@Slf4j
@Controller
@RequestMapping("/iot/carrier/cards")
public class CarrierFlowCardsController {

    @Autowired
    private IotCarrierInfoService iotCarrierInfoService;

    @Autowired
    private SystemUserLogService systemUserLogService;

    @Autowired
    private IotCarrierFlowPoolService iotCarrierFlowPoolService;

    @Autowired
    private IotFlowCardsPrimaryInfoService iotFlowCardsPrimaryInfoService;


    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("carrier/carrier_flow_cards_list");

        //初始化数据
        PageParams<IotFlowCardsPrimaryInfoValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        IotFlowCardsPrimaryInfoValidator validator = new IotFlowCardsPrimaryInfoValidator();

        Date startDate = DateTimeUtils.getFirstMonth(18);
        validator.setStartDateTime(DateTimeUtils.getDateFormat(startDate));
        validator.setEndDateTime(DateTimeUtils.getDateFormat(new Date()));

        params.setParams(validator);

        //查询
        ResponseData<PageList<IotFlowCardsPrimaryInfoValidator>> data = iotFlowCardsPrimaryInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //初始化数据
        PageParams<IotCarrierFlowPoolValidator> paramsPoos = new PageParams<>();
        paramsPoos.setPageSize(100);
        paramsPoos.setCurrentPage(1);
        IotCarrierFlowPoolValidator validatorPool = new IotCarrierFlowPoolValidator();
        paramsPoos.setParams(validatorPool);
        //查询
        ResponseData<PageList<IotCarrierFlowPoolValidator>> dataPool = iotCarrierFlowPoolService.page(paramsPoos);

        //初始化数据
        PageParams<IotCarrierInfoValidator> paramsCarrier = new PageParams<>();
        paramsCarrier.setPageSize(100);
        paramsCarrier.setCurrentPage(1);
        IotCarrierInfoValidator validatorCarrier = new IotCarrierInfoValidator();
        paramsCarrier.setParams(validatorCarrier);
        //查询
        ResponseData<PageList<IotCarrierInfoValidator>> dataCarrier = iotCarrierInfoService.page(paramsCarrier);

        Map<String, String> poolMap = dataPool.getData().getList().stream().collect(Collectors.toMap(IotCarrierFlowPoolValidator::getId, IotCarrierFlowPoolValidator::getPoolName));
        view.addObject("poolMap", poolMap);
        view.addObject("validator", validator);
        view.addObject("list", data.getData().getList());
        view.addObject("carriers", dataCarrier.getData().getList());
        view.addObject("pools", dataPool.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * 列表分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute IotFlowCardsPrimaryInfoValidator validator, PageParams pageParams, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("carrier/carrier_flow_cards_list");

        String createTime = validator.getCreatedTime();
        String[] date = createTime.split(" - ");
        validator.setStartDateTime(date[0]);
        validator.setEndDateTime(date[1]);
        //分页查询
        pageParams.setParams(validator);

        ResponseData<PageList<IotFlowCardsPrimaryInfoValidator>> data = iotFlowCardsPrimaryInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //初始化数据
        PageParams<IotCarrierFlowPoolValidator> paramsPoos = new PageParams<>();
        paramsPoos.setPageSize(100);
        paramsPoos.setCurrentPage(1);
        IotCarrierFlowPoolValidator validatorPool = new IotCarrierFlowPoolValidator();
        paramsPoos.setParams(validatorPool);
        //查询
        ResponseData<PageList<IotCarrierFlowPoolValidator>> dataPool = iotCarrierFlowPoolService.page(paramsPoos);

        //初始化数据
        PageParams<IotCarrierInfoValidator> paramsCarrier = new PageParams<>();
        paramsCarrier.setPageSize(100);
        paramsCarrier.setCurrentPage(1);
        IotCarrierInfoValidator validatorCarrier = new IotCarrierInfoValidator();
        paramsCarrier.setParams(validatorCarrier);
        //查询
        ResponseData<PageList<IotCarrierInfoValidator>> dataCarrier = iotCarrierInfoService.page(paramsCarrier);

        Map<String, String> poolMap = dataPool.getData().getList().stream().collect(Collectors.toMap(IotCarrierFlowPoolValidator::getId, IotCarrierFlowPoolValidator::getPoolName));
        view.addObject("poolMap", poolMap);
        view.addObject("validator", validator);
        view.addObject("list", data.getData().getList());
        view.addObject("carriers", dataCarrier.getData().getList());
        view.addObject("pools", dataPool.getData().getList());
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
        ModelAndView view = new ModelAndView("carrier/carrier_flow_cards_edit");

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

        //初始化数据
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


        IotFlowCardsPrimaryInfoValidator validator = new IotFlowCardsPrimaryInfoValidator();
        validator.setId(UUID.uuid32());
        validator.setCardStatus("0");
        validator.setUseStatus("0");
        validator.setOpenCardFee(new BigDecimal("0"));
        validator.setCycleFunctionFee(new BigDecimal("0"));

        view.addObject("pools", dataPool.getData().getList());
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
        ModelAndView view = new ModelAndView("carrier/carrier_flow_cards_edit");

        //初始化运营商数据
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

        //初始化流量池数据
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

        ResponseData<IotFlowCardsInfo> infoResponseData = iotFlowCardsPrimaryInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoResponseData.getCode())) {
            view.addObject("error", infoResponseData.getCode() + ":" + infoResponseData.getMessage());
            return view;
        }
        //log.info("[validatorMap]:{}", new Gson().toJson(infoResponseData));
        IotFlowCardsPrimaryInfoValidator iotFlowCardsPrimaryInfoValidator = infoResponseData.getData().getIotFlowCardsPrimaryInfoValidator();
        view.addObject("pools", dataPool.getData().getList());
        view.addObject("carriers", dataCarrier.getData().getList());
        view.addObject("validator", iotFlowCardsPrimaryInfoValidator);
        view.addObject("op", "edit");
        return view;
    }

    /**
     * 保存
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated IotFlowCardsPrimaryInfoValidator iotFlowCardsPrimaryInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("carrier/carrier_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("validator", iotFlowCardsPrimaryInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            iotFlowCardsPrimaryInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            iotFlowCardsPrimaryInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            iotFlowCardsPrimaryInfoValidator.setUpdatedTime(new Date());
            iotFlowCardsPrimaryInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        if (StringUtils.isEmpty(iotFlowCardsPrimaryInfoValidator.getOrderNum())) {
            iotFlowCardsPrimaryInfoValidator.setOrderNum(DateTimeUtils.getDateFormat(new Date(), "yyyyMMddHHmmssSSS") + Utils.getRandom(4));
        }

        //根据流量类型，映射计费类型
        if ("01".equals(iotFlowCardsPrimaryInfoValidator.getCardType())) {
            iotFlowCardsPrimaryInfoValidator.setChargeType("01");
        } else {
            iotFlowCardsPrimaryInfoValidator.setChargeType("02");
        }

        //保存数据
        ResponseData data = iotFlowCardsPrimaryInfoService.save(iotFlowCardsPrimaryInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CARRIER_CARDS", iotFlowCardsPrimaryInfoValidator.getId(), "add".equals(op) ? iotFlowCardsPrimaryInfoValidator.getCreatedBy() : iotFlowCardsPrimaryInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "添加运营商物联网卡" : "修改运营商物联网卡", JSON.toJSONString(iotFlowCardsPrimaryInfoValidator));
        }

        //记录日志
        log.info("[运营商物联网卡][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(iotFlowCardsPrimaryInfoValidator));

        view.setView(new RedirectView("/iot/carrier/cards/list", true, false));
        return view;

    }

    /**
     * @return
     */
    @RequestMapping(value = "/center/{cardId}", method = RequestMethod.GET)
    public ModelAndView center(@PathVariable String cardId) {
        ModelAndView view = new ModelAndView("carrier/carrier_flow_cards_view_center");

        view.addObject("cardId", cardId);
        return view;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/view/{cardId}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String cardId) {
        ModelAndView view = new ModelAndView("carrier/carrier_flow_cards_view");
        //初始化运营商数据
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

        Map<String, String> carrierMap = dataCarrier.getData().getList().stream().collect(Collectors.toMap(IotCarrierInfoValidator::getId, IotCarrierInfoValidator::getCarrierName));


        //初始化流量池数据
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

        ResponseData<IotFlowCardsInfo> infoResponseData = iotFlowCardsPrimaryInfoService.findById(cardId);
        if (!ResponseCode.SUCCESS.getCode().equals(infoResponseData.getCode())) {
            view.addObject("error", infoResponseData.getCode() + ":" + infoResponseData.getMessage());
            return view;
        }
        Map<String, String> poolMap = dataPool.getData().getList().stream().collect(Collectors.toMap(IotCarrierFlowPoolValidator::getId, IotCarrierFlowPoolValidator::getPoolName));

        view.addObject("poolMap", poolMap);
        view.addObject("carrierMap", carrierMap);
        view.addObject("primary", infoResponseData.getData().getIotFlowCardsPrimaryInfoValidator());
        view.addObject("secondary", infoResponseData.getData().getIotFlowCardsSecondaryInfoValidator());
        return view;
    }
}
