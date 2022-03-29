package com.smoc.cloud.message.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.message.MessageDailyStatisticValidator;
import com.smoc.cloud.common.smoc.message.MessageMoInfoValidator;
import com.smoc.cloud.message.entity.MessageDailyStatistic;
import com.smoc.cloud.message.entity.MessageMoInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

/**
 * 短信上行
 */
public interface MessageMoInfoRepository extends JpaRepository<MessageMoInfo, String> {

    /**
     * 分页查询
     * @param pageParams
     * @return
     */
    PageList<MessageMoInfoValidator> page(PageParams<MessageMoInfoValidator> pageParams);

}