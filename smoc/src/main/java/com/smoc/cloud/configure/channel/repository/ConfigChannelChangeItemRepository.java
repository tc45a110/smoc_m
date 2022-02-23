package com.smoc.cloud.configure.channel.repository;

import com.smoc.cloud.configure.channel.entity.ConfigChannelChangeItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigChannelChangeItemRepository extends JpaRepository<ConfigChannelChangeItem, String> {
}