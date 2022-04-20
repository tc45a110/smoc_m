package com.smoc.cloud.message.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.smoc.cloud.common.response.ResponseCode;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.smoc.message.model.MessageTemplateExcelModel;
import com.smoc.cloud.common.smoc.template.AccountTemplateInfoValidator;
import com.smoc.cloud.common.utils.Utils;
import com.smoc.cloud.common.validator.MpmValidatorUtil;
import com.smoc.cloud.customer.entity.EnterpriseBookInfo;
import com.smoc.cloud.customer.repository.EnterpriseBookRepository;
import com.smoc.cloud.message.model.MessageTemplateExcelModelListener;
import com.smoc.cloud.message.properties.MessageProperties;
import com.smoc.cloud.message.service.MessageSendWebTaskInfoService;
import com.smoc.cloud.template.service.AccountTemplateInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * web任务单
 */
@Slf4j
@RestController
@RequestMapping("message/web/task")
public class MessageSendWebTaskInfoController {

    @Autowired
    private MessageSendWebTaskInfoService messageSendWebTaskInfoService;

    @Autowired
    private EnterpriseBookRepository enterpriseBookRepository;

    @Autowired
    private MessageProperties messageProperties;


    /**
     *  发送短信
     *
     * @param op 操作标记，add表示添加，edit表示修改
     * @return
     */
    @RequestMapping(value = "/save/{op}", method = RequestMethod.POST)
    public ResponseData save(@RequestBody MessageWebTaskInfoValidator messageWebTaskInfoValidator, @PathVariable String op) {

        //完成参数规则验证
        if (!MpmValidatorUtil.validate(messageWebTaskInfoValidator)) {
            return ResponseDataUtil.buildError(ResponseCode.PARAM_ERROR.getCode(), MpmValidatorUtil.validateMessage(messageWebTaskInfoValidator));
        }

        //解析手机号 1：手动上传 2：附件上传 3：选择通讯录
        List<String> validNumbers = handleMobile(messageWebTaskInfoValidator);
        messageWebTaskInfoValidator.setMobiles(validNumbers);

        //保存操作
        ResponseData data = messageSendWebTaskInfoService.sendMessag(messageWebTaskInfoValidator, op);

        return data;
    }

    //解析手机号
    public List<String> handleMobile(MessageWebTaskInfoValidator messageWebTaskInfoValidator) {

        //手机号集合
        List<String> validNumbers = new ArrayList<>();

        //手动输入号码
        if ("1".equals(messageWebTaskInfoValidator.getUpType())) {
            String inputMobile = messageWebTaskInfoValidator.getInputNumber();
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
        if ("2".equals(messageWebTaskInfoValidator.getUpType())){
            String filePath = messageWebTaskInfoValidator.getNumberFiles();
            File desFile = new File(messageProperties.getMobileFileRootPath() + filePath);

            String fileType = filePath.substring(filePath.lastIndexOf("."));
            if(".xls".equals(fileType) || ".xlsx".equals(fileType)){
                validNumbers = readMessageTemplateExcel(desFile);
            }else if (".txt".equals(fileType)){
                validNumbers = readMessageTemplateTxt(desFile);
            }
        }

        //通讯录
        if ("3".equals(messageWebTaskInfoValidator.getUpType())){
            validNumbers = enterpriseBookRepository.findByGroupIdAndStatus(messageWebTaskInfoValidator.getGroupId(),"1");

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
                if(i>=3){
                    break;
                }
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
