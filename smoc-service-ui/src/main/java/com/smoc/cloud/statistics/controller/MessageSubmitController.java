package com.smoc.cloud.statistics.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.message.model.MessageTaskDetail;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSend;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.message.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * 短信提交记录
 */
@Slf4j
@Controller
@RequestMapping("/statistics/submit")
public class MessageSubmitController {

    @Autowired
    private MessageService messageService;

    /**
     * 短信提交记录列表
     * @param businessType
     * @param request
     * @return
     */
    @RequestMapping(value = "list/{businessType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("statistics/message_submit_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<MessageWebTaskInfoValidator> params = new PageParams<MessageWebTaskInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageWebTaskInfoValidator messageWebTaskInfoValidator = new MessageWebTaskInfoValidator();
        messageWebTaskInfoValidator.setEnterpriseId(user.getOrganization());
        messageWebTaskInfoValidator.setBusinessType(businessType);
        Date startDate = DateTimeUtils.getFirstMonth(6);
        messageWebTaskInfoValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        messageWebTaskInfoValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(messageWebTaskInfoValidator);

        //查询
        ResponseData<PageList<MessageWebTaskInfoValidator>> data = messageService.httpPage(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("businessType", businessType);
        return view;
    }

    /**
     * 短信提交记录分页
     * @param request
     * @return
     */
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute MessageWebTaskInfoValidator messageWebTaskInfoValidator, PageParams pageParams, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("statistics/message_submit_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        messageWebTaskInfoValidator.setEnterpriseId(user.getOrganization());
        if (!StringUtils.isEmpty(messageWebTaskInfoValidator.getStartDate())) {
            String[] date = messageWebTaskInfoValidator.getStartDate().split(" - ");
            messageWebTaskInfoValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageWebTaskInfoValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }
        pageParams.setParams(messageWebTaskInfoValidator);

        //查询
        ResponseData<PageList<MessageWebTaskInfoValidator>> data = messageService.httpPage(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("businessType", messageWebTaskInfoValidator.getBusinessType());
        return view;
    }

    /**
     * 查看明细
     *
     * @return
     */
    @RequestMapping(value = "/message/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("statistics/message_submit_detail_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<MessageWebTaskInfoValidator> infoData = messageService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoData.getCode())) {
            view.addObject("error", infoData.getCode() + ":" + infoData.getMessage());
            return view;
        }

        //查看是否是自己企业
        if(!user.getOrganization().equals(infoData.getData().getEnterpriseId())){
            view.addObject("error", "无法查看！");
            return view;
        }

        //初始化数据
        PageParams<MessageTaskDetail> params = new PageParams<MessageTaskDetail>();
        params.setPageSize(20);
        params.setCurrentPage(1);
        MessageTaskDetail messageTaskDetail = new MessageTaskDetail();
        messageTaskDetail.setTaskId(id);
        params.setParams(messageTaskDetail);

        //查询
        ResponseData<PageList<MessageTaskDetail>> data = messageService.httpTaskDetailList(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageTaskDetail", messageTaskDetail);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("messageWebTaskInfoValidator", infoData.getData());

        return view;
    }

    /**
     * 查看明细分页
     * @param messageTaskDetail
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/message/detail/page", method = RequestMethod.POST)
    public ModelAndView detailPage(@ModelAttribute MessageTaskDetail messageTaskDetail,PageParams params,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("statistics/message_submit_detail_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //查询信息
        ResponseData<MessageWebTaskInfoValidator> infoData = messageService.findById(messageTaskDetail.getTaskId());
        if (!ResponseCode.SUCCESS.getCode().equals(infoData.getCode())) {
            view.addObject("error", infoData.getCode() + ":" + infoData.getMessage());
            return view;
        }

        //查看是否是自己企业
        if(!user.getOrganization().equals(infoData.getData().getEnterpriseId())){
            view.addObject("error", "无法查看！");
            return view;
        }

        //查询
        params.setParams(messageTaskDetail);
        ResponseData<PageList<MessageTaskDetail>> data = messageService.httpTaskDetailList(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageTaskDetail", messageTaskDetail);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("messageWebTaskInfoValidator", infoData.getData());
        return view;
    }

    /**
     * 导出明细
     *
     * @return
     */
    @RequestMapping(value = "/detail/expMessage", method = RequestMethod.POST)
    public void expMessage(String taskId, HttpServletRequest request, HttpServletResponse response) {

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(taskId);
        if (!MpmValidatorUtil.validate(validator)) {
            return ;
        }

        //查询信息
        ResponseData<MessageWebTaskInfoValidator> infoData = messageService.findById(taskId);
        if (!ResponseCode.SUCCESS.getCode().equals(infoData.getCode())) {
            return ;
        }

        //查看是否是自己企业
        if(!user.getOrganization().equals(infoData.getData().getEnterpriseId())){
            return ;
        }

        //初始化数据
        PageParams<MessageTaskDetail> params = new PageParams<MessageTaskDetail>();
        params.setPageSize(1000000);
        params.setCurrentPage(1);
        MessageTaskDetail messageTaskDetail = new MessageTaskDetail();
        messageTaskDetail.setTaskId(taskId);
        params.setParams(messageTaskDetail);

        //查询
        ResponseData<PageList<MessageTaskDetail>> data = messageService.httpTaskDetailList(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return ;
        }

        ServletOutputStream outputStream = null;
        try {
            outputStream = getOutputStream(infoData.getData().getId(), response);
            ExcelWriterBuilder writeBook = EasyExcel.write(outputStream,MessageTaskDetail.class);
            ExcelWriterSheetBuilder sheet = writeBook.sheet(infoData.getData().getId());
            sheet.doWrite(data.getData().getList());
        } catch (Exception e) {
            log.error("导出excel表格失败:", e);
        }

    }

    private ServletOutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            //设置响应的类型
            response.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
            //设置响应的编码格式
            response.setCharacterEncoding("utf8");
            //设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");
            return response.getOutputStream();
        } catch (IOException e) {
            log.error("导出excel表格失败:", e);
            throw new Exception("导出excel表格失败!", e);
        }
    }
}
