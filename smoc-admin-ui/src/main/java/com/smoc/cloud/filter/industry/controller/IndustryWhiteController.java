package com.smoc.cloud.filter.industry.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.auth.qo.Dict;
import com.smoc.cloud.common.auth.qo.DictType;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.white.service.WhiteService;
import com.smoc.cloud.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 行业白名单
 */
@Slf4j
@RestController
@RequestMapping("/filter/industry/white")
public class IndustryWhiteController {

    @Autowired
    private WhiteService whiteService;

    private String industry = "INDUSTRY";

    /**
     * 名单 列表页
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String parentId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/industry/white/industry_white_list");

        //初始化数据
        PageParams<FilterWhiteListValidator> params = new PageParams<FilterWhiteListValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FilterWhiteListValidator filterWhiteListValidator = new FilterWhiteListValidator();
        filterWhiteListValidator.setEnterpriseId(industry);
        filterWhiteListValidator.setGroupId(parentId);
        params.setParams(filterWhiteListValidator);

        //查询
        ResponseData<PageList<FilterWhiteListValidator>> data = whiteService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterWhiteListValidator", filterWhiteListValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("parentId",parentId);
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    /**
     * 白名单列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FilterWhiteListValidator filterWhiteListValidator, PageParams pageParams, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/industry/white/industry_white_list");

        //分页查询
        filterWhiteListValidator.setEnterpriseId(industry);
        pageParams.setParams(filterWhiteListValidator);

        ResponseData<PageList<FilterWhiteListValidator>> data = whiteService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterWhiteListValidator", filterWhiteListValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("parentId",filterWhiteListValidator.getGroupId());
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    /**
     * 添加白名单
     *
     * @return
     */
    @RequestMapping(value = "/add/{parentId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String parentId,HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/industry/white/industry_white_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //初始化参数
        FilterWhiteListValidator filterWhiteListValidator = new FilterWhiteListValidator();
        filterWhiteListValidator.setId(UUID.uuid32());
        filterWhiteListValidator.setGroupId(parentId);
        filterWhiteListValidator.setEnterpriseId(industry);
        filterWhiteListValidator.setStatus("1");
        filterWhiteListValidator.setIsSync("0");

        view.addObject("filterWhiteListValidator",filterWhiteListValidator);
        view.addObject("op","add");
        view.addObject("parentId",parentId);
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    /**
     * 编辑白名单
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/industry/white/industry_white_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<FilterWhiteListValidator> data = whiteService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterWhiteListValidator",data.getData());
        view.addObject("op","edit");
        view.addObject("parentId",data.getData().getGroupId());
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated FilterWhiteListValidator filterWhiteListValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("filter/industry/white/industry_white_edit");


        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("dictValueMap", dictMap(request));
            view.addObject("parentId",filterWhiteListValidator.getGroupId());
            view.addObject("filterWhiteListValidator", filterWhiteListValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            filterWhiteListValidator.setCreatedTime(new Date());
            filterWhiteListValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            ResponseData<FilterWhiteListValidator> data = whiteService.findById(filterWhiteListValidator.getId());
            filterWhiteListValidator.setCreatedTime(data.getData().getCreatedTime());
            filterWhiteListValidator.setUpdatedBy(user.getRealName());
            filterWhiteListValidator.setUpdatedTime(new Date());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        log.info("[行业白名单管理][{}][{}]数据:{}",op, user.getUserName(), JSON.toJSONString(filterWhiteListValidator));

        //保存操作
        ResponseData data = whiteService.save(filterWhiteListValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/filter/industry/white/list/" + filterWhiteListValidator.getGroupId(), true, false));
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
        ModelAndView view = new ModelAndView("filter/industry/white/industry_white_edit");
        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<FilterWhiteListValidator> whiteData = whiteService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(whiteData.getCode())) {
            view.addObject("error", whiteData.getCode() + ":" + whiteData.getMessage());
            return view;
        }

        //记录日志
        log.info("[行业白名单管理][delete][{}]数据::{}", user.getUserName(), JSON.toJSONString(whiteData.getData()));
        //删除操作
        ResponseData data = whiteService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/filter/industry/white/list/" + whiteData.getData().getGroupId(), true, false));
        return view;
    }

    /**
     * 添加白名单
     *
     * @return
     */
    @RequestMapping(value = "/upFilesView/{parentId}", method = RequestMethod.GET)
    public ModelAndView upFilesView(@PathVariable String parentId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/industry/white/industry_white_upfiles_view");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        view.addObject("parentId",parentId);
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    /**
     * 导入
     * @param request
     * @return
     */
    @RequestMapping(value = "/upFiles", method = RequestMethod.POST)
    public ModelAndView save(String groupId, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");

        ModelAndView view = new ModelAndView("filter/industry/white/industry_white_upfiles_view");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(groupId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        /**
         * 获取文件信息
         */
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = mRequest.getFile("file");
        if (file != null && file.getSize() > 0) {

            List<ExcelModel> list = FileUtils.readFile(file,"1");

            //批量保存
            if(!StringUtils.isEmpty(list) && list.size()>0){
                FilterWhiteListValidator filterWhiteListValidator = new FilterWhiteListValidator();
                filterWhiteListValidator.setEnterpriseId(industry);
                filterWhiteListValidator.setGroupId(groupId);
                filterWhiteListValidator.setExcelModelList(list);
                filterWhiteListValidator.setIsSync("0");
                filterWhiteListValidator.setStatus("1");
                filterWhiteListValidator.setCreatedBy(user.getRealName());
                ResponseData data  = whiteService.batchSave(filterWhiteListValidator);
                if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                    view.addObject("error", data.getCode() + ":" + data.getMessage());
                    return view;
                }
            }

            log.info("[行业白名单管理][导入][{}]数据::{}", user.getUserName(), list.size());
        }

        view.setView(new RedirectView("/filter/industry/white/list/" + groupId, true, false));

        return view;
    }

    /**
     * 导出白名单
     *
     * @return
     */
    @RequestMapping(value = "/downFilesView/{parentId}", method = RequestMethod.GET)
    public ModelAndView downFilesView(@PathVariable String parentId,HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/industry/white/industry_white_downfiles_view");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        view.addObject("type", "1");
        view.addObject("parentId",parentId);
        view.addObject("dictValueMap", dictMap(request));

        return view;

    }

    /**
     * 导出
     * @param expType
     * @param groupId
     * @param request
     * @param response
     */
    @RequestMapping(value = "/downFiles", method = RequestMethod.POST)
    public void exceBookData(String groupId,String expType,HttpServletRequest request, HttpServletResponse response) {

        /**
         * 查询要导出的数据
         */
        PageParams<FilterWhiteListValidator> params = new PageParams<FilterWhiteListValidator>();
        params.setPageSize(100000);
        params.setCurrentPage(1);
        FilterWhiteListValidator filterWhiteListValidator = new FilterWhiteListValidator();
        filterWhiteListValidator.setGroupId(groupId);
        params.setParams(filterWhiteListValidator);
        ResponseData<List<ExcelModel>> data = whiteService.excelModel(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return ;
        }

        Map<String, String> dictMap = dictMap(request);
        String excelname = dictMap.get(groupId)+"白名单";

        FileUtils.downFiles(excelname,expType,data.getData(),request,response);
    }

    /**
     * 取字典数据，对关键词进行分类
     */
    private Map<String, String> dictMap(HttpServletRequest request) {
        Map<String, DictType> dictMap = (Map<String, DictType>) request.getServletContext().getAttribute("dict");
        //行业分类
        DictType infoType = dictMap.get("industryBlackList");

        Map<String, String> dictValueMap = new HashMap<>();

        for (Dict dict : infoType.getDict()) {
            dictValueMap.put(dict.getFieldCode(), dict.getFieldName());
        }

        return dictValueMap;
    }

}
