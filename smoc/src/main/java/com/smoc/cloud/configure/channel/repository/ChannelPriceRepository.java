package com.smoc.cloud.configure.channel.repository;


import com.smoc.cloud.configure.channel.entity.ConfigChannelPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * 通道价格操作类
 */
public interface ChannelPriceRepository extends CrudRepository<ConfigChannelPrice, String>, JpaRepository<ConfigChannelPrice, String> {


    void deleteByChannelId(String channelId);

    ConfigChannelPrice findByChannelId(String channelId);

    void deleteByChannelIdAndPriceStyle(String channelId, String unified_price);
}
