package com.smoc.cloud.configure.channel.repository;

import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelPriceHistoryValidator;
import com.smoc.cloud.configure.channel.entity.ConfigChannelPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigChannelPriceHistoryRepository  extends JpaRepository<ConfigChannelPriceHistory, String> {

    /**
     * 查询列表
     * @param pageParams
     * @return
     */
    PageList<ConfigChannelPriceHistoryValidator> page(PageParams<ConfigChannelPriceHistoryValidator> pageParams);
}
