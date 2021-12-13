package com.smoc.cloud.admin.controller;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.service.SystemExtendBusinessParamService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.SystemExtendBusinessParamValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 业务扩展参数
 **/
@Slf4j
@Controller
@RequestMapping("/param")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class SystemExtendBusinessParamController {

    @Autowired
    private SystemExtendBusinessParamService systemExtendBusinessParamService;


    /**
     * 数据列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{businessType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType) {

        ModelAndView view = new ModelAndView("param/extend_business_param_list");

        if (StringUtils.isEmpty(businessType)) {
            view.addObject("error", "关键系统参数错误");
            return view;
        }

        //数据查询
        ResponseData<List<SystemExtendBusinessParamValidator>> data = systemExtendBusinessParamService.list(businessType);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }
        view.addObject("businessType", businessType);
        view.addObject("list", data.getData());

        return view;
    }

    /**
     * 进入添加页面
     *
     * @return
     */
    @RequestMapping(value = "/add/{businessType}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String businessType) {

        ModelAndView view = new ModelAndView("param/extend_business_param_edit");

        if (StringUtils.isEmpty(businessType)) {
            view.addObject("error", "关键系统参数错误");
            return view;
        }

        //初始化参数
        SystemExtendBusinessParamValidator systemExtendBusinessParamValidator = new SystemExtendBusinessParamValidator();

        systemExtendBusinessParamValidator.setId(UUID.uuid32());
        systemExtendBusinessParamValidator.setBusinessType(businessType);
        systemExtendBusinessParamValidator.setShowType("text");
        systemExtendBusinessParamValidator.setParamMaxLength(32);
        systemExtendBusinessParamValidator.setDataType("STRING");
        systemExtendBusinessParamValidator.setIsReadonly("0");
        systemExtendBusinessParamValidator.setIsNull("0");
        systemExtendBusinessParamValidator.setDisplaySort(1000);
        systemExtendBusinessParamValidator.setParamStatus("1");
        systemExtendBusinessParamValidator.setCreatedTime(new Date());

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");
        view.addObject("systemExtendBusinessParamValidator", systemExtendBusinessParamValidator);

        return view;
    }

    /**
     * 进入修改页面
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id) {

        ModelAndView view = new ModelAndView("param/extend_business_param_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询数据
        ResponseData<SystemExtendBusinessParamValidator> data = systemExtendBusinessParamService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "edit");
        view.addObject("systemExtendBusinessParamValidator", data.getData());

        return view;
    }

    /**
     * 添加、修改信息
     *
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated SystemExtendBusinessParamValidator systemExtendBusinessParamValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("param/extend_business_param_edit");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("systemExtendBusinessParamValidator", systemExtendBusinessParamValidator);
            view.addObject("op", op);
            return view;
        }

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            systemExtendBusinessParamValidator.setCreatedTime(new Date());
            systemExtendBusinessParamValidator.setCreatedBy(user.getId());
            systemExtendBusinessParamValidator.setUpdatedTime(new Date());
            systemExtendBusinessParamValidator.setUpdatedBy(user.getId());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            systemExtendBusinessParamValidator.setUpdatedTime(new Date());
            systemExtendBusinessParamValidator.setUpdatedBy(user.getId());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        log.info("[业务扩展参数][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(systemExtendBusinessParamValidator));

        //保存操作
        ResponseData data = systemExtendBusinessParamService.save(systemExtendBusinessParamValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/param/list/" + systemExtendBusinessParamValidator.getBusinessType(), true, false));
        return view;
    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}/{businessType}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, @PathVariable String businessType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("param/extend_business_param_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[业务扩展参数][delete][{}]数据:{}", user.getUserName(), id);
        //删除操作
        ResponseData data = systemExtendBusinessParamService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/param/list/" + businessType, true, false));
        return view;
    }

}
