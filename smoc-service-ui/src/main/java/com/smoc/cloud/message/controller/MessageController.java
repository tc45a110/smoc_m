package com.smoc.cloud.message.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.material.service.BusinessAccountService;
import com.smoc.cloud.message.service.MessageService;
import com.smoc.cloud.message.utils.SendMessage;
import com.smoc.cloud.properties.SmocProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
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
 * 短信发送
 */
@Slf4j
@RestController
@RequestMapping("/message")
@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class MessageController {

    @Autowired
    private BusinessAccountService businessAccountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SmocProperties smocProperties;

    /**
     * 短信发送列表
     * @param signType
     * @param request
     * @return
     */
    @RequestMapping(value = "list/{businessType}/{signType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String businessType, @PathVariable String signType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //初始化数据
        PageParams<MessageWebTaskInfoValidator> params = new PageParams<MessageWebTaskInfoValidator>();
        params.setPageSize(10);
        params.setCurrentPage(1);
        MessageWebTaskInfoValidator messageWebTaskInfoValidator = new MessageWebTaskInfoValidator();
        messageWebTaskInfoValidator.setEnterpriseId(user.getOrganization());
        messageWebTaskInfoValidator.setBusinessType(businessType);
        messageWebTaskInfoValidator.setInfoType(signType);
        Date startDate = DateTimeUtils.getFirstMonth(6);
        messageWebTaskInfoValidator.setStartDate(DateTimeUtils.getDateFormat(startDate));
        messageWebTaskInfoValidator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
        params.setParams(messageWebTaskInfoValidator);

        //查询
        ResponseData<PageList<MessageWebTaskInfoValidator>> data = messageService.page(params);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("signType", signType);
        view.addObject("businessType", businessType);
        return view;
    }

    /**
     * 短信发送分页
     * @param request
     * @return
     */
    @RequestMapping(value = "page", method = RequestMethod.POST)
    public ModelAndView page(@ModelAttribute MessageWebTaskInfoValidator messageWebTaskInfoValidator,PageParams pageParams,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_list");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        //分页查询
        messageWebTaskInfoValidator.setEnterpriseId(user.getOrganization());
        if (!StringUtils.isEmpty(messageWebTaskInfoValidator.getStartDate())) {
            String[] date = messageWebTaskInfoValidator.getStartDate().split(" - ");
            messageWebTaskInfoValidator.setStartDate(StringUtils.trimWhitespace(date[0]));
            messageWebTaskInfoValidator.setEndDate(StringUtils.trimWhitespace(date[1]));
        }
        pageParams.setParams(messageWebTaskInfoValidator);


        ResponseData<PageList<AccountTemplateInfoValidator>> data = messageService.page(pageParams);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
        view.addObject("list", data.getData().getList());
        view.addObject("pageParams", data.getData().getPageParams());
        view.addObject("signType", messageWebTaskInfoValidator.getInfoType());
        view.addObject("businessType", messageWebTaskInfoValidator.getBusinessType());
        return view;
    }

    /**
     * 添加
     * @return
     */
    @RequestMapping(value = "/add/{businessType}/{signType}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String businessType,@PathVariable String signType, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("message/message_edit");

        //初始化参数
        MessageWebTaskInfoValidator messageWebTaskInfoValidator = new MessageWebTaskInfoValidator();
        messageWebTaskInfoValidator.setId(UUID.uuid32());
        messageWebTaskInfoValidator.setEnterpriseId(user.getOrganization());
        messageWebTaskInfoValidator.setBusinessType(businessType);
        messageWebTaskInfoValidator.setInfoType(signType);
        messageWebTaskInfoValidator.setSendType("1");
        messageWebTaskInfoValidator.setSendStatus("1");

        //查询企业下得所有业务账号
        AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
        accountBasicInfoValidator.setBusinessType(businessType);
        accountBasicInfoValidator.setInfoType(signType);
        accountBasicInfoValidator.setEnterpriseId(user.getOrganization());
        accountBasicInfoValidator.setAccountStatus("1");//正常
        ResponseData<List<AccountBasicInfoValidator>> info = businessAccountService.findBusinessAccount(accountBasicInfoValidator);
        if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
            view.addObject("error", info.getCode() + ":" + info.getMessage());
            return view;
        }

        //op操作标记，add表示添加，edit表示修改
        view.addObject("op", "add");
        view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
        view.addObject("list", info.getData());
        view.addObject("signType", signType);
        view.addObject("businessType", businessType);

        return view;

    }

    /**
     * 短信发送
     * @param messageWebTaskInfoValidator
     * @param result
     * @param op
     * @param request
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute @Validated MessageWebTaskInfoValidator messageWebTaskInfoValidator, BindingResult result, @PathVariable String op, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_edit");

        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");

        if("2".equals(messageWebTaskInfoValidator.getSendType()) && StringUtils.isEmpty(messageWebTaskInfoValidator.getTimingTime())){
            // 提交前台错误提示
            FieldError err = new FieldError("发送方式", "sendType", "定时时间不能为空");
            result.addError(err);
        }
        if(StringUtils.isEmpty(messageWebTaskInfoValidator.getInputNumber()) && StringUtils.isEmpty(messageWebTaskInfoValidator.getNumberFiles())){
            // 提交前台错误提示
            FieldError err = new FieldError("手机号", "inputNumber", "群发号码，号码文件不能都为空");
            result.addError(err);
        }

        //完成参数规则验证
        if (result.hasErrors()) {
            //查询企业下得所有业务账号
            AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
            accountBasicInfoValidator.setBusinessType(messageWebTaskInfoValidator.getBusinessType());
            accountBasicInfoValidator.setInfoType(messageWebTaskInfoValidator.getInfoType());
            accountBasicInfoValidator.setEnterpriseId(user.getOrganization());
            accountBasicInfoValidator.setAccountStatus("1");//正常
            ResponseData<List<AccountBasicInfoValidator>> info = businessAccountService.findBusinessAccount(accountBasicInfoValidator);
            if (!ResponseCode.SUCCESS.getCode().equals(info.getCode())) {
                view.addObject("error", info.getCode() + ":" + info.getMessage());
                return view;
            }

            view.addObject("messageWebTaskInfoValidator", messageWebTaskInfoValidator);
            view.addObject("list", info.getData());
            view.addObject("signType", messageWebTaskInfoValidator.getInfoType());
            view.addObject("businessType", messageWebTaskInfoValidator.getBusinessType());
            view.addObject("op", op);
            return view;
        }

        //初始化其他变量
        if (!StringUtils.isEmpty(op) && "add".equals(op)) {
            messageWebTaskInfoValidator.setCreatedTime(DateTimeUtils.getDateTimeFormat(new Date()));
            messageWebTaskInfoValidator.setCreatedBy(user.getRealName());
        } else if (!StringUtils.isEmpty(op) && "edit".equals(op)) {
            messageWebTaskInfoValidator.setUpdatedTime(new Date());
            messageWebTaskInfoValidator.setUpdatedBy(user.getRealName());
        } else {
            view.addObject("error", ResponseCode.PARAM_LINK_ERROR.getCode() + ":" + ResponseCode.PARAM_LINK_ERROR.getMessage());
            return view;
        }

        //保存数据
        messageWebTaskInfoValidator = SendMessage.handleFileSMS(messageWebTaskInfoValidator,smocProperties,user.getOrganization());
        ResponseData data = messageService.save(messageWebTaskInfoValidator,op);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            view.addObject("error", data.getCode() + ":" + data.getMessage());
            return view;
        }

        //记录日志
        log.info("[短信群发][{}][{}]数据:{}", op, user.getUserName(), JSON.toJSONString(messageWebTaskInfoValidator));

        view.setView(new RedirectView("/message/list/"+messageWebTaskInfoValidator.getBusinessType()+"/"+messageWebTaskInfoValidator.getInfoType(), true, false));
        return view;

    }

    /**
     * 异步上传号码
     * @param request
     * @return
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public JSONObject uploadFile(HttpServletRequest request) {

        JSONObject result = new JSONObject();
        String code = "1";
        String filePath = "";
        String sendFilePath = "";
        String errorFilePath = "";
        int sendNumber = 0;

        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");

        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = mRequest.getFile("file");
        if(file!=null&&file.getSize()>0){
            try {

                //文件格式非txt，直接返回-1，前端获取后提示用户
                if(!file.getOriginalFilename().endsWith(".txt")){
                    code = "-1";
                    result.put("code", code);
                    return result;
                }

                String nowDay = DateTimeUtils.currentDate(new Date());
                String uuid = UUID.uuid32();
                filePath = "/" + nowDay + "/"+ user.getOrganization() +"/" + uuid + "_source.txt";

                File desFile = new File(smocProperties.getMobileFileRootPath() + "/" + nowDay + "/"+ user.getOrganization());
                if(!desFile.getParentFile().exists()){
                    desFile.mkdirs();
                }
                file.transferTo(new File(smocProperties.getMobileFileRootPath() + filePath));

                //处理手机号，形成文件
                MessageWebTaskInfoValidator messageValidator = new MessageWebTaskInfoValidator();
                messageValidator.setNumberFiles(filePath);
                messageValidator = SendMessage.handleFileSMS(messageValidator,smocProperties,user.getOrganization());

                errorFilePath = messageValidator.getExceptionNumberAttachment();
                sendFilePath = messageValidator.getSendNumberAttachment();
                sendNumber = messageValidator.getSubmitNumber();
                if(!StringUtils.isEmpty(errorFilePath)){
                    request.getSession().setAttribute("errorFilePath", errorFilePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("[短信群发][upload][{}]数据::{}", user.getUserName(), e.getMessage());
                //文件上传异常，直接返回-2，前端获取后提示用户
                code = "-2";
                result.put("code", code);
                return result;
            }
        }

        result.put("code", code);
        result.put("filePath", filePath);
        result.put("sendFilePath", sendFilePath);
        result.put("errorFilePath", errorFilePath);
        result.put("sendNumber", sendNumber);
        return result;
    }

    /**
     * 异常内容文件下载
     */
    @RequestMapping(value = "/downloadErrorFile/{fileUrl}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadErrorFile(@PathVariable String fileUrl, HttpServletRequest request) {
        SecurityUser user = (SecurityUser)request.getSession().getAttribute("user");

        ResponseEntity<byte[]> entity = null;

        //读取文件内容输入流中
        InputStream in = null;
        try {
            fileUrl = request.getSession().getAttribute(fileUrl).toString();

            File downloadFile = new File(smocProperties.getMobileFileRootPath() + fileUrl);

            //文件不存在
            if(!downloadFile.exists()){
                log.info("[短信群发][download][{}]数据::{}", user.getUserName(), "文件不存在:"+downloadFile.getAbsolutePath());
                return entity;
            }

            in = new FileInputStream(downloadFile);
            byte[] body = new byte[in.available()];
            in.read(body);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attchement;filename=" + downloadFile.getName());
            HttpStatus statusCode = HttpStatus.OK;
            entity = new ResponseEntity<byte[]>(body, headers, statusCode);
        } catch (Exception e) {
            log.error("[短信群发][download][{}]数据::{}", user.getUserName(), e.getMessage());
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

    /**
     * 下载txt模板
     */
    @RequestMapping(value = "/downFileTemp/{type}", method = RequestMethod.GET)
    public void downFileTemp(@PathVariable String type,HttpServletRequest request, HttpServletResponse response) {

        String fileName = "example.txt";

        if("1".equals(type)){
            fileName = "example.txt";
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
