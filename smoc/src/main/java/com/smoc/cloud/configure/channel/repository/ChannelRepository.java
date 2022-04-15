package com.smoc.cloud.configure.channel.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelAccountInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ChannelInterfaceInfoQo;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticComplaintData;
import com.smoc.cloud.common.smoc.customer.qo.AccountStatisticSendData;
import com.smoc.cloud.configure.channel.entity.ConfigChannelBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 通道操作类
 */
public interface ChannelRepository extends CrudRepository<ConfigChannelBasicInfo, String>, JpaRepository<ConfigChannelBasicInfo, String> {



    PageList<ChannelBasicInfoQo> page(PageParams<ChannelBasicInfoQo> pageParams);

    Iterable<ConfigChannelBasicInfo> findByChannelId(String channelId);

    /**
     * 通道按维度统计发送量
     * @param statisticSendData
     * @return
     */
    List<AccountStatisticSendData> statisticChannelSendNumber(AccountStatisticSendData statisticSendData);

    /**
     *  通道投诉率统计
     * @param statisticComplaintData
     * @return
     */
    List<AccountStatisticComplaintData> statisticComplaintMonth(AccountStatisticComplaintData statisticComplaintData);

    /**
     * 通道账号使用明细
     * @param pageParams
     * @return
     */
    PageList<ChannelAccountInfoQo> channelAccountList(PageParams<ChannelAccountInfoQo> pageParams);

    /**
     * 通道接口参数查询
     * @param pageParams
     * @return
     */
    PageList<ChannelInterfaceInfoQo> channelInterfacePage(PageParams<ChannelInterfaceInfoQo> pageParams);
}
