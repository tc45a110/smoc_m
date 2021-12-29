package com.smoc.cloud.configure.channel.group.repository;


import com.smoc.cloud.configure.channel.group.entity.ConfigChannelGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * 通道组配置操作类
 */
public interface ChannelGroupConfigRepository extends CrudRepository<ConfigChannelGroup, String>, JpaRepository<ConfigChannelGroup, String> {


}
