package com.smoc.cloud.configure.number.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.DictService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.validator.DictValidator;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigNumberInfoValidator;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import com.smoc.cloud.common.smoc.filter.FilterGroupListValidator;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.configure.number.service.ConfigNumberInfoService;
import com.smoc.cloud.filter.black.service.BlackService;
import com.smoc.cloud.filter.group.service.GroupService;
import com.smoc.cloud.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 号段管理
 **/
@Slf4j
@Controller
@RequestMapping("/configure/number")
public class ConfigNumberInfoController {

    @Autowired
    private ConfigNumberInfoService configNumberInfoService;

    @Autowired
    private DictService dictService;

    private String type = "carrierSegment";

    /**
     * 号段列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/config_number/config_number_list");

        //初始化数据
        PageParams<DictValidator> params = new PageParams<DictValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        DictValidator dictValidator = new DictValidator();
        dictValidator.setDictType(type);
        params.setParams(dictValidator);

        //查询
        ResponseData<PageList<DictValidator>> data = dictService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("dictValidator", dictValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 号段列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute DictValidator dictValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("configure/config_number/config_number_list");

        //分页查询
        dictValidator.setDictType(type);
        pageParams.setParams(dictValidator);

        ResponseData<PageList<DictValidator>> data = dictService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("dictValidator", dictValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 添加号段
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/config_number/config_number_edit");

        //初始化参数
        DictValidator dictValidator = new DictValidator();
        dictValidator.setId(UUID.uuid32());
        dictValidator.setDictType(type);
        dictValidator.setActive(1);
        dictValidator.setTypeId("88be0adffe8f4c65a27f857e3253e4c0");

        view.addObject("dictValidator",dictValidator);
        view.addObject("op","add");

        return view;

    }

    /**
     * 编辑号段
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/config_number/config_number_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<DictValidator> data = dictService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("dictValidator",data.getData());
        view.addObject("op","edit");

        return view;

    }

    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute DictValidator dictValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("configure/config_number/config_number_edit");

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("dictValidator", dictValidator);
            view.addObject("op", op);
            return view;
        }

        //记录日志
        log.info("[号段管理][{}][{}]数据:{}",op, user.getUserName(), JSON.toJSONString(dictValidator));

        //保存操作
        dictValidator.setCreateDateTime(DateTimeUtils.getDateTimeFormat(new Date()));
        ResponseData data = dictService.save(dictValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/configure/number/list", true, false));
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
        ModelAndView view = new ModelAndView("configure/config_number/config_number_edit");
        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<DictValidator> numberData = dictService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(numberData.getCode())) {
            view.addObject("error", numberData.getCode() + ":" + numberData.getMessage());
            return view;
        }

        //记录日志
        log.info("[号段管理][delete][{}]数据::{}", user.getUserName(), JSON.toJSONString(numberData.getData()));
        //删除操作
        ResponseData data = dictService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/configure/number/list" , true, false));
        return view;
    }

    /**
     * 添加号段
     *
     * @return
     */
    @RequestMapping(value = "/upFilesView", method = RequestMethod.GET)
    public ModelAndView upFilesView(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("configure/config_number/config_number_upfiles_view");
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

        ModelAndView view = new ModelAndView("configure/config_number/config_number_upfiles_view");

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

            log.info("[号段管理][导入][{}]数据::{}", user.getUserName(), list.size());
        }

        view.setView(new RedirectView("/configure/number/list/" , true, false));

        return view;
    }

    /**
     * 下载模板
     */
    @RequestMapping(value = "/downFileTemp/{type}", method = RequestMethod.GET)
    public void downFileTemp(@PathVariable String type,HttpServletRequest request, HttpServletResponse response) {

        String fileName = "号段模板.txt";

        if("1".equals(type)){
            fileName = "号段模板.txt";
        }
        if("2".equals(type)){
            fileName = "号段模板.xlsx";
        }
        if("3".equals(type)){
            fileName = "省号码模板.txt";
        }
        if("4".equals(type)){
            fileName = "省号码模板.xlsx";
        }
        if("5".equals(type)){
            fileName = "模板.txt";
        }
        if("6".equals(type)){
            fileName = "模板.xlsx";
        }

        //设置文件路径
        ClassPathResource classPathResource = new ClassPathResource("static/files/number/" + fileName);
        try {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 实现文件下载
        byte[] buffer = new byte[1024];
        InputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = classPathResource.getInputStream();
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            return ;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
