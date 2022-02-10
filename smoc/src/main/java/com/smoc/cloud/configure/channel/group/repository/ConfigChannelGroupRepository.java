package com.smoc.cloud.configure.channel.group.repository;


import com.smoc.cloud.common.smoc.configuate.qo.ChannelBasicInfoQo;
import com.smoc.cloud.common.smoc.configuate.qo.ConfigChannelGroupQo;
import com.smoc.cloud.configure.channel.group.entity.ConfigChannelGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * 通道组配置操作类
 */
public interface ConfigChannelGroupRepository extends CrudRepository<ConfigChannelGroup, String>, JpaRepository<ConfigChannelGroup, String> {

    /**
     * 查询通道列表
     * @param channelBasicInfoQo
     * @return
     */
    List<ChannelBasicInfoQo> findChannelList(ChannelBasicInfoQo channelBasicInfoQo);

    /**
     * 查询已配置的通道
     * @param configChannelGroupQo
     * @return
     */
    List<ConfigChannelGroupQo> findConfigChannelGroupList(ConfigChannelGroupQo configChannelGroupQo);

    List<ConfigChannelGroup> findByChannelGroupId(String channelGroupId);
}
