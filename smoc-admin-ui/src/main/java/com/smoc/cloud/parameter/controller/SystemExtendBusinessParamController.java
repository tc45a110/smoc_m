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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 过滤参数扩展
 **/
@Slf4j
@RestController
@RequestMapping("/parameter/filter")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class SystemExtendBusinessParamController {

    @Autowired
    private SystemExtendBusinessParameterService systemExtendBusinessParameterService;
    @Autowired
    private ParameterExtendFiltersValueService parameterExtendFiltersValueService;

    /**
     * 过滤业务业务参数 编辑页
     * @param businessType 参数类型
     * @param businessId 业务ID
     * @param columns 每列要显示的 列宽  建议值3、4、6、12
     * @return
     */
    @RequestMapping(value = "/list/{businessType}/{businessId}/{columns}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType, @PathVariable String businessId,@PathVariable String columns) {

        ModelAndView view = new ModelAndView("parameter/parameter_extend_filter_edit");

        //判断非空
        if (StringUtils.isEmpty(businessType) || StringUtils.isEmpty(businessId) || StringUtils.isEmpty(columns)) {
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
        view.addObject("columns", columns);
        view.addObject("list", responseData.getData());
        view.addObject("keyValueMap", keyValueMap);

        return view;
    }

    /**
     * 保存过滤业务业务参数  保存
     * @param businessType 参数类型
     * @param businessId 业务ID
     * @param columns 每列要显示的 列宽  建议值3、4、6、12
     * @param request
     * @return
     */
    @RequestMapping(value = "/save/{businessType}/{businessId}/{columns}", method = RequestMethod.POST)
    public ModelAndView save(@PathVariable String businessType, @PathVariable String businessId,@PathVariable String columns, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("parameter/parameter_extend_filter_edit");

        //判断非空
        if (StringUtils.isEmpty(businessType) || StringUtils.isEmpty(businessId) || StringUtils.isEmpty(columns)) {
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

        //获取页面传过来的值，并进行封装
        List<ParameterExtendFiltersValueValidator> list = new ArrayList<>();
        for (SystemExtendBusinessParamValidator obj : responseData.getData()) {
            String showType = obj.getShowType();
            String value = "";
            if("checkbox".equals(showType)){
                String[] values = request.getParameterValues(obj.getParamKey());
                if(!StringUtils.isEmpty(values) && values.length>0){
                    for(String v : values){
                        if(StringUtils.isEmpty(value)){
                            value = v;
                        }else{
                            value += "," +v;
                        }
                    }
                }
            }else{
                value = request.getParameter(obj.getParamKey());
            }

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

        //保存操作
        ResponseData response = parameterExtendFiltersValueService.save(list,businessId,user.getRealName(),businessType);
        if (!ResponseCode.SUCCESS.getCode().equals(response.getCode())) {
            view.addObject("error", response.getCode() + ":" + response.getMessage());
            return view;
        }

        //把参数值列表转成map，进行回显，这样前台容易根据 paramKey 取值
        Map<String, String> keyValueMap = new HashMap<>();
        if (null != list && list.size() > 0) {
            keyValueMap = list.stream().collect(Collectors.toMap(ParameterExtendFiltersValueValidator::getParamKey, parameterExtendFiltersValueValidator -> parameterExtendFiltersValueValidator.getParamValue()));
        }

        view.addObject("businessId", businessId);
        view.addObject("businessType", businessType);
        view.addObject("columns", columns);
        view.addObject("list", responseData.getData());
        view.addObject("keyValueMap", keyValueMap);

        return view;
    }

    /**
     * 过滤业务业务参数 查看
     * @param businessType 参数类型
     * @param businessId 业务ID
     * @param columns 每列要显示的 列宽  建议值3、4、6、12
     * @return
     */
    @RequestMapping(value = "/view/{businessType}/{businessId}/{columns}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable String businessType, @PathVariable String businessId,@PathVariable String columns) {

        ModelAndView view = new ModelAndView("parameter/parameter_extend_filter_view");

        //判断非空
        if (StringUtils.isEmpty(businessType) || StringUtils.isEmpty(businessId) || StringUtils.isEmpty(columns)) {
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
        view.addObject("columns", columns);
        view.addObject("list", responseData.getData());
        view.addObject("keyValueMap", keyValueMap);

        return view;
    }

    /**
     * 过滤业务 查看
     *
     * @param businessType 参数类型
     * @param businessId   业务ID
     * @return
     */
    @RequestMapping(value = "/look/{businessType}/{businessId}", produces = "text/html;charset=utf-8", method = RequestMethod.GET)
    public String look(@PathVariable String businessType, @PathVariable String businessId) {

        //判断非空
        if (StringUtils.isEmpty(businessType) || StringUtils.isEmpty(businessId)) {
            return "businessType参数不能为空";
        }

        //查询业务扩展字段对应的值
        ResponseData<List<ParameterExtendFiltersValueValidator>> parameterValues = parameterExtendFiltersValueService.findParameterValue(businessId);
        if (!ResponseCode.SUCCESS.getCode().equals(parameterValues.getCode())) {
            return parameterValues.getMessage();
        }

        List<ParameterExtendFiltersValueValidator> list = parameterValues.getData();
        if (StringUtils.isEmpty(list) || list.size() <= 0) {
            return "无过滤参数";
        }

        //封装过滤参数数据
        StringBuilder extendBusinessParam = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            ParameterExtendFiltersValueValidator qo = list.get(i);
            extendBusinessParam.append(qo.getParamName() + "：" + qo.getParamValue()+"；");
            if (i != list.size()-1) {
                extendBusinessParam.append("@");
            }
        }

        return extendBusinessParam.toString();
    }
}
