package com.smoc.cloud.saler.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.saler.qo.ChannelStatisticSendData;
import com.smoc.cloud.common.smoc.saler.qo.CustomerChannelInfoQo;
import com.smoc.cloud.configure.channel.entity.ConfigChannelBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 通道操作类
 */
public interface SalerChannelRepository extends JpaRepository<ConfigChannelBasicInfo, String> {

    /**
     * 查询-分页
     *
     * @param pageParams
     * @return
     */
    PageList<CustomerChannelInfoQo> page(PageParams<CustomerChannelInfoQo> pageParams);

    /**
     * 单个通道发送量统计按月
     * @param statisticSendData
     * @return
     */
    List<ChannelStatisticSendData> statisticSendNumberMonthByChannel(ChannelStatisticSendData statisticSendData);
}
