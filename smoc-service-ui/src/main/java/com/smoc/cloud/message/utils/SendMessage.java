package com.smoc.cloud.message.utils;


import com.smoc.cloud.common.smoc.template.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.common.utils.UUID;
import com.smoc.cloud.common.utils.Utils;
import com.smoc.cloud.properties.SmocProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SendMessage {


    public static MessageWebTaskInfoValidator handleFileSMS(MessageWebTaskInfoValidator messageValidator,SmocProperties smocProperties,String org) {

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
                if(!StringUtils.isEmpty(fileSource)){
                    fileSendPath = fileSource.replace("_source", "_send");
                }else{
                    File targetFolder = new File(smocProperties.getMobileFileRootPath()+"/" + nowDay + "/" + org);
                    if(!targetFolder.exists()){
                        targetFolder.mkdirs();
                    }
                    fileSendPath = "/" + nowDay + "/" + org + "/" + uuid + "_send.txt";
                }

                sendMessageNumber = validNumbers.size();
                sendMessageNumberSplit = countSendNumber(messageValidator.getMessageContent())*sendMessageNumber;
                FileUtils.writeLines(new File(smocProperties.getMobileFileRootPath() + fileSendPath), validNumbers);
            }

            //生成异常号码文件
            if(!errorFormatNumbers.isEmpty()||!repeatNumbers.isEmpty()){
                if(!StringUtils.isEmpty(fileSource)){
                    fileError = fileSource.replace("_source", "_send");
                }else{
                    File targetFolder = new File(smocProperties.getMobileFileRootPath()+"/" + nowDay + "/" + org);
                    if(!targetFolder.exists()){
                        targetFolder.mkdirs();
                    }
                    fileError = "/" + nowDay + "/" + org + "/" + uuid + "_error.txt";
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
            messageValidator.setSuccessNumber(sendMessageNumber);
            messageValidator.setSendMessageNumber(sendMessageNumberSplit);
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
