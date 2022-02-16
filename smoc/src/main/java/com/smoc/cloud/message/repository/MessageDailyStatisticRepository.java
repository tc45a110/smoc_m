package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import com.smoc.cloud.message.entity.MessageDailyStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 短信日统计
 */
public interface MessageDailyStatisticRepository extends JpaRepository<MessageDailyStatistic, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<MessageDailyStatisticValidator> page(PageParams<MessageDailyStatisticValidator> pageParams);
}