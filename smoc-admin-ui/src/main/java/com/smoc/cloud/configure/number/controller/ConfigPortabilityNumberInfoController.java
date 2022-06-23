package com.smoc.cloud.configure.number.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigNumberInfoValidator;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.number.service.ConfigNumberInfoService;
import com.smoc.cloud.utils.FileUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 携号转网管理
 **/
@Slf4j
@Controller
@RequestMapping("/configure/number/portability")
public class ConfigPortabilityNumberInfoController {

    @Autowired
    private ConfigNumberInfoService configNumberInfoService;

    private String type = "3";

    /**
     * 携号转网列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/config_number/config_portability_number_list");

        //初始化数据
        PageParams<ConfigNumberInfoValidator> params = new PageParams<ConfigNumberInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        ConfigNumberInfoValidator configNumberInfoValidator = new ConfigNumberInfoValidator();
        configNumberInfoValidator.setNumberCodeType(type);
        params.setParams(configNumberInfoValidator);

        //查询
        ResponseData<PageList<ConfigNumberInfoValidator>> data = configNumberInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("configNumberInfoValidator", configNumberInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 携号转网列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute ConfigNumberInfoValidator configNumberInfoValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("configure/config_number/config_portability_number_list");

        //分页查询
        configNumberInfoValidator.setNumberCodeType(type);
        pageParams.setParams(configNumberInfoValidator);

        ResponseData<PageList<ConfigNumberInfoValidator>> data = configNumberInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("configNumberInfoValidator", configNumberInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 添加携号转网
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/config_number/config_portability_number_edit");

        //初始化参数
        ConfigNumberInfoValidator configNumberInfoValidator = new ConfigNumberInfoValidator();
        configNumberInfoValidator.setId(UUID.uuid32());
        configNumberInfoValidator.setNumberCodeType(type);
        configNumberInfoValidator.setStatus("1");

        view.addObject("configNumberInfoValidator",configNumberInfoValidator);
        view.addObject("op","add");

        return view;

    }

    /**
     * 编辑携号转网
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/config_number/config_portability_number_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<ConfigNumberInfoValidator> data = configNumberInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("configNumberInfoValidator",data.getData());
        view.addObject("op","edit");

        return view;

    }

    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated ConfigNumberInfoValidator configNumberInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("configure/config_number/config_portability_number_edit");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("configNumberInfoValidator", configNumberInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            configNumberInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            configNumberInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            configNumberInfoValidator.setUpdatedBy(user.getRealName());
            configNumberInfoValidator.setUpdatedTime(new Date());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        log.info("[携号转网管理][{}][{}]数据:{}",op, user.getUserName(), JSON.toJSONString(configNumberInfoValidator));

        //保存操作
        ResponseData data = configNumberInfoService.save(configNumberInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/configure/number/portability/list", true, false));
        return view;
    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public ModelAndView deleteById(@PathVariable String id, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("configure/config_number/config_portability_number_edit");
        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<ConfigNumberInfoValidator> numberData = configNumberInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(numberData.getCode())) {
            view.addObject("error", numberData.getCode() + ":" + numberData.getMessage());
            return view;
        }

        //记录日志
        log.info("[携号转网管理][delete][{}]数据::{}", user.getUserName(), JSON.toJSONString(numberData.getData()));
        //删除操作
        ResponseData data = configNumberInfoService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/configure/number/portability/list" , true, false));
        return view;
    }

    /**
     * 添加携号转网
     *
     * @return
     */
    @RequestMapping(value = "/upFilesView", method = RequestMethod.GET)
    public ModelAndView upFilesView(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/config_number/config_portability_number_upfiles_view");
        view.addObject("configNumberInfoValidator",new ConfigNumberInfoValidator());
        return view;

    }

    /**
     * 导入
     * @param request
     * @return
     */
    @RequestMapping(value = "/upFiles", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute ConfigNumberInfoValidator configNumberInfoValidator,HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");

        ModelAndView view = new ModelAndView("configure/config_number/config_portability_number_upfiles_view");

        /**
         * 获取文件信息
         */
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = mRequest.getFile("file");
        if (file != null && file.getSize() > 0) {

            List<ExcelModel> list = FileUtils.readFile(file,"4");

            //批量保存
            if(!StringUtils.isEmpty(list) && list.size()>0){
                configNumberInfoValidator.setNumberCodeType(type);
                configNumberInfoValidator.setExcelModelList(list);
                configNumberInfoValidator.setStatus("1");
                configNumberInfoValidator.setCreatedBy(user.getRealName());
                ResponseData data  = configNumberInfoService.batchSave(configNumberInfoValidator);
                if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                    view.addObject("error", data.getCode() + ":" + data.getMessage());
                    return view;
                }
            }

            log.info("[携号转网管理][导入][{}]数据::{}", user.getUserName(), list.size());
        }

        view.setView(new RedirectView("/configure/number/portability/list/" , true, false));

        return view;
    }

}
