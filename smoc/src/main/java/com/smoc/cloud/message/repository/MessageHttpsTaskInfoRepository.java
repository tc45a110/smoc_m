package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageHttpsTaskInfoValidator;
import com.smoc.cloud.common.smoc.message.model.StatisticMessageSendData;
import com.smoc.cloud.message.entity.MessageHttpsTaskInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * HTTPS任务单
 */
public interface MessageHttpsTaskInfoRepository extends JpaRepository<MessageHttpsTaskInfo, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<MessageHttpsTaskInfoValidator> page(PageParams<MessageHttpsTaskInfoValidator> pageParams);

    /**
     * 自服务平台不同维度统计发送量
     * @param pageParams
     * @return
     */
    PageList<StatisticMessageSendData> messageSendNumberList(PageParams<StatisticMessageSendData> pageParams);
}