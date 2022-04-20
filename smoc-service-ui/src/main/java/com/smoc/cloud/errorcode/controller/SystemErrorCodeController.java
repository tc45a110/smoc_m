package com.smoc.cloud.errorcode.controller;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.parameter.SystemErrorCodeValidator;
import com.smoc.cloud.errorcode.service.SystemErrorCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 错误码
 */
@Slf4j
@RestController
@RequestMapping("/code")
public class SystemErrorCodeController {

    @Autowired
    private SystemErrorCodeService systemErrorCodeService;


    /**
     * 错误码列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String parentId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("errorcode/error_code_list");

        //初始化数据
        PageParams<SystemErrorCodeValidator> params = new PageParams<SystemErrorCodeValidator>();
        params.setPageSize(5000);
        params.setCurrentPage(1);
        SystemErrorCodeValidator systemErrorCodeValidator = new SystemErrorCodeValidator();
        systemErrorCodeValidator.setCodeType(parentId);
        systemErrorCodeValidator.setFlag("service-ui");
        params.setParams(systemErrorCodeValidator);

        //查询
        ResponseData<PageList<SystemErrorCodeValidator>> data = systemErrorCodeService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemErrorCodeValidator", systemErrorCodeValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("parentId",parentId);

        return view;

    }

}
