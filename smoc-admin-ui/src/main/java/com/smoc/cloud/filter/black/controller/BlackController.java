package com.smoc.cloud.filter.black.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.filter.ExcelModel;
import com.smoc.cloud.common.smoc.filter.FilterBlackListValidator;
import com.smoc.cloud.common.smoc.filter.FilterGroupListValidator;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.filter.black.service.BlackService;
import com.smoc.cloud.filter.group.service.GroupService;
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
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 黑名单管理
 **/
@Slf4j
@Controller
@RequestMapping("/filter/black")
public class BlackController {

    @Autowired
    private BlackService blackService;

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView main(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/black/black_main");

        view.addObject("parentId","smoc_black");

        return view;
    }


    /**
     * 黑名单列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String parentId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/black/black_list");

        //初始化数据
        PageParams<FilterBlackListValidator> params = new PageParams<FilterBlackListValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        FilterBlackListValidator filterBlackListValidator = new FilterBlackListValidator();
        filterBlackListValidator.setGroupId(parentId);
        params.setParams(filterBlackListValidator);

        //查询
        ResponseData<PageList<FilterBlackListValidator>> data = blackService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询组
        ResponseData<FilterGroupListValidator> groupData = groupService.findById(parentId);
        if (!ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            view.addObject("error", groupData.getCode() + ":" + groupData.getMessage());
            return view;
        }

        view.addObject("filterBlackListValidator", filterBlackListValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("filterGroupListValidator",groupData.getData());
        view.addObject("parentId",parentId);

        return view;

    }

    /**
     * 黑名单列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute FilterBlackListValidator filterBlackListValidator, PageParams pageParams) {

        ModelAndView view = new ModelAndView("filter/black/black_list");

        //分页查询
        pageParams.setParams(filterBlackListValidator);

        ResponseData<PageList<FilterBlackListValidator>> data = blackService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询组
        ResponseData<FilterGroupListValidator> groupData = groupService.findById(filterBlackListValidator.getGroupId());
        if (!ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            view.addObject("error", groupData.getCode() + ":" + groupData.getMessage());
            return view;
        }

        view.addObject("filterBlackListValidator", filterBlackListValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("filterGroupListValidator",groupData.getData());
        view.addObject("parentId",filterBlackListValidator.getGroupId());

        return view;

    }

    /**
     * 添加黑名单
     *
     * @return
     */
    @RequestMapping(value = "/add/{parentId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String parentId,HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/black/black_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //初始化参数
        FilterBlackListValidator filterBlackListValidator = new FilterBlackListValidator();
        filterBlackListValidator.setId(UUID.uuid32());
        filterBlackListValidator.setGroupId(parentId);
        filterBlackListValidator.setEnterpriseId("SMOC");
        filterBlackListValidator.setStatus("1");
        filterBlackListValidator.setIsSync("0");

        //查询组
        ResponseData<FilterGroupListValidator> data = groupService.findById(parentId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("filterBlackListValidator",filterBlackListValidator);
        view.addObject("filterGroupListValidator",data.getData());
        view.addObject("op","add");

        return view;

    }

    /**
     * 编辑黑名单
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/black/black_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<FilterBlackListValidator> data = blackService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询组
        ResponseData<FilterGroupListValidator> groupData = groupService.findById(data.getData().getGroupId());
        if (!ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            view.addObject("error", groupData.getCode() + ":" + groupData.getMessage());
            return view;
        }

        view.addObject("filterBlackListValidator",data.getData());
        view.addObject("filterGroupListValidator",groupData.getData());
        view.addObject("op","edit");

        return view;

    }

    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated FilterBlackListValidator filterBlackListValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("filter/black/black_edit");

        //查询组
        ResponseData<FilterGroupListValidator> groupData = groupService.findById(filterBlackListValidator.getGroupId());
        if (!ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            view.addObject("error", groupData.getCode() + ":" + groupData.getMessage());
            return view;
        }
        view.addObject("filterGroupListValidator",groupData.getData());

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("filterBlackListValidator", filterBlackListValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            filterBlackListValidator.setCreatedTime(new Date());
            filterBlackListValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            ResponseData<FilterBlackListValidator> data = blackService.findById(filterBlackListValidator.getId());
            filterBlackListValidator.setCreatedTime(data.getData().getCreatedTime());
            filterBlackListValidator.setUpdatedBy(user.getRealName());
            filterBlackListValidator.setUpdatedTime(new Date());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        log.info("[黑名单管理][{}][{}]数据:{}",op, user.getUserName(), JSON.toJSONString(filterBlackListValidator));

        //保存操作
        ResponseData data = blackService.save(filterBlackListValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/filter/black/list/" + filterBlackListValidator.getGroupId(), true, false));
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
        ModelAndView view = new ModelAndView("filter/black/black_edit");
        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<FilterBlackListValidator> whiteData = blackService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(whiteData.getCode())) {
            view.addObject("error", whiteData.getCode() + ":" + whiteData.getMessage());
            return view;
        }

        //记录日志
        log.info("[黑名单管理][delete][{}]数据::{}", user.getUserName(), JSON.toJSONString(whiteData.getData()));
        //删除操作
        ResponseData data = blackService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/filter/black/list/" + whiteData.getData().getGroupId(), true, false));
        return view;
    }

    /**
     * 添加黑名单
     *
     * @return
     */
    @RequestMapping(value = "/upFilesView/{parentId}", method = RequestMethod.GET)
    public ModelAndView upFilesView(@PathVariable String parentId, HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/black/black_upfiles_view");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询组
        ResponseData<FilterGroupListValidator> groupData = groupService.findById(parentId);
        if (!ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            view.addObject("error", groupData.getCode() + ":" + groupData.getMessage());
            return view;
        }

        view.addObject("parentId", groupData.getData().getId());
        view.addObject("filterGroupListValidator", groupData.getData());

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

        ModelAndView view = new ModelAndView("filter/black/black_upfiles_view");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(groupId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询组
        ResponseData<FilterGroupListValidator> groupData = groupService.findById(groupId);
        if (!ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            view.addObject("error", groupData.getCode() + ":" + groupData.getMessage());
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
                FilterBlackListValidator filterBlackListValidator = new FilterBlackListValidator();
                filterBlackListValidator.setGroupId(groupData.getData().getGroupId());
                filterBlackListValidator.setExcelModelList(list);
                filterBlackListValidator.setIsSync("0");
                filterBlackListValidator.setStatus("1");
                filterBlackListValidator.setCreatedBy(user.getRealName());
                ResponseData data  = blackService.batchSave(filterBlackListValidator);
                if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                    view.addObject("error", data.getCode() + ":" + data.getMessage());
                    return view;
                }
            }

            log.info("[黑名单管理][导入][{}]数据::{}", user.getUserName(), list.size());
        }

        view.setView(new RedirectView("/filter/black/list/" + groupId, true, false));

        return view;
    }

    /**
     * 导出黑名单
     *
     * @return
     */
    @RequestMapping(value = "/downFilesView/{parentId}", method = RequestMethod.GET)
    public ModelAndView downFilesView(@PathVariable String parentId,HttpServletRequest request) {

        ModelAndView view = new ModelAndView("filter/black/black_downfiles_view");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询组
        ResponseData<FilterGroupListValidator> groupData = groupService.findById(parentId);
        if (!ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            view.addObject("error", groupData.getCode() + ":" + groupData.getMessage());
            return view;
        }

        view.addObject("parentId", groupData.getData().getId());
        view.addObject("type", "1");
        view.addObject("filterGroupListValidator", groupData.getData());
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

        //查询组
        ResponseData<FilterGroupListValidator> groupData = groupService.findById(groupId);
        if (!ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            return ;
        }

        /**
         * 查询要导出的数据
         */
        PageParams<FilterWhiteListValidator> params = new PageParams<FilterWhiteListValidator>();
        params.setPageSize(100000);
        params.setCurrentPage(1);
        FilterWhiteListValidator filterWhiteListValidator = new FilterWhiteListValidator();
        filterWhiteListValidator.setGroupId(groupData.getData().getGroupId());
        params.setParams(filterWhiteListValidator);
        ResponseData<List<ExcelModel>> data = blackService.excelModel(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return ;
        }

        String excelname = groupData.getData().getGroupName();

        FileUtils.downFiles(excelname,expType,data.getData(),request,response);
    }

}
