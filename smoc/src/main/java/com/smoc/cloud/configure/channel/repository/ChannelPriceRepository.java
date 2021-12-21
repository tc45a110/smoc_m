package com.smoc.cloud.configure.channel.repository;


import com.smoc.cloud.common.smoc.configuate.validator.ChannelPriceValidator;
import com.smoc.cloud.configure.channel.entity.ConfigChannelPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 通道价格操作类
 */
public interface ChannelPriceRepository extends CrudRepository<ConfigChannelPrice, String>, JpaRepository<ConfigChannelPrice, String> {


    void deleteByChannelId(String channelId);

    ConfigChannelPrice findByChannelId(String channelId);

    void deleteByChannelIdAndPriceStyle(String channelId, String unified_price);

    List<ChannelPriceValidator> findChannelPrice(ChannelPriceValidator channelPriceValidator);

    void deleteByChannelIdAndAreaCode(String channelId, String areaCode);

    void batchSave(ChannelPriceValidator channelPriceValidator);
}
