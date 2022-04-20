package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageAccountValidator;
import com.smoc.cloud.common.smoc.message.model.MessageFormat;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSend;
import com.smoc.cloud.common.smoc.message.MessageWebTaskInfoValidator;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSendData;
import com.smoc.cloud.message.entity.MessageWebTaskInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * web任务单
 */
public interface MessageWebTaskInfoRepository extends JpaRepository<MessageWebTaskInfo, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<MessageWebTaskInfoValidator> page(PageParams<MessageWebTaskInfoValidator> pageParams);

    /**
     * 统计发送数量
     *
     * @param qo
     * @return
     */
    Map<String, Object> countSum(MessageWebTaskInfoValidator qo);

    /**
     * 发送短信：更新状态
     * @param id
     */
    @Modifying
    @Query(value = "update message_web_task_info set SEND_STATUS = :sendStatus where ID = :id ",nativeQuery = true)
    void sendMessageById(@Param("id") String id, @Param("sendStatus") String sendStatus);

    /**
     * 查询企业发送量
     * @param messageAccountValidator
     * @return
     */
    StatisticMessageSend statisticMessageSendCount(MessageAccountValidator messageAccountValidator);

    /**
     * 统计短信提交发送量
     * @param messageWebTaskInfoValidator
     * @return
     */
    StatisticMessageSend statisticSubmitMessageSendCount(MessageWebTaskInfoValidator messageWebTaskInfoValidator);

    /**
     * 异步 批量保存 待发短信
     * @param messages
     * @param messageCount
     * @param phoneCount
     */
    void saveMessageBatch(List<MessageFormat> messages, Integer messageCount, Integer phoneCount);
}