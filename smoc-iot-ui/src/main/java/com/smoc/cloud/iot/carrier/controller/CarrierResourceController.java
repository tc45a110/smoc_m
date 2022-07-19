package com.smoc.cloud.iot.carrier.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.iot.validator.IotCarrierInfoValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBasicInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
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

/**
 * 运营商资源接入
 */
@Slf4j
@Controller
@RequestMapping("/iot/carrier")
public class CarrierResourceController {

    @Autowired
    private IotCarrierInfoService iotCarrierInfoService;


    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView view = new ModelAndView("carrier/carrier_list");

        //初始化数据
        PageParams<IotCarrierInfoValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        IotCarrierInfoValidator validator = new IotCarrierInfoValidator();
        params.setParams(validator);

        //查询
        ResponseData<PageList<IotCarrierInfoValidator>> data = iotCarrierInfoService.page(params);
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
    public ModelAndView page(@ModelAttribute IotCarrierInfoValidator validator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("carrier/carrier_list");

        //分页查询
        pageParams.setParams(validator);

        ResponseData<PageList<IotCarrierInfoValidator>> data = iotCarrierInfoService.page(pageParams);
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
        ModelAndView view = new ModelAndView("carrier/carrier_edit");
        IotCarrierInfoValidator validator = new IotCarrierInfoValidator();
        validator.setId(UUID.uuid32());
        validator.setCarrierStatus("1");
        validator.setApiType("1");
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
        ModelAndView view = new ModelAndView("carrier/carrier_edit");
        ResponseData<IotCarrierInfoValidator> validatorData = iotCarrierInfoService.findById(id);
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
    public ModelAndView save(@ModelAttribute @Validated IotCarrierInfoValidator iotCarrierInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("carrier/carrier_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("validator", iotCarrierInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            iotCarrierInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            iotCarrierInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            iotCarrierInfoValidator.setUpdatedTime(new Date());
            iotCarrierInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }
        iotCarrierInfoValidator.setCarrierIdentifying(iotCarrierInfoValidator.getCarrierUsername().toUpperCase());

        //保存数据
        ResponseData data = iotCarrierInfoService.save(iotCarrierInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync("CARRIER", iotCarrierInfoValidator.getId(), "add".equals(op) ? iotCarrierInfoValidator.getCreatedBy() : iotCarrierInfoValidator.getUpdatedBy(), op, "add".equals(op) ? "运营商接入" : "修改运营商接入", JSON.toJSONString(iotCarrierInfoValidator));
        }

        //记录日志
        log.info("[运营商接入][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(iotCarrierInfoValidator));

        view.setView(new RedirectView("/iot/carrier/list", true, false));
        return view;

    }


}
