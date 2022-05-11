package com.smoc.cloud.configure.channel.repository;


import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.common.smoc.configuate.validator.ConfigChannelSpareChannelValidator;
import com.smoc.cloud.configure.channel.entity.ConfigChannelSpareChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 备用通道接口参数操作类
 */
public interface ConfigChannelSpareChannelRepository extends CrudRepository<ConfigChannelSpareChannel, String>, JpaRepository<ConfigChannelSpareChannel, String> {


    Iterable<ConfigChannelSpareChannel> findByChannelIdAndSpareChannelId(String channelId, String spareChannelId);

    /**
     * 根据原通道属性查询符合要求的备用通道
     * @param channelBasicInfoValidator
     * @return
     */
    List<ConfigChannelSpareChannelValidator> findSpareChannel(ChannelBasicInfoValidator channelBasicInfoValidator);

    ConfigChannelSpareChannel findByChannelId(String id);
}
