package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageCodeValidator;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.common.smoc.message.model.MessageTaskDetail;
import com.smoc.cloud.message.entity.MessageDetailInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

/**
 * 短信明细
 */
public interface MessageDetailInfoRepository extends JpaRepository<MessageDetailInfo, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<MessageDetailInfoValidator> page(PageParams<MessageDetailInfoValidator> pageParams);

    /**
     * 根据投诉手机号、投诉内容、投诉运营商查询业务账号
     * @param carrier
     * @param reportNumber
     * @param reportContent
     * @return
     */
    List<MessageDetailInfo> findByCarrierAndPhoneNumberAndMessageContent(String carrier, String reportNumber, String reportContent);

    /**
     * 查询下发频次
     * @param validator
     * @return
     */
    int statisticMessageNumber(MessageDetailInfoValidator validator);

    /**
     * 统计自服务平台短信明细列表
     * @param pageParams
     * @return
     */
    PageList<MessageDetailInfoValidator> servicerPage(PageParams<MessageDetailInfoValidator> pageParams);

    /**
     * 查询自服务web短信明细列表
     * @param pageParams
     * @return
     */
    PageList<MessageTaskDetail> webTaskDetailList(PageParams<MessageTaskDetail> pageParams);

    /**
     * 查询自服务http短信明细列表
     * @param pageParams
     * @return
     */
    PageList<MessageTaskDetail> httpTaskDetailList(PageParams<MessageTaskDetail> pageParams);

    /**
     * 单条短信发送记录
     * @param pageParams
     * @return
     */
    PageList<MessageDetailInfoValidator> sendMessageList(PageParams<MessageDetailInfoValidator> pageParams);

    /**
     * 根据企业实时查询成功发送总量
     * @param messageDetailInfoValidator
     * @return
     */
    Map<String, Object> statisticEnterpriseSendMessage(MessageDetailInfoValidator messageDetailInfoValidator);

    /**
     * 消息状态码统计查询
     * @param params
     * @return
     */
    PageList<MessageCodeValidator> messageCcodeStautsList(PageParams<MessageCodeValidator> params);

    /**
     * 短信发送总量
     * @param messageDetailInfoValidator
     * @return
     */
    Map<String, Object> statisticEnterpriseTotalMessage(MessageDetailInfoValidator messageDetailInfoValidator);

    /**
     * 通道消息明细分页查询
     * @param params
     * @return
     */
    PageList<MessageDetailInfoValidator> messageChannelPage(PageParams<MessageDetailInfoValidator> params);

    /**
     * 统计提交给通道发送总量
     * @param messageDetailInfoValidator
     * @return
     */
    Map<String, Object> statisticChannelSendMessage(MessageDetailInfoValidator messageDetailInfoValidator);
}