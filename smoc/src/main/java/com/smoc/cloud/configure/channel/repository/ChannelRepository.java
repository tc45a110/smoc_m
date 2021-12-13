package com.smoc.cloud.configure.channel.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelBasicInfoValidator;
import com.smoc.cloud.configure.channel.entity.ConfigChannelBasicInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * 通道操作类
 */
public interface ChannelRepository extends CrudRepository<ConfigChannelBasicInfo, String>, JpaRepository<ConfigChannelBasicInfo, String> {



    PageList<ChannelBasicInfoValidator> page(PageParams<ChannelBasicInfoValidator> pageParams);

    Iterable<ConfigChannelBasicInfo> findByChannelId(String channelId);
}
