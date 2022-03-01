package com.smoc.cloud.complaint.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageComplaintInfoValidator;
import com.smoc.cloud.common.smoc.message.model.ComplaintExcelModel;
import com.smoc.cloud.complaint.entity.MessageComplaintInfo;
import com.smoc.cloud.complaint.repository.ComplaintRepository;
import com.smoc.cloud.customer.entity.EnterpriseChainInfo;
import com.smoc.cloud.message.entity.MessageDetailInfo;
import com.smoc.cloud.message.repository.MessageDetailInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 投诉管理
 **/
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ComplaintService {

    @Resource
    private ComplaintRepository complaintRepository;

    @Resource
    private MessageDetailInfoRepository messageDetailInfoRepository;

    /**
     * 查询列表
     *
     * @param pageParams
     * @return
     */
    public PageList<MessageComplaintInfoValidator> page(PageParams<MessageComplaintInfoValidator> pageParams) {
        return complaintRepository.page(pageParams);
    }

    /**
     * 根据id获取信息
     *
     * @param id
     * @return
     */
    public ResponseData findById(String id) {
        Optional<MessageComplaintInfo> data = complaintRepository.findById(id);
        //使用了 Optional 的判断方式，判断优雅
        return ResponseDataUtil.buildSuccess(data.orElse(null));
    }

    /**
     * 保存或修改
     *
     * @param messageComplaintInfoValidator
     * @param op  操作类型 为add、edit
     * @return
     */
    @Transactional
    public ResponseData<MessageComplaintInfo> save(MessageComplaintInfoValidator messageComplaintInfoValidator, String op) {

        Optional<MessageComplaintInfo> data = complaintRepository.findById(messageComplaintInfoValidator.getId());

        MessageComplaintInfo entity = new MessageComplaintInfo();
        BeanUtils.copyProperties(messageComplaintInfoValidator, entity);

        //op 不为 edit 或 add
        if (!("edit".equals(op) || "add".equals(op))) {
            return ResponseDataUtil.buildError();
        }

        //记录日志
        log.info("[投诉管理][{}]数据:{}", op, JSON.toJSONString(entity));
        complaintRepository.saveAndFlush(entity);

        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Transactional
    public ResponseData<MessageComplaintInfo> deleteById(String id) {

        MessageComplaintInfo data = complaintRepository.findById(id).get();
        //记录日志
        log.info("[投诉管理][delete]数据:{}",JSON.toJSONString(data));
        complaintRepository.deleteById(id);


        return ResponseDataUtil.buildSuccess();
    }

    /**
     * 批量导入投诉
     * @param messageComplaintInfoValidator
     * @return
     */
    @Async
    public ResponseData batchSave(MessageComplaintInfoValidator messageComplaintInfoValidator) {


        List<ComplaintExcelModel> list = messageComplaintInfoValidator.getComplaintList();

        //根据投诉手机号、投诉内容、投诉运营商查询业务账号、码号、下发时间、下发频次
        for(ComplaintExcelModel info:list){
            List<MessageDetailInfo> messageList =  messageDetailInfoRepository.findByCarrierAndPhoneNumberAndMessageContent(messageComplaintInfoValidator.getCarrier(),info.getReportNumber(),info.getReportContent());
            if(!StringUtils.isEmpty(messageList) && messageList.size()>0){
                MessageDetailInfo message =  messageList.get(0);
                info.setBusinessAccount(message.getBusinessAccount());
                info.setSendDate(message.getSendTime());
                info.setChannelId(message.getChannelId());
                //info.setSendRate();
            }
        }

        complaintRepository.batchSave(messageComplaintInfoValidator);

        return ResponseDataUtil.buildSuccess();

    }
}
