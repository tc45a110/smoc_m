package com.smoc.cloud.scheduler.message.processor;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.smoc.cloud.common.smoc.message.model.MessageTemplateExcelModel;
import com.smoc.cloud.common.utils.Utils;
import com.smoc.cloud.scheduler.message.properties.MessageProperties;
import com.smoc.cloud.scheduler.message.service.MessageTimingTaskService;
import com.smoc.cloud.scheduler.message.service.model.MessageTemplateExcelModelListener;
import com.smoc.cloud.scheduler.message.service.model.MessageTimingModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 定时发送短信任务
 */
@Slf4j
@Component
public class MessageTimingTaskProcessor implements ItemProcessor<MessageTimingModel, MessageTimingModel> {

    @Autowired
    private MessageProperties messageProperties;

    @Autowired
    private MessageTimingTaskService messageTimingTaskService;

    @Override
    public MessageTimingModel process(MessageTimingModel messageTimingModel) throws Exception {

        //解析手机号 1：手动上传 2：附件上传 3：选择通讯录
        List<String> mobileList = handleMobile(messageTimingModel);

        if(!StringUtils.isEmpty(mobileList) && mobileList.size()>0){
            messageTimingTaskService.sendMessage(messageTimingModel,mobileList);
        }

        return messageTimingModel;
    }

    public List<String> handleMobile(MessageTimingModel messageTimingModel) {
        //手机号集合
        List<String> validNumbers = new ArrayList<>();

        //手动输入号码
        if ("1".equals(messageTimingModel.getUpType())) {
            String inputMobile = messageTimingModel.getInputMobile();
            if (!StringUtils.isEmpty(inputMobile)) {
                //替换中文逗号
                inputMobile = inputMobile.replaceAll("，", ",");
                for (String tmp : inputMobile.split(",")) {
                    if (StringUtils.isEmpty(tmp)) {
                        continue;
                    }
                    //符合规范
                    if (Utils.isPhone(tmp)) {
                        validNumbers.add(tmp);
                    }
                }
            }
        }

        //上传附件
        if ("2".equals(messageTimingModel.getUpType())){
            String filePath = messageTimingModel.getMobileFile();
            File desFile = new File(messageProperties.getMobileFileRootPath() + filePath);

            String fileType = filePath.substring(filePath.lastIndexOf("."));
            if(".xls".equals(fileType) || ".xlsx".equals(fileType)){
                validNumbers = readMessageTemplateExcel(desFile);
            }else if (".txt".equals(fileType)){
                validNumbers = readMessageTemplateTxt(desFile);
            }
        }

        //通讯录
        if ("3".equals(messageTimingModel.getUpType())){
            validNumbers = messageTimingTaskService.findByGroupIdAndStatus(messageTimingModel.getGroupId(),"1");

        }

        //去重
        if (validNumbers.size() > 0) {
            validNumbers = validNumbers.stream().distinct().collect(Collectors.toList());;
        }

        return validNumbers;
    }

    public List<String> readMessageTemplateExcel(File file) {
        InputStream inputStream = null;
        List<String> list = new ArrayList<String>();
        try {
            inputStream = new FileInputStream(file);
            MessageTemplateExcelModelListener excelModelListener = new MessageTemplateExcelModelListener();
            ExcelReader excelReader = EasyExcel.read(inputStream, MessageTemplateExcelModel.class, excelModelListener).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
            excelReader.finish();
            list = excelModelListener.getExcelModelList();
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> readMessageTemplateTxt(File desFile) {
        BufferedReader br = null;
        List<String> list = new ArrayList<String>();
        try {
            br = new BufferedReader(new FileReader(desFile));
            String tempString = null;
            int i = 0;
            while ((tempString = br.readLine()) != null) {
                if (StringUtils.isEmpty(tempString)) {
                    continue;
                }

                list.add(tempString.trim());
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}
