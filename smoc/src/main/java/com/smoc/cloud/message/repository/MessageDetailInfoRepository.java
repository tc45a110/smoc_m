package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageDetailInfoValidator;
import com.smoc.cloud.message.entity.MessageDetailInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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
}