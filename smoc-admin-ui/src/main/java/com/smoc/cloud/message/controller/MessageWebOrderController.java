package com.smoc.cloud.message.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.smoc.template.MessageFrameParamers;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.message.service.MessageWebTaskInfoService;
import com.smoc.cloud.properties.SmocProperties;
import com.smoc.cloud.template.service.AccountTemplateInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * web短消息任务单
 **/
@Slf4j
@RestController
@RequestMapping("/message/order/web")
public class MessageWebOrderController {

    @Autowired
    private MessageWebTaskInfoService messageWebTaskInfoService;

    @Autowired
    private AccountTemplateInfoService accountTemplateInfoService;

    @Autowired
    private SmocProperties smocProperties;

    /**
     * web短消息任务单查询
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("message/message_order_web_list");

        //初始化数据
        PageParams<MessageWebTaskInfoValidator> params = new PageParams<MessageWebTaskInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageWebTaskInfoValidator messageWebTaskInfoValidator = new MessageWebTaskInfoValidator();

        Date startDate = DateTimeUtils.getFirstMonth(12);
        messageWebTaskInfoValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        messageWebTaskInfoValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(messageWebTaskInfoValidator);

        //分页查询
        ResponseData<PageList<MessageWebTaskInfoValidator>> data = messageWebTaskInfoService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //数量统计
        ResponseData<Map<String, Object>> count = messageWebTaskInfoService.count(messageWebTaskInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        Map<String,Object>  countMap = count.getData();
        //log.info("[map]:{}",new Gson().toJson(countMap));
        view.addObject("countMap", countMap);
        view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        return view;

    }

    /**
     * web短消息任务单分页查询
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute MessageWebTaskInfoValidator messageWebTaskInfoValidator, PageParams pageParams) {
        ModelAndView view = new ModelAndView("message/message_order_web_list");


        //日期格式
        if (!StringUtils.isEmpty(messageWebTaskInfoValidator.getStartDate())) {
            String[] date = messageWebTaskInfoValidator.getStartDate().split(" - ");
            messageWebTaskInfoValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageWebTaskInfoValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }

        pageParams.setParams(messageWebTaskInfoValidator);

        //log.info("[messageDailyStatisticValidator]:{}",new Gson().toJson(messageDailyStatisticValidator));
        //分页查询
        ResponseData<PageList<MessageWebTaskInfoValidator>> data = messageWebTaskInfoService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //数量统计
        ResponseData<Map<String, Object>> count = messageWebTaskInfoService.count(messageWebTaskInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(count.getCode())) {
            view.addObject("error", count.getCode() + ":" + count.getMessage());
            return view;
        }

        Map<String,Object>  countMap = count.getData();
        //log.info("[map]:{}",new Gson().toJson(countMap));
        view.addObject("countMap", countMap);
        view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());

        return view;

    }

    /**
     * 查看明细
     *
     * @return
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ModelAndView detail(@PathVariable String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_order_web_detail");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            view.addObject("error", ResponseCode.PARAM_ERROR.getCode() + ":" + MpmValidatorUtil.validateMessage(validator));
            return view;
        }

        //查询信息
        ResponseData<MessageWebTaskInfoValidator> infoData = messageWebTaskInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoData.getCode())) {
            view.addObject("error", infoData.getCode() + ":" + infoData.getMessage());
            return view;
        }

        //查询模板
        ResponseData<AccountTemplateInfoValidator> data = accountTemplateInfoService.findById(infoData.getData().getTemplateId());
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
        }

        //还原帧数据
        if("MULTI_SMS".equals(data.getData().getTemplateType()) || "MMS".equals(data.getData().getTemplateType())){
            List<MessageFrameParamers> paramsSort = new ArrayList<MessageFrameParamers>();
            if (!StringUtils.isEmpty(data.getData().getMmAttchment())) {
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonArray Jarray = parser.parse(data.getData().getMmAttchment()).getAsJsonArray();
                for(JsonElement obj : Jarray){
                    MessageFrameParamers p = gson.fromJson(obj , MessageFrameParamers.class);
                    paramsSort.add(p);
                }
            }
            int allSize = 0;
            for(MessageFrameParamers p:paramsSort){
                allSize += new Integer(p.getResSize());
            }
            view.addObject("params", paramsSort);
            view.addObject("allSize", allSize);
        }

        view.addObject("messageWebTaskInfoValidator", infoData.getData());
        view.addObject("accountTemplateInfoValidator", data.getData());
        view.addObject("signType", infoData.getData().getInfoType());
        view.addObject("businessType", infoData.getData().getBusinessType());

        return view;
    }

    /**
     * 查询附件
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/findFilesById/{id}", produces = "text/html;charset=utf-8", method = RequestMethod.GET)
    public String findByModelId(@PathVariable String id, HttpServletRequest request) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return MpmValidatorUtil.validateMessage(validator);
        }

        //查询
        ResponseData<MessageWebTaskInfoValidator> data = messageWebTaskInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return "5";
        }

        //查询附件
        StringBuilder files = new StringBuilder();
        MessageWebTaskInfoValidator messageWebTaskInfoValidator = data.getData();
        if(!StringUtils.isEmpty(messageWebTaskInfoValidator.getNumberFiles())){
            files.append("1@");
        }
        if(!StringUtils.isEmpty(messageWebTaskInfoValidator.getSendNumberAttachment())){
            files.append("2@");
        }
        if(!StringUtils.isEmpty(messageWebTaskInfoValidator.getExceptionNumberAttachment())){
            files.append("3@");
        }
        if(!StringUtils.isEmpty(messageWebTaskInfoValidator.getReportAttachment())){
            files.append("4");
        }

        if(StringUtils.isEmpty(files.toString())){
            return "5";
        }

        return files.toString();
    }

    /**
     * 根据文件类型下载各类号码文件
     */
    @RequestMapping(value = "/download/{fileType}/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileType, @PathVariable String id, HttpServletRequest request) {
        ResponseEntity<byte[]> entity = null;

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        ResponseData<MessageWebTaskInfoValidator> data = messageWebTaskInfoService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return entity;
        }

        //查看任务是否属于改企业
        if(!user.getOrganization().equals(data.getData().getEnterpriseId())){
            return entity;
        }

        MessageWebTaskInfoValidator messageValidator = data.getData();

        File downloadFile = null;
        if("numberFiles".equals(fileType)){
            downloadFile = new File(smocProperties.getMobileFileRootPath() + messageValidator.getNumberFiles());
        }else if("sendNumber".equals(fileType)){
            downloadFile = new File(smocProperties.getMobileFileRootPath() + messageValidator.getSendNumberAttachment());
        }else if("exceptionFile".equals(fileType)){
            downloadFile = new File(smocProperties.getMobileFileRootPath() + messageValidator.getExceptionNumberAttachment());
        }else if("reportFile".equals(fileType)){
            downloadFile = new File(smocProperties.getMobileFileRootPath() + messageValidator.getReportAttachment());
        }else{
            return entity;
        }

        //文件不存在
        if(!downloadFile.exists()){
            return entity;
        }

        //读取文件内容输入流中
        InputStream in = null;
        try {
            in = new FileInputStream(downloadFile);
            byte[] body = new byte[in.available()];
            in.read(body);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attchement;filename=" + downloadFile.getName());
            HttpStatus statusCode = HttpStatus.OK;
            entity = new ResponseEntity<byte[]>(body, headers, statusCode);
        } catch (Exception e) {
            log.error("[短信任务单][download][{}]数据::{}", user.getUserName(), e.getMessage());
            e.printStackTrace();
        } finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return entity;
    }
}
