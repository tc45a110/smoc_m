package com.smoc.cloud.customer.controller;

import com.alibaba.fastjson.JSON;
import com.smoc.cloud.admin.security.remote.service.SystemUserLogService;
import com.smoc.cloud.common.auth.entity.SecurityUser;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.smoc.customer.validator.SystemAttachmentValidator;
import com.smoc.cloud.common.validator.MpmIdValidator;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.service.SystemAttachmentService;
import com.smoc.cloud.properties.SmocProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * 附件管理
 */
@Slf4j
@RestController
@RequestMapping("/attachment")
public class SystemAttachmentController {

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Autowired
    private SmocProperties smocProperties;

    @Autowired
    private SystemUserLogService systemUserLogService;

    /**
     * 根据业务id查询附件
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/findByModelId/{id}", produces = "text/html;charset=utf-8", method = RequestMethod.GET)
    public String findByModelId(@PathVariable String id, HttpServletRequest request) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return MpmValidatorUtil.validateMessage(validator);
        }

        //查询附件
        StringBuilder files = new StringBuilder();
        ResponseData<List<SystemAttachmentValidator>> filesList = systemAttachmentService.findByMoudleId(id);
        if (!StringUtils.isEmpty(filesList.getData()) && filesList.getData().size() > 0) {
            List<SystemAttachmentValidator> list = filesList.getData();
            for (int a=0;a<list.size();a++) {
                SystemAttachmentValidator info = list.get(a);
                files.append(info.getAttachmentName()+","+info.getId());
                if (a != list.size()-1) {
                    files.append("@");
                }
            }
            return files.toString();
        }

        return "0";

    }

    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.GET)
    public String deleteById(@PathVariable String id, HttpServletRequest request) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return "";
        }

        //查询删除数据信息
        ResponseData<SystemAttachmentValidator> infoData = systemAttachmentService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(infoData.getCode())) {
            return "";
        }

        //记录日志
        SecurityUser user = (SecurityUser) request.getSession().getAttribute("user");
        log.info("[附件管理][delete][{}]数据::{}", user.getUserName(), JSON.toJSONString(infoData));

        //删除操作
        ResponseData data = systemAttachmentService.deleteById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return data.getCode() + ":" + data.getMessage();
        }

        //保存操作记录
        if (ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            systemUserLogService.logsAsync(infoData.getData().getMoudleInentification(), infoData.getData().getMoudleId(), user.getRealName(), "delete", "删除附件", JSON.toJSONString(infoData));
        }

        return "1";

    }

    /**
     * 下载模板
     */
    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public void download(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {

        //完成参数规则验证
        MpmIdValidator validator = new MpmIdValidator();
        validator.setId(id);
        if (!MpmValidatorUtil.validate(validator)) {
            return;
        }

        //查询数据
        ResponseData<SystemAttachmentValidator> data = systemAttachmentService.findById(id);
        if (!ResponseCode.SUCCESS.getCode().equals(data.getCode())) {
            return;
        }

        //文件路径
        File file = new File(smocProperties.getEnterpriseFilePath() + data.getData().getAttachmentUri());
        if (file.exists()) {

            try {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                // 下载文件能正常显示中文
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(data.getData().getAttachmentName(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // 实现文件下载
            byte[] buffer = new byte[1024];
            InputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                return;
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
}
