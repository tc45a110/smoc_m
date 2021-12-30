package com.smoc.cloud.configure.channel.group.repository;


import com.smoc.cloud.common.page.PageList;
import com.smoc.cloud.common.page.PageParams;
import com.smoc.cloud.common.smoc.configuate.validator.ChannelGroupInfoValidator;
import com.smoc.cloud.configure.channel.group.entity.ConfigChannelGroupInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;



/**
 * 通道组操作类
 */
public interface ChannelGroupRepository extends CrudRepository<ConfigChannelGroupInfo, String>, JpaRepository<ConfigChannelGroupInfo, String> {


    PageList<ChannelGroupInfoValidator> page(PageParams<ChannelGroupInfoValidator> pageParams);

    Iterable<ConfigChannelGroupInfo> findByChannelGroupId(String channelGroupId);

}
