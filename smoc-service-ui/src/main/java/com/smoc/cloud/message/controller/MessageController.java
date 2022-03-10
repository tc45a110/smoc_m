package com.smoc.cloud.message.controller;


import com.alibaba.fastjson.JSONObject;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.AccountBasicInfoValidator;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.material.service.BusinessAccountService;
import com.smoc.cloud.material.service.MessageService;
import com.smoc.cloud.properties.SmocProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    @RequestMapping(value = "list/{bussinessType}/{signType}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable String bussinessType, @PathVariable String signType, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams",params);
        view.addObject("signType", signType);
        view.addObject("bussinessType", bussinessType);
        return view;
    }

    /**
     * 短信发送分页
     * @param signType
     * @param request
     * @return
     */
    @RequestMapping(value = "page/{bussinessType}/{signType}", method = RequestMethod.POST)
    public ModelAndView page(@PathVariable String bussinessType,@PathVariable String signType,HttpServletRequest request) {
        ModelAndView view = new ModelAndView("message/message_list");
        //查询数据
        PageParams params = new PageParams<>();
        params.setPages(3);
        params.setPageSize(10);
        params.setStartRow(1);
        params.setEndRow(10);
        params.setCurrentPage(1);
        params.setTotalRows(22);

        view.addObject("pageParams",params);
        view.addObject("signType", signType);
        view.addObject("bussinessType", bussinessType);
        return view;
    }

    /**
     * 添加
     * @return
     */
    @RequestMapping(value = "/add/{bussinessType}/{signType}", method = RequestMethod.GET)
    public ModelAndView add(@PathVariable String bussinessType,@PathVariable String signType, HttpServletRequest request) {
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        ModelAndView view = new ModelAndView("message/message_edit");

        //初始化参数
        MessageWebTaskInfoValidator messageWebTaskInfoValidator = new MessageWebTaskInfoValidator();
        messageWebTaskInfoValidator.setBusinessType(bussinessType);
        messageWebTaskInfoValidator.setInfoType(signType);
        messageWebTaskInfoValidator.setSendType("1");

        //查询企业下得所有业务账号
        AccountBasicInfoValidator accountBasicInfoValidator = new AccountBasicInfoValidator();
        accountBasicInfoValidator.setBusinessType(bussinessType);
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
        view.addObject("bussinessType", bussinessType);

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
                messageValidator.setOriginalAttachment(filePath);
                messageValidator = messageService.handleFileSMS(messageValidator);

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
}
