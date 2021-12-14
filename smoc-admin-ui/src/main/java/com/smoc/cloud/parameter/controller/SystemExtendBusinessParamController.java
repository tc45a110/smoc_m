package com.smoc.cloud.parameter.controller;


import com.smoc.cloud.admin.security.remote.service.SystemExtendBusinessParameterService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.SystemExtendBusinessParamValidator;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.parameter.ParameterExtendFiltersValueValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.parameter.service.ParameterExtendFiltersValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务参数扩展
 **/
@Slf4j
@Controller
@RequestMapping("/parameter")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class SystemExtendBusinessParamController {

    @Autowired
    private SystemExtendBusinessParameterService systemExtendBusinessParameterService;
    @Autowired
    private ParameterExtendFiltersValueService parameterExtendFiltersValueService;

    /**
     * 过滤业务业务参数
     *
     * @param businessType 过滤业务扩展参数类型
     * @return
     */
    @RequestMapping(value = "/filter/list/{businessType}/{businessId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType, @PathVariable String businessId) {

        ModelAndView view = new ModelAndView("parameter/parameter_extend_edit");

        //判断businessType
        if (StringUtils.isEmpty(businessType)) {
            view.addObject("error", "businessType参数不能为空");
            return view;
        }

        //查询业务扩展字段
        ResponseData<List<SystemExtendBusinessParamValidator>> responseData = systemExtendBusinessParameterService.list(businessType);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
            return view;
        }

        //查询业务扩展字段对应的值
        ResponseData<List<ParameterExtendFiltersValueValidator>> parameterValues = parameterExtendFiltersValueService.findParameterValue(businessId);
        if (!ResponseCode.SUCCESS.getCode().equals(parameterValues.getCode())) {
            view.addObject("error", parameterValues.getCode() + ":" + parameterValues.getMessage());
            return view;
        }

        //把参数值列表转成map，这样前台容易根据 paramKey 取值
        Map<String, String> keyValueMap = new HashMap<>();
        if (null != parameterValues.getData() && parameterValues.getData().size() > 0) {
            keyValueMap = parameterValues.getData().stream().collect(Collectors.toMap(ParameterExtendFiltersValueValidator::getParamKey, parameterExtendFiltersValueValidator -> parameterExtendFiltersValueValidator.getParamValue()));
        }

        view.addObject("businessId", businessId);
        view.addObject("businessType", businessType);
        view.addObject("list", responseData.getData());
        view.addObject("keyValueMap", keyValueMap);

        return view;
    }

    /**
     * 保存过滤业务业务参数
     *
     * @param businessType 过滤业务扩展参数类型
     * @return
     */
    @RequestMapping(value = "/filter/save/{businessType}/{businessId}", method = RequestMethod.POST)
    public ModelAndView save(@PathVariable String businessType, @PathVariable String businessId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("parameter/parameter_extend_edit");

        //判断非空
        if (StringUtils.isEmpty(businessType) || StringUtils.isEmpty(businessId)) {
            view.addObject("error", "businessType参数不能为空");
            return view;
        }

        //查询
        ResponseData<List<SystemExtendBusinessParamValidator>> responseData = systemExtendBusinessParameterService.list(businessType);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
            return view;
        }

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        List<ParameterExtendFiltersValueValidator> list = new ArrayList<>();

        for (SystemExtendBusinessParamValidator obj : responseData.getData()) {
            String value = request.getParameter(obj.getParamKey());
            if (!StringUtils.isEmpty(value)) {
                //log.info("提交参数值：{}:{}", obj.getParamKey(), value);
                ParameterExtendFiltersValueValidator parameter = new ParameterExtendFiltersValueValidator();
                parameter.setId(UUID.uuid32());
                parameter.setBusinessType(businessType);
                parameter.setBusinessId(businessId);
                parameter.setParamName(obj.getParamTitle());
                parameter.setParamKey(obj.getParamKey());
                parameter.setParamValue(value);
                parameter.setCreatedBy(user.getId());
                parameter.setCreatedTime(new Date());
                list.add(parameter);
            }
        }

        ResponseData response = parameterExtendFiltersValueService.save(list,businessId,user.getRealName(),businessType);
        if (!ResponseCode.SUCCESS.getCode().equals(response.getCode())) {
            view.addObject("error", response.getCode() + ":" + response.getMessage());
            return view;
        }

        //把参数值列表转成map，这样前台容易根据 paramKey 取值
        Map<String, String> keyValueMap = new HashMap<>();
        if (null != list && list.size() > 0) {
            keyValueMap = list.stream().collect(Collectors.toMap(ParameterExtendFiltersValueValidator::getParamKey, parameterExtendFiltersValueValidator -> parameterExtendFiltersValueValidator.getParamValue()));
        }

        view.addObject("businessId", businessId);
        view.addObject("businessType", businessType);
        view.addObject("list", responseData.getData());
        view.addObject("keyValueMap", keyValueMap);

        return view;
    }

}
