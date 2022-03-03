package com.smoc.cloud.complaint.service;


import com.alibaba.fastjson.JSON;
import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.response.ResponseData;
import com.smoc.cloud.common.response.ResponseDataUtil;
import com.smoc.cloud.common.smoc.message.MessageComplaintInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.model.ComplaintExcelModel;
import com.smoc.cloud.common.smoc.utils.ChannelUtils;
import com.smoc.cloud.common.smoc.utils.SysFilterUtil;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.complaint.entity.MessageComplaintInfo;
import com.smoc.cloud.complaint.repository.ComplaintRepository;
import com.smoc.cloud.filter.entity.FilterGroupList;
import com.smoc.cloud.filter.repository.BlackRepository;
import com.smoc.cloud.filter.repository.GroupRepository;
import com.smoc.cloud.message.entity.MessageDetailInfo;
import com.smoc.cloud.message.repository.MessageDetailInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
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

    @Resource
    private BlackRepository blackRepository;

    @Resource
    private GroupRepository groupRepository;

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
        if(data.isPresent()){
            MessageComplaintInfo entity = data.get();
            entity.setBusinessAccount(messageComplaintInfoValidator.getBusinessAccount());
            entity.setIs12321(messageComplaintInfoValidator.getIs12321());
            entity.setNumberCode(messageComplaintInfoValidator.getNumberCode());
            entity.setSendDate(messageComplaintInfoValidator.getSendDate());
            entity.setSendRate(messageComplaintInfoValidator.getSendRate());
            entity.setBusinessType(messageComplaintInfoValidator.getBusinessType());

            //op 不为 edit 或 add
            if (!("edit".equals(op) || "add".equals(op))) {
                return ResponseDataUtil.buildError();
            }

            //记录日志
            log.info("[投诉管理][{}]数据:{}", op, JSON.toJSONString(entity));
            complaintRepository.saveAndFlush(entity);
        }

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

        /**
         * 每日投诉需要查业务账号
         * 根据投诉手机号、投诉内容、投诉运营商查询业务账号、码号、下发时间、下发频次
         */
        if("day".equals(messageComplaintInfoValidator.getComplaintSource())){
            for(ComplaintExcelModel info:list){
                //查询日志表
                List<MessageDetailInfo> messageList =  messageDetailInfoRepository.findByCarrierAndPhoneNumberAndMessageContent(messageComplaintInfoValidator.getCarrier(),info.getReportNumber(),info.getReportContent());
                if(!StringUtils.isEmpty(messageList) && messageList.size()>0){
                    MessageDetailInfo message =  messageList.get(0);
                    info.setBusinessAccount(message.getBusinessAccount());
                    info.setSendDate(message.getSendTime());
                    info.setChannelId(message.getChannelId());
                    info.setNumberCode(message.getNumberCode());


                    //查询30天内下发频次
                    MessageDetailInfoValidator validator = new MessageDetailInfoValidator();
                    validator.setPhoneNumber(info.getReportNumber());
                    Date startDate = DateTimeUtils.dateAddDays(new Date(),-30);
                    validator.setStartDate(DateTimeUtils.getDateFormat(startDate));
                    validator.setEndDate(DateTimeUtils.getDateFormat(new Date()));
                    int number = messageDetailInfoRepository.statisticMessageNumber(validator);
                    info.setSendRate(""+number);
                }
                //查询
                MessageComplaintInfo messageComplaintInfo = complaintRepository.findByCarrierSourceAndReportNumberAndReportContentAndReportDate(messageComplaintInfoValidator.getCarrier(),info.getReportNumber(),info.getReportContent(),info.getReportDate());
                if(!StringUtils.isEmpty(messageComplaintInfo)){
                    //记录日志
                    log.info("[投诉管理][delete]数据:{}",JSON.toJSONString(messageComplaintInfo));
                    complaintRepository.deleteById(messageComplaintInfo.getId());
                }
            }
        }

        complaintRepository.batchSave(messageComplaintInfoValidator);

        /**
         * 加入黑名单
         */
        saveBlack(messageComplaintInfoValidator);

        return ResponseDataUtil.buildSuccess();

    }

    /**
     * 导入黑名单
     * @param messageComplaintInfoValidator
     */
    private void saveBlack(MessageComplaintInfoValidator messageComplaintInfoValidator) {

        //每日投诉
        if("day".equals(messageComplaintInfoValidator.getComplaintSource())){
            //查询是否有每日投诉群组
            Optional<FilterGroupList> optional = groupRepository.findById(SysFilterUtil.GROUP_COMPLAINT_ID);
            if(!optional.isPresent()){
                FilterGroupList filterGroupList = new FilterGroupList();
                filterGroupList.setId(SysFilterUtil.GROUP_COMPLAINT_ID);
                filterGroupList.setEnterpriseId("smoc_black");
                filterGroupList.setGroupId(SysFilterUtil.GROUP_COMPLAINT_ID);
                filterGroupList.setGroupName(SysFilterUtil.GROUP_COMPLAINT_NAME);
                filterGroupList.setParentId("root");
                filterGroupList.setIsLeaf("1");
                filterGroupList.setStatus("1");
                filterGroupList.setSort(0);
                filterGroupList.setCreatedTime(new Date());
                filterGroupList.setCreatedBy(messageComplaintInfoValidator.getCreatedBy());
                groupRepository.saveAndFlush(filterGroupList);

                //记录日志
                log.info("[群组管理][每日投诉群组][{}]数据:{}","add",JSON.toJSONString(filterGroupList));
            }

            //导入黑名单
            blackRepository.complaintBathSave(messageComplaintInfoValidator,SysFilterUtil.GROUP_COMPLAINT_ID);

        }

        //12321投诉
        if("12321".equals(messageComplaintInfoValidator.getComplaintSource())){
            //查询是否有12321投诉群组
            Optional<FilterGroupList> optional = groupRepository.findById(SysFilterUtil.GROUP_12321_ID);
            if(!optional.isPresent()){
                FilterGroupList filterGroupList = new FilterGroupList();
                filterGroupList.setId(SysFilterUtil.GROUP_12321_ID);
                filterGroupList.setEnterpriseId("smoc_black");
                filterGroupList.setGroupId(SysFilterUtil.GROUP_12321_ID);
                filterGroupList.setGroupName(SysFilterUtil.GROUP_12321_NAME);
                filterGroupList.setParentId("root");
                filterGroupList.setIsLeaf("1");
                filterGroupList.setStatus("1");
                filterGroupList.setSort(0);
                filterGroupList.setCreatedTime(new Date());
                filterGroupList.setCreatedBy(messageComplaintInfoValidator.getCreatedBy());
                groupRepository.saveAndFlush(filterGroupList);

                //记录日志
                log.info("[群组管理][12321投诉群组][{}]数据:{}","add",JSON.toJSONString(filterGroupList));
            }

            //导入黑名单
            blackRepository.complaintBathSave(messageComplaintInfoValidator,SysFilterUtil.GROUP_12321_ID);

        }

    }
}
