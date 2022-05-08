package com.smoc.cloud.configure.channel.repository;


import com.smoc.cloud.configure.channel.entity.ConfigChannelInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 通道接口参数操作类
 */
public interface ChannelInterfaceRepository extends CrudRepository<ConfigChannelInterface, String>, JpaRepository<ConfigChannelInterface, String> {


    ConfigChannelInterface findByChannelId(String id);

    Iterable<ConfigChannelInterface> findByIdAndChannelId(String id, String channelId);

    List<ConfigChannelInterface> findBySrcId(String numberCode);
}
