package com.smoc.cloud.book.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.book.service.BookService;
import com.smoc.cloud.book.service.GroupService;
import com.smoc.cloud.book.util.FileUtils;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.qo.BookExcelModel;
import com.smoc.cloud.common.smoc.customer.validator.EnterpriseBookInfoValidator;
import com.smoc.cloud.common.smoc.filter.FilterGroupListValidator;
import com.smoc.cloud.common.smoc.filter.FilterWhiteListValidator;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * 通讯录管理
 **/
@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private BookService bookService;

    /**
     * 通讯录列表
     *
     * @return
     */
    @RequestMapping(value = "/list/{parentId}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String parentId, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("book/book_list");

        //查询组
        ResponseData<FilterGroupListValidator> groupData = groupService.findById(parentId);
        if (!ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            view.addObject("error", groupData.getCode() + ":" + groupData.getMessage());
            return view;
        }

        //查询是否是自己企业通讯录
        if(!user.getOrganization().equals(groupData.getData().getEnterpriseId())){
            view.addObject("error", "无法进行查看操作");
            return view;
        }

        //初始化数据
        PageParams<EnterpriseBookInfoValidator> params = new PageParams<EnterpriseBookInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        EnterpriseBookInfoValidator enterpriseBookInfoValidator = new EnterpriseBookInfoValidator();
        enterpriseBookInfoValidator.setGroupId(parentId);
        params.setParams(enterpriseBookInfoValidator);

        //查询
        ResponseData<PageList<EnterpriseBookInfoValidator>> data = bookService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseBookInfoValidator", enterpriseBookInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("filterGroupListValidator",groupData.getData());
        view.addObject("parentId",parentId);

        return view;

    }

    /**
     * 通讯录列表查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute EnterpriseBookInfoValidator enterpriseBookInfoValidator, PageParams pageParams, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("book/book_list");

        //查询组
        ResponseData<FilterGroupListValidator> groupData = groupService.findById(enterpriseBookInfoValidator.getGroupId());
        if (!ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            view.addObject("error", groupData.getCode() + ":" + groupData.getMessage());
            return view;
        }

        //查询是否是自己企业通讯录
        if(!user.getOrganization().equals(groupData.getData().getEnterpriseId())){
            view.addObject("error", "无法进行查看操作");
            return view;
        }

        //分页查询
        pageParams.setParams(enterpriseBookInfoValidator);

        ResponseData<PageList<FilterWhiteListValidator>> data = bookService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("enterpriseBookInfoValidator", enterpriseBookInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("filterGroupListValidator",groupData.getData());
        view.addObject("parentId",enterpriseBookInfoValidator.getGroupId());

        return view;

    }

    /**
     * 添加通讯录
     *
     * @return
     */
    @RequestMapping(value = "/add/{parentId}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String parentId, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("book/book_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(parentId);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询组
        ResponseData<FilterGroupListValidator> data = groupService.findById(parentId);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //查询是否是自己企业通讯录
        if(!user.getOrganization().equals(data.getData().getEnterpriseId())){
            view.addObject("error", "无法进行操作");
            return view;
        }

        //初始化参数
        EnterpriseBookInfoValidator enterpriseBookInfoValidator = new EnterpriseBookInfoValidator();
        enterpriseBookInfoValidator.setId(UUID.uuid32());
        enterpriseBookInfoValidator.setGroupId(parentId);
        enterpriseBookInfoValidator.setStatus("1");
        enterpriseBookInfoValidator.setIsSync("0");

        view.addObject("enterpriseBookInfoValidator",enterpriseBookInfoValidator);
        view.addObject("filterGroupListValidator",data.getData());
        view.addObject("op","add");
        return view;

    }

    /**
     * 编辑通讯录
     *
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable String id, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("book/book_edit");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }
        //查询信息
        ResponseData<EnterpriseBookInfoValidator> data = bookService.findById(id);
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

        //查询是否是自己企业通讯录
        if(!user.getOrganization().equals(groupData.getData().getEnterpriseId())){
            view.addObject("error", "无法进行操作");
            return view;
        }

        view.addObject("enterpriseBookInfoValidator",data.getData());
        view.addObject("filterGroupListValidator",groupData.getData());
        view.addObject("op","edit");
        return view;
    }

    /**
     * 保存通讯录
     * @param enterpriseBookInfoValidator
     * @param result
     * @param op
     * @param request
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated EnterpriseBookInfoValidator enterpriseBookInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("book/book_edit");

        //查询组
        ResponseData<FilterGroupListValidator> groupData = groupService.findById(enterpriseBookInfoValidator.getGroupId());
        if (!ResponseCode.SUCCESS.getCode().equals(groupData.getCode())) {
            view.addObject("error", groupData.getCode() + ":" + groupData.getMessage());
            return view;
        }
        view.addObject("filterGroupListValidator",groupData.getData());

        //查询是否是自己企业通讯录
        if(!user.getOrganization().equals(groupData.getData().getEnterpriseId())){
            view.addObject("error", "无法进行操作");
            return view;
        }

        //完成参数规则验证
        if (result.hasErrors()) {
            view.addObject("enterpriseBookInfoValidator", enterpriseBookInfoValidator);
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            enterpriseBookInfoValidator.setCreatedTime(new Date());
            enterpriseBookInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            ResponseData<EnterpriseBookInfoValidator> data = bookService.findById(enterpriseBookInfoValidator.getId());
            enterpriseBookInfoValidator.setCreatedTime(data.getData().getCreatedTime());
            enterpriseBookInfoValidator.setUpdatedBy(user.getRealName());
            enterpriseBookInfoValidator.setUpdatedTime(new Date());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //记录日志
        log.info("[通讯录管理][{}][{}]数据:{}",op, user.getUserName(), JSON.toJSONString(enterpriseBookInfoValidator));

        //保存操作
        enterpriseBookInfoValidator.setEnterpriseId(user.getOrganization());
        ResponseData data = bookService.save(enterpriseBookInfoValidator, op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/book/list/" + enterpriseBookInfoValidator.getGroupId(), true, false));
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
        ModelAndView view = new ModelAndView("book/book_edit");
        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        ResponseData<EnterpriseBookInfoValidator> whiteData = bookService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(whiteData.getCode())) {
            view.addObject("error", whiteData.getCode() + ":" + whiteData.getMessage());
            return view;
        }

        //查询是否是自己企业通讯录
        if(!user.getOrganization().equals(whiteData.getData().getEnterpriseId())){
            view.addObject("error", "无法进行操作");
            return view;
        }

        //记录日志
        log.info("[通讯录管理][delete][{}]数据::{}", user.getUserName(), JSON.toJSONString(whiteData.getData()));
        //删除操作
        ResponseData data = bookService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.setView(new RedirectView("/book/list/" + whiteData.getData().getGroupId(), true, false));
        return view;
    }

    /**
     * 导入通讯录
     *
     * @return
     */
    @RequestMapping(value = "/upFilesView/{parentId}", method = RequestMethod.GET)
    public ModelAndView upFilesView(@PathVariable String parentId, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("book/book_upfiles_view");

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

        //查询是否是自己企业通讯录
        if(!user.getOrganization().equals(groupData.getData().getEnterpriseId())){
            view.addObject("error", "无法进行操作");
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

        ModelAndView view = new ModelAndView("book/book_upfiles_view");

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

        //查询是否是自己企业通讯录
        if(!user.getOrganization().equals(groupData.getData().getEnterpriseId())){
            view.addObject("error", "无法进行操作");
            return view;
        }

        /**
         * 获取文件信息
         */
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> file = mRequest.getFiles("file[]");
        if(!StringUtils.isEmpty(file) && file.size()>0){

            List<BookExcelModel> list = FileUtils.readFile(file.get(0),"1");

            //批量保存
            if(!StringUtils.isEmpty(list) && list.size()>0){
                EnterpriseBookInfoValidator enterpriseBookInfoValidator = new EnterpriseBookInfoValidator();
                enterpriseBookInfoValidator.setGroupId(groupData.getData().getGroupId());
                enterpriseBookInfoValidator.setEnterpriseId(user.getOrganization());
                enterpriseBookInfoValidator.setExcelModelList(list);
                enterpriseBookInfoValidator.setIsSync("0");
                enterpriseBookInfoValidator.setStatus("1");
                enterpriseBookInfoValidator.setCreatedBy(user.getRealName());
                ResponseData data  = bookService.batchSave(enterpriseBookInfoValidator);
                if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
                    view.addObject("error", data.getCode() + ":" + data.getMessage());
                    return view;
                }
            }

            log.info("[通讯录管理][导入][{}]数据::{}", user.getUserName(), list.size());
        }

        view.setView(new RedirectView("/book/list/" + groupId, true, false));

        return view;
    }

    /**
     * 下载模板
     */
    @RequestMapping(value = "/downFileTemp/{type}", method = RequestMethod.GET)
    public void downFileTemp(@PathVariable String type,HttpServletRequest request, HttpServletResponse response) {

        String fileName = "通讯录数据导入模板.xlsx";

        if("1".equals(type)){
            fileName = "通讯录数据导入模板.xlsx";
        }

        //设置文件路径
        ClassPathResource classPathResource = new ClassPathResource("static/files/" + fileName);
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
            fis = classPathResource.getInputStream();;
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
