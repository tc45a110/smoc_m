package com.smoc.cloud.configure.number.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.SystemSegmentProvinceCityValidator;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.number.service.SegmentProvinceCityService;
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
import java.util.List;

/**
 * 省号码管理
 **/
@Slf4j
@Controller
@RequestMapping("/configure/number/province")
public class ConfigProvinceNumberInfoController {

    @Autowired
    private SegmentProvinceCityService segmentProvinceCityService;

    /**
     * 省号码列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/config_number/config_province_number_list");

        //初始化数据
        PageParams<SystemSegmentProvinceCityValidator> params = new PageParams<SystemSegmentProvinceCityValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator = new SystemSegmentProvinceCityValidator();
        params.setParams(systemSegmentProvinceCityValidator);

        //查询
        ResponseData<PageList<SystemSegmentProvinceCityValidator>> data = segmentProvinceCityService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemSegmentProvinceCityValidator", systemSegmentProvinceCityValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 省号码列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("configure/config_number/config_province_number_list");

        //分页查询
        pageParams.setParams(systemSegmentProvinceCityValidator);

        ResponseData<PageList<SystemSegmentProvinceCityValidator>> data = segmentProvinceCityService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemSegmentProvinceCityValidator", systemSegmentProvinceCityValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 添加省号码
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/config_number/config_province_number_edit");

        //初始化参数
        SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator = new SystemSegmentProvinceCityValidator();
        systemSegmentProvinceCityValidator.setId(UUID.uuid32());

        view.addObject("systemSegmentProvinceCityValidator",systemSegmentProvinceCityValidator);
        view.addObject("op","add");

        return view;

    }

    /**
     * 编辑省号码
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/config_number/config_province_number_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<SystemSegmentProvinceCityValidator> data = segmentProvinceCityService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("systemSegmentProvinceCityValidator",data.getData());
        view.addObject("op","edit");

        return view;

    }

    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("configure/config_number/config_province_number_edit");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("systemSegmentProvinceCityValidator", systemSegmentProvinceCityValidator);
            view.addObject("op", op);
            return view;
        }

        //记录日志
        log.info("[省号码管理][{}][{}]数据:{}",op, user.getUserName(), JSON.toJSONString(systemSegmentProvinceCityValidator));

        //保存操作
        ResponseData data = segmentProvinceCityService.save(systemSegmentProvinceCityValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/configure/number/province/list", true, false));
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
        ModelAndView view = new ModelAndView("configure/config_number/config_province_number_edit");
        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<SystemSegmentProvinceCityValidator> numberData = segmentProvinceCityService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(numberData.getCode())) {
            view.addObject("error", numberData.getCode() + ":" + numberData.getMessage());
            return view;
        }

        //记录日志
        log.info("[省号码管理][delete][{}]数据::{}", user.getUserName(), JSON.toJSONString(numberData.getData()));
        //删除操作
        ResponseData data = segmentProvinceCityService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/configure/number/province/list" , true, false));
        return view;
    }

    /**
     * 添加省号码
     *
     * @return
     */
    @RequestMapping(value = "/upFilesView", method = RequestMethod.GET)
    public ModelAndView upFilesView(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/config_number/config_province_number_upfiles_view");
        view.addObject("systemSegmentProvinceCityValidator",new SystemSegmentProvinceCityValidator());
        return view;

    }

    /**
     * 导入
     * @param request
     * @return
     */
    @RequestMapping(value = "/upFiles", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute SystemSegmentProvinceCityValidator systemSegmentProvinceCityValidator,HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");

        ModelAndView view = new ModelAndView("configure/config_number/config_province_number_upfiles_view");

        /**
         * 获取文件信息
         */
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = mRequest.getFile("file");
        if (file != null && file.getSize() > 0) {

            List<ExcelModel> list = FileUtils.readFile(file,"4");

            //批量保存
            if(!StringUtils.isEmpty(list) && list.size()>0){
                systemSegmentProvinceCityValidator.setExcelModelList(list);
                ResponseData data  = segmentProvinceCityService.batchSave(systemSegmentProvinceCityValidator);
                if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                    view.addObject("error", data.getCode() + ":" + data.getMessage());
                    return view;
                }
            }

            log.info("[省号码管理][导入][{}]数据::{}", user.getUserName(), list.size());
        }

        view.setView(new RedirectView("/configure/number/province/list/" , true, false));

        return view;
    }

}
