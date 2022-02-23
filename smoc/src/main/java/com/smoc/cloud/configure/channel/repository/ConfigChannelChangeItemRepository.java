package com.smoc.cloud.configure.channel.repository;

import com.smoc.cloud.configure.channel.entity.ConfigChannelChangeItem;
import com.smoc.cloud.configure.channel.entity.ConfigChannelInterface;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfigChannelChangeItemRepository extends JpaRepository<ConfigChannelChangeItem, String> {

    List<ConfigChannelChangeItem> findConfigChannelChangeItemByChangeIdAndStatus(String changeId,String status);

    List<ConfigChannelChangeItem> findConfigChannelChangeItemByChangeIdOrderByStatusDesc(String changeId);

}