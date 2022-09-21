package com.smoc.cloud.complaint.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageChannelComplaintValidator;
import com.smoc.cloud.common.smoc.message.MessageComplaintInfoValidator;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.complaint.entity.MessageComplaintInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * 投诉管理操作类
 */
public interface ComplaintRepository extends CrudRepository<MessageComplaintInfo, String>, JpaRepository<MessageComplaintInfo, String> {


    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    PageList<MessageComplaintInfoValidator> page(PageParams<MessageComplaintInfoValidator> pageParams);

    /**
     * 批量导入投诉
     * @param messageComplaintInfoValidator
     */
    void batchSave(MessageComplaintInfoValidator messageComplaintInfoValidator);

    /**
     * 查询
     * @param carrier
     * @param reportNumber
     * @param reportContent
     * @param reportDate
     * @return
     */
    MessageComplaintInfo findByCarrierSourceAndReportNumberAndReportContentAndReportDate(String carrier, String reportNumber, String reportContent, String reportDate);

    /**
     * 查询通道投诉排行
     * @param messageChannelComplaintValidator
     * @return
     */
    List<MessageChannelComplaintValidator> channelComplaintRanking(MessageChannelComplaintValidator messageChannelComplaintValidator);

    /**
     * 根据投诉手机号查询10天内的下发记录
     * @param detail
     * @return
     */
    List<MessageDetailInfoValidator> sendMessageList(MessageDetailInfoValidator detail);
}
