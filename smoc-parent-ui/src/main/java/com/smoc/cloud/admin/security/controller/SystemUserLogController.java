package com.smoc.cloud.admin.security.controller;

import com.google.gson.Gson;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.validator.SystemUserLogValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.identification.validator.IdentificationRequestDataValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 操作日志查询
 */
@Slf4j
@RestController
@RequestMapping("user/logs")
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class SystemUserLogController {

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 用户操作日志查询
     *
     * @param moduleId 数据模块的ID
     * @return
     */
    @RequestMapping(value = "/list/{module}/{moduleId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String module,@PathVariable String moduleId) {

        ModelAndView view = new ModelAndView("sys_user_logs/user_logs_list");

        //判断module
        if (StringUtils.isEmpty(moduleId) || StringUtils.isEmpty(module)) {
            view.addObject("error", "module参数不能为空");
            return view;
        }

        SystemUserLogValidator systemUserLogValidator = new SystemUserLogValidator();
        systemUserLogValidator.setModuleId(moduleId);
        systemUserLogValidator.setModule(module);

        //查询数据
        PageParams<SystemUserLogValidator> params = new PageParams<>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        params.setParams(systemUserLogValidator);

        //分页查询
        ResponseData<PageList<SystemUserLogValidator>> responseData = systemUserLogService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
            return view;
        }
        view.addObject("systemUserLogValidator", systemUserLogValidator);
        view.addObject("list", responseData.getData().getList());
        view.addObject("pageParams", responseData.getData().getPageParams());

        return view;
    }

    /**
     * 用户操作日志分页查询
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute SystemUserLogValidator systemUserLogValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("sys_user_logs/user_logs_list");

        //判断module
        if (StringUtils.isEmpty(systemUserLogValidator.getModuleId())) {
            view.addObject("error", "module参数不能为空");
            return view;
        }

        //分页查询
        pageParams.setParams(systemUserLogValidator);
        ResponseData<PageList<SystemUserLogValidator>> responseData = systemUserLogService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(responseData.getCode())) {
            view.addObject("error", responseData.getCode() + ":" + responseData.getMessage());
            return view;
        }
        view.addObject("systemUserLogValidator", systemUserLogValidator);
        view.addObject("list", responseData.getData().getList());
        view.addObject("pageParams", responseData.getData().getPageParams());
        return view;
    }

    /**
     * 查看请求订单数据
     *
     * @return
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("sys_user_logs/user_logs_json_view");

        //修改:查询数据
        ResponseData<SystemUserLogValidator> data = systemUserLogService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }
        log.info("[log]:{}",data.getData().getLogData());
        view.addObject("jsonData",data.getData().getLogData());
        return view;

    }
}
