package com.smoc.cloud.message.utils;


import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.utils.Utils;
import com.smoc.cloud.properties.MessageProperties;
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
            messageValidator.setSuccessNumber(sendMessageNumber);
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
            messageValidator.setSuccessNumber(sendMessageNumber);
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
            messageValidator.setSuccessNumber(sendMessageNumber);
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



}
