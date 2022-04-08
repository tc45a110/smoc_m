package com.smoc.cloud.message.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smoc.cloud.common.smoc.template.MessageFrameParamers;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.utils.Utils;
import com.smoc.cloud.properties.MessageProperties;
import com.smoc.cloud.properties.ResourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SendMessage {

    /**
     * 异步上传手机号
     * @param messageValidator
     * @param smocProperties
     * @param org
     * @return
     */
    public static MessageWebTaskInfoValidator handleFileSMS(MessageWebTaskInfoValidator messageValidator, MessageProperties smocProperties, String org) {

        BufferedReader reader = null;

        try {
            //发送内容文件路径
            String fileSendPath = null;
            //异常发送内容文件路径
            String fileError = null;
            //未拆分的总条数
            Integer sendMessageNumber = 0;
            //拆分后的总条数
            Integer sendMessageNumberSplit = 0;
            //上传文件字节数，解析后更新保存数据库
            Long originalAttachmentSize = 0l;

            //经过去重，校验手机号格式，变量参数，黑名单后得到的待发送号码集合
            HashSet<String> validNumbers = new HashSet<String>();
            //不符合手机号格式的异常号码集合
            HashSet<String> errorFormatNumbers = new HashSet<String>();

            //重复号码集合
            HashSet<String> repeatNumbers = new HashSet<String>();

            //解析表单上传的手机号码文件
            String fileSource = messageValidator.getNumberFiles();
            if (!StringUtils.isEmpty(fileSource)) {
                File originalFile = new File(smocProperties.getMobileFileRootPath() + fileSource);
                originalAttachmentSize = originalFile.length();
                reader = new BufferedReader(new FileReader(originalFile));
                String tmp = null;
                while ((tmp = reader.readLine()) != null) {
                    if(StringUtils.isEmpty(tmp)){
                        continue;
                    }

                    if(!Utils.isPhone(tmp)){
                        errorFormatNumbers.add(tmp);
                    }else if(validNumbers.contains(tmp)){
                        repeatNumbers.add(tmp);
                    }else{
                        validNumbers.add(tmp);
                    }
                }
            }

            //生成待发送号码文件
            if(!validNumbers.isEmpty()){
                if(!StringUtils.isEmpty(fileSource)){
                    fileSendPath = fileSource.replace("_source", "_send");
                }

                sendMessageNumber = validNumbers.size();
                sendMessageNumberSplit = countSendNumber(messageValidator.getMessageContent())*sendMessageNumber;
                FileUtils.writeLines(new File(smocProperties.getMobileFileRootPath() + fileSendPath), validNumbers);
            }

            //生成异常号码文件
            if(!errorFormatNumbers.isEmpty()||!repeatNumbers.isEmpty()){
                if(!StringUtils.isEmpty(fileSource)){
                    fileError = fileSource.replace("_source", "_error");
                }

                List<String> errorContents = new ArrayList<String>();
                for(String tmp:errorFormatNumbers){
                    errorContents.add(tmp+"(手机号码不符合规范)");
                }

                for(String tmp:repeatNumbers){
                    errorContents.add(tmp+"(重复号码)");
                }

                FileUtils.writeLines(new File(smocProperties.getMobileFileRootPath() + fileError), errorContents);
            }

            messageValidator.setSendNumberAttachment(fileSendPath);
            messageValidator.setExceptionNumberAttachment(fileError);
            messageValidator.setSubmitNumber(sendMessageNumber);
            messageValidator.setNumberAttachmentSize(originalAttachmentSize);
            messageValidator.setSuccessNumber(sendMessageNumberSplit);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return messageValidator;

    }

    /**
     * 表单输入框手机号
     * @param messageValidator
     * @param smocProperties
     * @param org
     * @return
     */
    public static MessageWebTaskInfoValidator FileSMSInput(MessageWebTaskInfoValidator messageValidator, MessageProperties smocProperties, String org) {
        BufferedReader reader = null;

        try {
            //发送内容文件路径
            String fileSendPath = null;
            //异常发送内容文件路径
            String fileError = null;
            //未拆分的总条数
            Integer sendMessageNumber = 0;
            //拆分后的总条数
            Integer sendMessageNumberSplit = 0;
            //上传文件字节数，解析后更新保存数据库
            Long originalAttachmentSize = 0l;

            //经过去重，校验手机号格式，变量参数，黑名单后得到的待发送号码集合
            HashSet<String> validNumbers = new HashSet<String>();
            //不符合手机号格式的异常号码集合
            HashSet<String> errorFormatNumbers = new HashSet<String>();

            //重复号码集合
            HashSet<String> repeatNumbers = new HashSet<String>();

            //解析表单提交的文本手机号码数据
            String inputMobile = messageValidator.getInputNumber();
            if (!StringUtils.isEmpty(inputMobile)) {
                //替换中文逗号
                inputMobile = inputMobile.replaceAll("，", ",");
                for (String tmp : inputMobile.split(",")) {
                    if(StringUtils.isEmpty(tmp)){
                        continue;
                    }

                    if(!Utils.isPhone(tmp)){
                        errorFormatNumbers.add(tmp);
                    }else if(validNumbers.contains(tmp)){
                        repeatNumbers.add(tmp);
                    }else{
                        validNumbers.add(tmp);
                    }
                }
            }

            String nowDay = DateTimeUtils.currentDate(new Date());
            String uuid = UUID.uuid32();

            //生成待发送号码文件
            if(!validNumbers.isEmpty()){
                File targetFolder = new File(smocProperties.getMobileFileRootPath()+"/" + nowDay + "/" + org);
                if(!targetFolder.exists()){
                    targetFolder.mkdirs();
                }
                fileSendPath = "/" + nowDay + "/" + org + "/" + uuid + "_send.txt";

                sendMessageNumber = validNumbers.size();
                sendMessageNumberSplit = countSendNumber(messageValidator.getMessageContent())*sendMessageNumber;
                FileUtils.writeLines(new File(smocProperties.getMobileFileRootPath() + fileSendPath), validNumbers);
            }

            //生成异常号码文件
            if(!errorFormatNumbers.isEmpty()||!repeatNumbers.isEmpty()){
                File targetFolder = new File(smocProperties.getMobileFileRootPath()+"/" + nowDay + "/" + org);
                if(!targetFolder.exists()){
                    targetFolder.mkdirs();
                }
                fileError = "/" + nowDay + "/" + org + "/" + uuid + "_error.txt";

                List<String> errorContents = new ArrayList<String>();
                for(String tmp:errorFormatNumbers){
                    errorContents.add(tmp+"(手机号码不符合规范)");
                }

                for(String tmp:repeatNumbers){
                    errorContents.add(tmp+"(重复号码)");
                }

                FileUtils.writeLines(new File(smocProperties.getMobileFileRootPath() + fileError), errorContents);
            }

            messageValidator.setSendNumberAttachment(fileSendPath);
            messageValidator.setExceptionNumberAttachment(fileError);
            messageValidator.setSubmitNumber(sendMessageNumber);
            messageValidator.setNumberAttachmentSize(originalAttachmentSize);
            messageValidator.setSuccessNumber(sendMessageNumberSplit);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return messageValidator;

    }

    public static MessageWebTaskInfoValidator handleFileVariableSMS(MessageWebTaskInfoValidator messageValidator, MessageProperties smocProperties, String organization) {

        BufferedReader reader = null;

        try {
            //发送内容文件路径
            String fileSendPath = null;
            //异常发送内容文件路径
            String fileError = null;
            //未拆分的总条数
            Integer sendMessageNumber = 0;
            //拆分后的总条数
            Integer sendMessageNumberSplit = 0;
            //上传文件字节数，解析后更新保存数据库
            Long originalAttachmentSize = 0l;

            //经过去重，校验手机号格式，变量参数，黑名单后得到的待发送号码集合
            HashSet<String> validNumbers = new HashSet<String>();
            //不符合手机号格式的异常号码集合
            HashSet<String> errorFormatNumbers = new HashSet<String>();
            //变量短信，参数不足的异常号码集合
            HashSet<String> errorParamNumbers = new HashSet<String>();
            //重复号码集合
            HashSet<String> repeatNumbers = new HashSet<String>();

            //解析表单上传的手机号码文件
            String fileSource = messageValidator.getNumberFiles();
            if (!StringUtils.isEmpty(fileSource)) {
                File originalFile = new File(smocProperties.getMobileFileRootPath() + fileSource);
                originalAttachmentSize = originalFile.length();
                reader = new BufferedReader(new FileReader(originalFile));
                String tmp = null;
                while ((tmp = reader.readLine()) != null) {
                    if(StringUtils.isEmpty(tmp)){
                        continue;
                    }

                    if(validNumbers.contains(tmp)){
                        repeatNumbers.add(tmp);
                    }else{
                        validNumbers.add(tmp);
                    }
                }
            }

            //校验手机号码格式，校验参数个数
            Iterator<String> it = validNumbers.iterator();
            while(it.hasNext()) {
                String tmp = it.next();
                if (!Utils.isPhone(tmp.split("\\|")[0])) {
                    errorFormatNumbers.add(tmp);
                    it.remove();
                } else if(formatContent(tmp, messageValidator.getMessageContent())==null){
                    errorParamNumbers.add(tmp);
                    it.remove();
                }
            }

            //生成待发送号码文件
            if(!validNumbers.isEmpty()){
                if(!StringUtils.isEmpty(fileSource)){
                    fileSendPath = fileSource.replace("_source", "_send");
                }

                sendMessageNumber = validNumbers.size();
                List<String> contents = new ArrayList<>(validNumbers.size());
                for(String tmp:validNumbers){
                    String sendNumber = tmp.split("\\|")[0];
                    String sendContent = formatContent(tmp, messageValidator.getMessageContent());
                    sendMessageNumberSplit += countSendNumber(sendContent);

                    if(sendContent!=null&&sendContent.startsWith(sendNumber)){
                        contents.add(sendContent);
                    }else{
                        contents.add(new StringBuilder(sendNumber).append(",").append(sendContent).toString());
                    }
                }

                FileUtils.writeLines(new File(smocProperties.getMobileFileRootPath() + fileSendPath), contents);
            }

            //生成异常号码文件
            if(!errorFormatNumbers.isEmpty()||!repeatNumbers.isEmpty()){
                if(!StringUtils.isEmpty(fileSource)){
                    fileError = fileSource.replace("_source", "_error");
                }

                List<String> errorContents = new ArrayList<String>();
                for(String tmp:errorFormatNumbers){
                    errorContents.add(tmp+"(手机号码不符合规范)");
                }
                for(String tmp:errorParamNumbers){
                    errorContents.add(tmp+"(变量参数不够)");
                }
                for(String tmp:repeatNumbers){
                    errorContents.add(tmp+"(重复号码)");
                }

                FileUtils.writeLines(new File(smocProperties.getMobileFileRootPath() + fileError), errorContents);
            }

            messageValidator.setSendNumberAttachment(fileSendPath);
            messageValidator.setExceptionNumberAttachment(fileError);
            messageValidator.setSubmitNumber(sendMessageNumber);
            messageValidator.setNumberAttachmentSize(originalAttachmentSize);
            messageValidator.setSuccessNumber(sendMessageNumberSplit);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return messageValidator;
    }

    /**
     * 变量短信合成发送内容
     */
    private static String formatContent(String inputMobile, String templateContent){
        //如果没有传模版数据，则直接返回输入号码内容
        if(StringUtils.isEmpty(templateContent)){
            return inputMobile;
        }

        String[] inputInfo = inputMobile.split("\\|");
        for(int i=1;i<inputInfo.length;i++){
            String replaceKey = "\\$\\{"+i+"\\}";
            templateContent = templateContent.replaceAll(replaceKey, inputInfo[i]);
        }
        //说明提交的参数不够，没有将模版中的变量替换完
        if(templateContent.indexOf("${")!=-1){
            return null;
        }

        return templateContent;
    }

    /**
     * 计算拆分条数
     */
    private static int countSendNumber(String content){
        if(StringUtils.isEmpty(content)){
            return 0;
        }else{
            if(content.length()<=70){
                return 1;
            }else{
                Double d = Math.ceil(new Float(content.length())/67);
                return d.intValue();
            }
        }
    }

    /**
     * 多媒体-输入框输入的手机号
     * @param messageValidator
     * @param smocProperties
     * @param org
     * @return
     */
    public static MessageWebTaskInfoValidator mmFileSMSInput(MessageWebTaskInfoValidator messageValidator, MessageProperties smocProperties, ResourceProperties resourceProperties, String org) {
        BufferedReader reader = null;

        String title = messageValidator.getMessageContent();
        String mulContent = messageValidator.getMultimediaAttachmentData();

        //新增时，将模板标题加入彩信内容中，修改任务时，不用再重复处理模版标题
        if(mulContent.startsWith("[")){
            JSONObject mulContentJSON = new JSONObject();
            JSONArray mulContentArray = JSONObject.parseArray(mulContent);
            mulContentJSON.put("templateTitle", title);
            mulContentJSON.put("mulContent", mulContentArray);
            messageValidator.setMultimediaAttachmentData(mulContentJSON.toJSONString());
        }

        try {
            //发送内容文件路径
            String fileSendPath = null;
            //异常发送内容文件路径
            String fileError = null;
            //未拆分的总条数
            Integer sendMessageNumber = 0;
            //拆分后的总条数
            Integer sendMessageNumberSplit = 0;
            //上传文件字节数，解析后更新保存数据库
            Long originalAttachmentSize = 0l;

            //经过去重，校验手机号格式，变量参数，黑名单后得到的待发送号码集合
            HashSet<String> validNumbers = new HashSet<String>();
            //不符合手机号格式的异常号码集合
            HashSet<String> errorFormatNumbers = new HashSet<String>();

            //重复号码集合
            HashSet<String> repeatNumbers = new HashSet<String>();

            //解析表单提交的文本手机号码数据
            String inputMobile = messageValidator.getInputNumber();
            if (!StringUtils.isEmpty(inputMobile)) {
                //替换中文逗号
                inputMobile = inputMobile.replaceAll("，", ",");
                for (String tmp : inputMobile.split(",")) {
                    if(StringUtils.isEmpty(tmp)){
                        continue;
                    }

                    if(!Utils.isPhone(tmp)){
                        errorFormatNumbers.add(tmp);
                    }else if(validNumbers.contains(tmp)){
                        repeatNumbers.add(tmp);
                    }else{
                        validNumbers.add(tmp);
                    }
                }
            }

            String nowDay = DateTimeUtils.currentDate(new Date());
            String uuid = UUID.uuid32();

            //生成压缩包
            String zipTargetFolder = "/" + nowDay + "/" + org + "/" + messageValidator.getId();
            String zipPath = makeZip(zipTargetFolder, messageValidator.getMultimediaAttachmentData(), messageValidator.getMessageContent(),smocProperties,resourceProperties);
            if(StringUtils.isEmpty(zipPath)){
                log.info("[短信群发]数据:{}", "zip文件生成失败");
            }

            //生成待发送号码文件
            if(!validNumbers.isEmpty()){
                File targetFolder = new File(smocProperties.getMobileFileRootPath()+"/" + nowDay + "/" + org);
                if(!targetFolder.exists()){
                    targetFolder.mkdirs();
                }
                fileSendPath = "/" + nowDay + "/" + org + "/" + uuid + "_send.txt";

                sendMessageNumber = validNumbers.size();
                sendMessageNumberSplit = countSendNumber(messageValidator.getMessageContent())*sendMessageNumber;
                FileUtils.writeLines(new File(smocProperties.getMobileFileRootPath() + fileSendPath), validNumbers);
            }

            //生成异常号码文件
            if(!errorFormatNumbers.isEmpty()||!repeatNumbers.isEmpty()){
                File targetFolder = new File(smocProperties.getMobileFileRootPath()+"/" + nowDay + "/" + org);
                if(!targetFolder.exists()){
                    targetFolder.mkdirs();
                }
                fileError = "/" + nowDay + "/" + org + "/" + uuid + "_error.txt";

                List<String> errorContents = new ArrayList<String>();
                for(String tmp:errorFormatNumbers){
                    errorContents.add(tmp+"(手机号码不符合规范)");
                }

                for(String tmp:repeatNumbers){
                    errorContents.add(tmp+"(重复号码)");
                }

                FileUtils.writeLines(new File(smocProperties.getMobileFileRootPath() + fileError), errorContents);
            }

            messageValidator.setSendNumberAttachment(fileSendPath);
            messageValidator.setExceptionNumberAttachment(fileError);
            messageValidator.setSubmitNumber(sendMessageNumber);
            messageValidator.setNumberAttachmentSize(originalAttachmentSize);
            messageValidator.setSuccessNumber(sendMessageNumberSplit);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return messageValidator;
    }

    /**
     * 多媒体-上传的手机号
     * @param messageValidator
     * @param smocProperties
     * @param org
     * @return
     */
    public static MessageWebTaskInfoValidator mmHandleFileSMS(MessageWebTaskInfoValidator messageValidator, MessageProperties smocProperties, ResourceProperties resourceProperties, String org) {
        BufferedReader reader = null;

        String title = messageValidator.getMessageContent();
        String mulContent = messageValidator.getMultimediaAttachmentData();

        //新增时，将模板标题加入彩信内容中，修改任务时，不用再重复处理模版标题
        if(mulContent.startsWith("[")){
            JSONObject mulContentJSON = new JSONObject();
            JSONArray mulContentArray = JSONObject.parseArray(mulContent);
            mulContentJSON.put("templateTitle", title);
            mulContentJSON.put("mulContent", mulContentArray);
            messageValidator.setMultimediaAttachmentData(mulContentJSON.toJSONString());
        }

        try {
            //发送内容文件路径
            String fileSendPath = null;
            //异常发送内容文件路径
            String fileError = null;
            //未拆分的总条数
            Integer sendMessageNumber = 0;
            //拆分后的总条数
            Integer sendMessageNumberSplit = 0;
            //上传文件字节数，解析后更新保存数据库
            Long originalAttachmentSize = 0l;

            //经过去重，校验手机号格式，变量参数，黑名单后得到的待发送号码集合
            HashSet<String> validNumbers = new HashSet<String>();
            //不符合手机号格式的异常号码集合
            HashSet<String> errorFormatNumbers = new HashSet<String>();

            //重复号码集合
            HashSet<String> repeatNumbers = new HashSet<String>();

            //解析表单上传的手机号码文件
            String fileSource = messageValidator.getNumberFiles();
            if (!StringUtils.isEmpty(fileSource)) {
                File originalFile = new File(smocProperties.getMobileFileRootPath() + fileSource);
                originalAttachmentSize = originalFile.length();
                reader = new BufferedReader(new FileReader(originalFile));
                String tmp = null;
                while ((tmp = reader.readLine()) != null) {
                    if (StringUtils.isEmpty(tmp)) {
                        continue;
                    }
                    if (!Utils.isPhone(tmp)) {
                        errorFormatNumbers.add(tmp);
                    }else {
                        validNumbers.add(tmp);
                    }
                }
            }

            String nowDay = DateTimeUtils.currentDate(new Date());

            //生成压缩包
            String zipTargetFolder = "/" + nowDay + "/" + org + "/" + messageValidator.getId();
            String zipPath = makeZip(zipTargetFolder, messageValidator.getMultimediaAttachmentData(), messageValidator.getMessageContent(),smocProperties,resourceProperties);
            if(StringUtils.isEmpty(zipPath)){
                log.info("[短信群发]数据:{}", "zip文件生成失败");
            }

            //生成待发送号码文件
            if(!validNumbers.isEmpty()){
                if(!StringUtils.isEmpty(fileSource)){
                    fileSendPath = fileSource.replace("_source", "_send");
                }

                sendMessageNumber = validNumbers.size();
                sendMessageNumberSplit = countSendNumber(messageValidator.getMessageContent())*sendMessageNumber;
                FileUtils.writeLines(new File(smocProperties.getMobileFileRootPath() + fileSendPath), validNumbers);
            }

            //生成异常号码文件
            if(!errorFormatNumbers.isEmpty()||!repeatNumbers.isEmpty()){
                if(!StringUtils.isEmpty(fileSource)){
                    fileError = fileSource.replace("_source", "_error");
                }

                List<String> errorContents = new ArrayList<String>();
                for(String tmp:errorFormatNumbers){
                    errorContents.add(tmp+"(手机号码不符合规范)");
                }

                for(String tmp:repeatNumbers){
                    errorContents.add(tmp+"(重复号码)");
                }

                FileUtils.writeLines(new File(smocProperties.getMobileFileRootPath() + fileError), errorContents);
            }

            messageValidator.setSendNumberAttachment(fileSendPath);
            messageValidator.setExceptionNumberAttachment(fileError);
            messageValidator.setSubmitNumber(sendMessageNumber);
            messageValidator.setNumberAttachmentSize(originalAttachmentSize);
            messageValidator.setSuccessNumber(sendMessageNumberSplit);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return messageValidator;
    }

    /**
     * 多媒体-变量短信
     * @param messageValidator
     * @param smocProperties
     * @param org
     * @return
     */
    public static MessageWebTaskInfoValidator mmHandleFileVariableSMS(MessageWebTaskInfoValidator messageValidator, MessageProperties smocProperties, ResourceProperties resourceProperties, String org) {
        BufferedReader reader = null;

        String title = messageValidator.getMessageContent();
        String mulContent = messageValidator.getMultimediaAttachmentData();

        //新增时，将模板标题加入彩信内容中，修改任务时，不用再重复处理模版标题
        if(mulContent.startsWith("[")){
            JSONObject mulContentJSON = new JSONObject();
            JSONArray mulContentArray = JSONObject.parseArray(mulContent);
            mulContentJSON.put("templateTitle", title);
            mulContentJSON.put("mulContent", mulContentArray);
            messageValidator.setMultimediaAttachmentData(mulContentJSON.toJSONString());
        }

        try {
            //发送内容文件路径
            String fileSendPath = null;
            //异常发送内容文件路径
            String fileError = null;
            //未拆分的总条数
            Integer sendMessageNumber = 0;
            //拆分后的总条数
            Integer sendMessageNumberSplit = 0;
            //上传文件字节数，解析后更新保存数据库
            Long originalAttachmentSize = 0l;

            //经过去重，校验手机号格式，变量参数，黑名单后得到的待发送号码集合
            HashSet<String> validNumbers = new HashSet<String>();
            //不符合手机号格式的异常号码集合
            HashSet<String> errorFormatNumbers = new HashSet<String>();
            //变量短信，参数不足的异常号码集合
            HashSet<String> errorParamNumbers = new HashSet<String>();
            //重复号码集合
            HashSet<String> repeatNumbers = new HashSet<String>();

            //解析表单上传的手机号码文件
            String fileSource = messageValidator.getNumberFiles();
            if (!StringUtils.isEmpty(fileSource)) {
                File originalFile = new File(smocProperties.getMobileFileRootPath() + fileSource);
                originalAttachmentSize = originalFile.length();
                reader = new BufferedReader(new FileReader(originalFile));
                String tmp = null;
                while ((tmp = reader.readLine()) != null) {
                    if(StringUtils.isEmpty(tmp)){
                        continue;
                    }

                    if(validNumbers.contains(tmp)){
                        repeatNumbers.add(tmp);
                    }else{
                        validNumbers.add(tmp);
                    }
                }
            }

            //校验手机号码格式，校验参数个数
            Iterator<String> it = validNumbers.iterator();
            while(it.hasNext()) {
                String tmp = it.next();
                if (!Utils.isPhone(tmp.split("\\|")[0])) {
                    errorFormatNumbers.add(tmp);
                    it.remove();
                } else if(formatContent(tmp, messageValidator.getMessageContent())==null){
                    errorParamNumbers.add(tmp);
                    it.remove();
                }
            }

            String nowDay = DateTimeUtils.currentDate(new Date());

            //批量生成压缩包,并将zip文件路径和手机号拼装
            List<String> contents = new ArrayList<String>(validNumbers.size());
            for(String tmp:validNumbers){
                String uuidTmp = UUID.uuid32();
                String zipTargetFolder = "/" + nowDay + "/" + org + "/" + uuidTmp;
                String setResult = formatContent(tmp, messageValidator.getMultimediaAttachmentData());
                if(!StringUtils.isEmpty(setResult)){
                    String zipPathTmp = makeZip(zipTargetFolder, setResult, messageValidator.getMessageContent(),smocProperties,resourceProperties);
                    contents.add(new StringBuilder(tmp.split("\\|")[0]).append(",").append(zipPathTmp).toString());
                }

            }
            sendMessageNumber = contents.size();

            //生成待发送号码文件
            if(!validNumbers.isEmpty()){
                if(!StringUtils.isEmpty(fileSource)){
                    fileSendPath = fileSource.replace("_source", "_send");
                }

                FileUtils.writeLines(new File(smocProperties.getMobileFileRootPath() + fileSendPath), contents);
            }

            //生成异常号码文件
            if(!errorFormatNumbers.isEmpty()||!repeatNumbers.isEmpty()){
                if(!StringUtils.isEmpty(fileSource)){
                    fileError = fileSource.replace("_source", "_error");
                }

                List<String> errorContents = new ArrayList<String>();
                for(String tmp:errorFormatNumbers){
                    errorContents.add(tmp+"(手机号码不符合规范)");
                }
                for(String tmp:errorParamNumbers){
                    errorContents.add(tmp+"(变量参数不够)");
                }
                for(String tmp:repeatNumbers){
                    errorContents.add(tmp+"(重复号码)");
                }

                FileUtils.writeLines(new File(smocProperties.getMobileFileRootPath() + fileError), errorContents);
            }

            messageValidator.setSendNumberAttachment(fileSendPath);
            messageValidator.setExceptionNumberAttachment(fileError);
            messageValidator.setSubmitNumber(sendMessageNumber);
            messageValidator.setNumberAttachmentSize(originalAttachmentSize);
            messageValidator.setSuccessNumber(sendMessageNumber);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return messageValidator;
    }

    /**
     * 生成压缩包并返回压缩包路径
     */
    private static String makeZip(String zipTargetFolder, String multimediaAttachmentData, String title, MessageProperties smocProperties,ResourceProperties resourceProperties) throws Exception {
        if(StringUtils.isEmpty(zipTargetFolder)||StringUtils.isEmpty(multimediaAttachmentData)){
            return "";
        }

        JSONObject mulContentJSON = JSONObject.parseObject(multimediaAttachmentData);
        JSONArray mulContentArray = mulContentJSON.getJSONArray("mulContent");
        String mulContent = mulContentArray.toJSONString();

        List<MessageFrameParamers> paramsSort = new Gson().fromJson(mulContent, new TypeToken<List<MessageFrameParamers>>() {}.getType());

        //生成彩信包文件夹
        File zipFolder = new File(smocProperties.getMobileZipRootPath() + zipTargetFolder);
        zipFolder.mkdirs();

        //按次序将资源文件或者文本内容，拷贝或生成到彩信包文件夹内，同时生成SmilBodyPar数组，为创建smil文件做准备
        int indexInZip = 1;
        List<SmilBodyPar> pars = new ArrayList<SmilBodyPar>();
        for(MessageFrameParamers p:paramsSort){
            SmilBodyParImg smilBodyParImg = null;
            SmilBodyParTxt smilBodyParTxt = null;

            String resType = p.getResType();
            //图片等多媒体文件拷贝
            if(!"0".equals(resType)){
                String sourcePath = resourceProperties.getResourceFileRootPath() + p.getResUrl();
                String suffixName = p.getResUrl().split("\\.")[1];
                String targerPath = smocProperties.getMobileZipRootPath() + zipTargetFolder + "/" + indexInZip + "." + suffixName;
                FileUtils.copyFile(new File(sourcePath), new File(targerPath));

                smilBodyParImg = new SmilBodyParImg();
                smilBodyParImg.setRegion("Image");
                smilBodyParImg.setSrc(indexInZip+"."+suffixName);

                indexInZip++;
            }

            //根据文本内容生成txt文件
            if(!StringUtils.isEmpty(p.getFrameTxt())){
                String targerPath = smocProperties.getMobileZipRootPath() + zipTargetFolder + "/" + indexInZip + ".txt";
                FileUtils.writeStringToFile(new File(targerPath), p.getFrameTxt());

                smilBodyParTxt = new SmilBodyParTxt();
                smilBodyParTxt.setRegion("Text");
                smilBodyParTxt.setSrc(indexInZip+".txt");

                indexInZip++;
            }

            //设置帧停留属性值
            SmilBodyPar par = new SmilBodyPar(smilBodyParImg, smilBodyParTxt);
            par.setDur(p.getStayTimes()+"000ms");
            pars.add(par);
        }

        //生成彩信包标题文件
        String targerPath_title = smocProperties.getMobileZipRootPath() + zipTargetFolder + "/title.txt";
        FileUtils.writeStringToFile(new File(targerPath_title), title);

        //生成彩信包smil文件
        String targerPath_mms = smocProperties.getMobileZipRootPath() + zipTargetFolder + "/mms.smil";
        FileUtils.writeStringToFile(new File(targerPath_mms), formatSmil(pars));

        //生成彩信压缩包
        ZipUtils.toZip(smocProperties.getMobileZipRootPath() + zipTargetFolder, smocProperties.getMobileZipRootPath() + zipTargetFolder + ".zip", false);

        //彩信压缩包生成后，删除彩信包文件夹
        FileUtils.deleteQuietly(zipFolder);

        return smocProperties.getMobileZipRootPath() + zipTargetFolder + ".zip";
    }

    /**
     * 构建smil对象并转成XML字符串
     */
    private static String formatSmil(List<SmilBodyPar> pars) throws Exception {
        SmilHeadLayoutRootLayoutHeadRegion r1 = new SmilHeadLayoutRootLayoutHeadRegion();
        r1.setId("Image");
        r1.setTop("0");
        r1.setLeft("0");
        r1.setHeight("50");
        r1.setWidth("100");
        r1.setFit("hidden");
        SmilHeadLayoutRootLayoutHeadRegion r2 = new SmilHeadLayoutRootLayoutHeadRegion();
        r2.setId("Text");
        r2.setTop("50");
        r2.setLeft("0");
        r2.setHeight("50");
        r2.setWidth("100");
        r2.setFit("hidden");
        List<SmilHeadLayoutRootLayoutHeadRegion> regions = new ArrayList<SmilHeadLayoutRootLayoutHeadRegion>();
        regions.add(r1);
        regions.add(r2);

        SmilHeadLayoutRootLayout rootLayout = new SmilHeadLayoutRootLayout(regions);
        rootLayout.setHeight("208");
        rootLayout.setWidth("176");

        SmilHeadLayout headLayout = new SmilHeadLayout(rootLayout);
        SmilHead smilHead = new SmilHead(headLayout);

        SmilBody body = new SmilBody(pars);
        Smil smilInfo = new Smil(smilHead, body);
        smilInfo.setXmlns("http://www.w3.org/2000/SMIL20/CR/Language");
        return XMLUtils.convertToXml(smilInfo);
    }



}
